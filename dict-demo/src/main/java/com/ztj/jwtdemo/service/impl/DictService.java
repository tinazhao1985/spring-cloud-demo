package com.ztj.jwtdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.ztj.jwtdemo.common.bean.BaseResponse;
import com.ztj.jwtdemo.common.bean.ErrorResponse;
import com.ztj.jwtdemo.common.bean.SuccessResponse;
import com.ztj.jwtdemo.common.constant.DictConstant;
import com.ztj.jwtdemo.common.util.DictTreeUtil;
import com.ztj.jwtdemo.common.util.MessageUtil;
import com.ztj.jwtdemo.dao.DictMapper;
import com.ztj.jwtdemo.model.Dict;
import com.ztj.jwtdemo.service.IDictService;
import com.ztj.jwtdemo.vo.DictTree;
import com.ztj.jwtdemo.vo.FrontDict;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class DictService implements IDictService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DictMapper mapper;

    /**
     * 字典树和下拉框联动树
     * 注：
     * 全部数需要所有的字典信息，包括启用和禁用的
     * 联动下拉框只要启用的
     *
     * @return
     */
    @Override
    public List<DictTree> getTree(String code) {
        // 从db中获取全部字典
        List<Dict> list = mapper.selectAll();
        if(null == list || list.isEmpty()) {
            return null;
        }

        if(StringUtils.isEmpty(code)) {
            // 用所有的字典生成树
            return getTree(list, "");
        }

        // 根据传入的字典编码生成树
        List<Dict> dictList = new ArrayList<>();
        // 父节点
        List<Dict> pDict = mapper.selectByCode(code);
        if(null == pDict || pDict.isEmpty()) {
            return null;
        } else {
            if(pDict.get(0).getStatus() == DictConstant.DICT_STATUS_DISABLE) {
                // 父节点被禁用
                return null;
            }
        }

        dictList.add(pDict.get(0));
        // 获取子节点
        getChildrenByPcode(list, code, dictList);
        return getTree(dictList, pDict.get(0).getPcode());
    }

    /**
     * 根据编码获取所有的子字典
     *
     * @param dictAll
     * @param code
     * @param dictResult
     */
    @Override
    public void getChildrenByPcode( List<Dict> dictAll, String code, List<Dict> dictResult){
        for(Dict dict: dictAll){
            if(code.equals(dict.getPcode())){
                // 递归遍历下一级
                getChildrenByPcode(dictAll, dict.getCode(), dictResult);
                dictResult.add(dict);
            }
        }
    }

    /**
     * 生成树
     *
     * @param dictList
     * @param root
     * @return
     */
    private List<DictTree> getTree(List<Dict> dictList, String root) {
        // 根节点为空则需要全部的数据，不为空则跳过禁用的数据
        boolean skip = false;
        if(StringUtils.isNotEmpty(root)) {
            skip = true;
        }

        List<DictTree> trees = new ArrayList<DictTree>();
        DictTree node;
        for (Dict dict : dictList) {
            if(skip && dict.getStatus() == DictConstant.DICT_STATUS_DISABLE) {
                continue;
            }

            node = new DictTree();
            node.setCode(dict.getCode());
            node.setPcode(dict.getPcode());
            node.setNameCn(dict.getNameCn());
            if(StringUtils.isNotEmpty(dict.getNameEn())) {
                node.setNameEn(dict.getNameEn());
            }
            node.setTips(dict.getTips());
            node.setStatus(dict.getStatus());
            trees.add(node);
        }
        return DictTreeUtil.bulid(trees, root);
    }

    /**
     * 新增子节点
     *
     * @param dict
     * @return
     */
    @Override
    public BaseResponse addDict(Dict dict) {
        // 填写校验
        String msg = checkInput(dict);
        if(StringUtils.isNotEmpty(msg)) {
            return new ErrorResponse(msg);
        }

        String code = dict.getCode();
        String pcode = dict.getPcode();

        // 父节点编码是否存在且唯一
        List<Dict> pDictList = mapper.selectByCode(pcode);
        if(pDictList == null || pDictList.isEmpty()){
            return new ErrorResponse("dict.pcode.notexists", null);
        } else if(pDictList.size() > 1) {
            return new ErrorResponse("dict.pcode.notUnique", null);
        }

        // 父节点
        Dict pDict = pDictList.get(0);

        // 缓存格式
        FrontDict frontDict = new FrontDict();

        List<Dict> children = mapper.selectByPcode(dict.getPcode());
        if(null == children || children.isEmpty()) {
            // 父节点下无子节点，需要新增cache
            frontDict.setCode(pDict.getCode());
            frontDict.setPcode(pDict.getPcode());
            frontDict.setNameCn(pDict.getNameCn());
            frontDict.setStatus(pDict.getStatus());

            String nameEn = pDict.getNameEn();
            if(StringUtils.isNotEmpty(nameEn)) {
                frontDict.setNameEn(nameEn);
            }
            String tips = pDict.getTips();
            if(StringUtils.isNotEmpty(tips)) {
                frontDict.setTips(tips);
            }
            if(null != pDict.getOrderNum()) {
                frontDict.setOrderNum(pDict.getOrderNum());
            }
            children = new ArrayList<>();
            children.add(dict);
            frontDict.setChildren(children);
            setDictMapToCache(pcode, frontDict);

        } else {
            // 同一个父节点下的子节点编码不能重复
            for(Dict child : children) {
                if(code.equals(child.getCode())) {
                    return new ErrorResponse("dict.code.repeat", null);
                }
            }

            // 更新缓存
            String key = DictConstant.DICT_KEY_PREFIX + pcode;
            String cache = redisTemplate.opsForValue().get(key);
            frontDict = JSON.parseObject(cache, FrontDict.class);
            frontDict.getChildren().add(dict);
            setDictMapToCache(pcode, frontDict);
        }

        // 入库
        mapper.insertSelective(dict);

        return new SuccessResponse(null);
    }

    /**
     * 编辑
     *
     * @param code
     * @return
     */
    @Override
    public BaseResponse getDict(String code) {
        String key = DictConstant.DICT_KEY_PREFIX + code;
        String cache = redisTemplate.opsForValue().get(key);
        if(cache == null) {
            // 缓存中不存在，从数据库中获取
            List<Dict> dicts = mapper.selectByCode(code);
            if(null == dicts || dicts.isEmpty()) {
                return new ErrorResponse("operation.failed.data.notexists", null);
            }

            FrontDict frontDict = new FrontDict();
            frontDict.setCode(dicts.get(0).getCode());
            frontDict.setPcode(dicts.get(0).getPcode());
            frontDict.setNameCn(dicts.get(0).getNameCn());
            frontDict.setNameEn(dicts.get(0).getNameEn());
            frontDict.setTips(dicts.get(0).getTips());
            frontDict.setStatus(dicts.get(0).getStatus());
            if(null != dicts.get(0).getOrderNum()) {
                frontDict.setOrderNum(dicts.get(0).getOrderNum());
            }
            return new SuccessResponse(frontDict);
        } else {
            FrontDict frontDict = JSON.parseObject(cache, FrontDict.class);
            return new SuccessResponse(frontDict);
        }
    }

    /**
     * 根据父节点编码和子节点编码修改子节点
     *
     * @param dict
     * @return
     */
    @Override
    public BaseResponse updateDict(Dict dict) {
        // 填写校验
        String msg = checkInput(dict);
        if(StringUtils.isNotEmpty(msg)) {
            return new ErrorResponse(msg);
        }

        String code = dict.getCode();
        String pcode = dict.getPcode();

        // 判断该节点是否存在
        Dict oldDict = mapper.selectByCodeAndPcode(pcode, code);
        if(null == oldDict) {
            return new ErrorResponse("dict.code.notexists", null);
        }

        // 父节点编码是否存在且唯一
        List<Dict> pDictList = mapper.selectByCode(pcode);
        if(pDictList == null || pDictList.isEmpty()){
            return new ErrorResponse("dict.pcode.notexists", null);
        } else if(pDictList.size() > 1) {
            return new ErrorResponse("dict.pcode.notUnique", null);
        }

        // 先入库再更新缓存，方便排序
        mapper.updateSelective(dict);

        // 更新缓存
        String key = DictConstant.DICT_KEY_PREFIX + pcode;
        String cache = redisTemplate.opsForValue().get(key);
        FrontDict frontDict = JSON.parseObject(cache, FrontDict.class);

        // 重新获取父节点下的所有子节点
        List<Dict> allDicts = mapper.selectByPcode(pcode);
        frontDict.setChildren(allDicts);
        setDictMapToCache(pcode, frontDict);

        return new SuccessResponse(null);
    }

    /**
     * 删除子节点
     *
     * @param code
     * @param pcode
     */
    @Override
    public BaseResponse deleteBothByPcode(String code, String pcode) {
        // 判断子节点下是否还有节点
        List<Dict> children = mapper.selectByPcode(code);
        if(null != children && children.size() > 0) {
            return new ErrorResponse("dict.children.notnull", null);
        }

        // 判断父节点下是否有其他子节点
        List<Dict> otherDicts = mapper.selectByPcodeExclude(pcode, code);
        if(null == otherDicts || otherDicts.isEmpty()) {
            // 删除父节点缓存
            deleteDictFromCache(pcode);
        } else {
            // 更新父节点缓存
            String key = DictConstant.DICT_KEY_PREFIX + pcode;
            String cache = redisTemplate.opsForValue().get(key);
            FrontDict frontDict = JSON.parseObject(cache, FrontDict.class);
            frontDict.setChildren(otherDicts);
            setDictMapToCache(pcode, frontDict);
        }

        // 删除该节点
        mapper.deleteByCodeAndPCode(code, pcode);

        return new SuccessResponse(null);
    }

    /**
     * 启用/禁用
     *
     * @param code
     * @param pcode
     * @param status
     * @return
     */
    @Override
    public BaseResponse updStatus(String code, String pcode, int status) {
        Dict oldDict = mapper.selectByCodeAndPcode(pcode, code);
        if(null == oldDict) {
            return new ErrorResponse("dict.code.notexists", null);
        }

        // 更新缓存--父节点
        String key = DictConstant.DICT_KEY_PREFIX + pcode;
        String cache = redisTemplate.opsForValue().get(key);
        if(StringUtils.isEmpty(cache)) {
            log.error("code-{}, pcode-{} can not found in cache. ", code, pcode);
            return new ErrorResponse("dict.code.notexists", null);
        }

        FrontDict frontDict = JSON.parseObject(cache, FrontDict.class);
        List<Dict> dictList = frontDict.getChildren();
        if(null == dictList || dictList.isEmpty()) {
            log.error("children in cache - {} is empty. ", pcode);
            return new ErrorResponse("dict.code.notexists", null);
        } else {
            boolean notExists = true;
            for(Dict item : dictList) {
                if(item.getCode().equals(code)) {
                    item.setStatus(status);
                    notExists = false;
                    break;
                }
            }
            if(notExists) {
                log.error("can not found {} from cache - {}. ", code, pcode);
                return new ErrorResponse("dict.code.notexists", null);
            }
            setDictMapToCache(pcode, frontDict);
        }

        // 更新缓存--子节点
        key = DictConstant.DICT_KEY_PREFIX + code;
        cache = redisTemplate.opsForValue().get(key);
        if(StringUtils.isNotEmpty(cache)) {
            frontDict = JSON.parseObject(cache, FrontDict.class);
            frontDict.setStatus(status);
            setDictMapToCache(pcode, frontDict);
        } else {
            log.info("code {} has no children. ", code);
        }

        // 入库
        Dict dict = new Dict();
        dict.setCode(code);
        dict.setPcode(pcode);
        dict.setStatus(status);
        mapper.updateSelective(dict);

        return new SuccessResponse(null);
    }

    /**
     * 获取子集数据字典并写入cache
     * 用于应用启动时初始化cache
     *
     * @return
     */
    @Override
    public void getDictList() {
        // 清空缓存
        redisTemplate.delete(redisTemplate.keys(DictConstant.DICT_KEY_PREFIX + "*"));

        // 从db中读取
        List<Dict> dictAll = mapper.selectAll();
        if (null == dictAll || dictAll.size() == 0) {
            return;
        }

        for (Dict dict : dictAll) {
            FrontDict frontDict = new FrontDict();
            frontDict.setCode(dict.getCode());
            frontDict.setPcode(dict.getPcode());
            frontDict.setNameCn(dict.getNameCn());
            frontDict.setStatus(dict.getStatus());

            String tips = dict.getTips();
            if (StringUtils.isNotEmpty(tips)) {
                frontDict.setTips(tips);
            }
            String nameEn = dict.getNameEn();
            if (StringUtils.isNotEmpty(nameEn)) {
                frontDict.setNameEn(nameEn);
            }
            if (null != dict.getOrderNum()) {
                frontDict.setOrderNum(frontDict.getOrderNum());
            }

            List<Dict> children = findChildren(dictAll, dict.getCode());
            if (null == children || children.isEmpty()) {
                // 无子节点的字典
                continue;
            }
            frontDict.setChildren(children);

            // 写入缓存
            String key = DictConstant.DICT_KEY_PREFIX + dict.getCode();
            String value = JSON.toJSONString(frontDict);
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 查询子节点
     *
     * @param dictAll
     * @param pcode
     * @return
     */
    private List<Dict> findChildren(List<Dict> dictAll, String pcode) {
        List<Dict> dicts = new ArrayList<>();
        for(Dict dict : dictAll) {
            if(pcode.equals(dict.getPcode())) {
                dicts.add(dict);
            }
        }
        return dicts;
    }

    /**
     * 更新缓存
     *
     * @param code
     * @param frontDict
     * @return
     */
    private void setDictMapToCache(String code, FrontDict frontDict) {
        String key = DictConstant.DICT_KEY_PREFIX + code;
        String value = JSON.toJSONString(frontDict);

        String oldCache = redisTemplate.opsForValue().get(key);
        if(oldCache == null) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.delete(key);
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 根据Key删除缓存
     *
     * @param key
     */
    private void deleteDictFromCache(String key) {
        redisTemplate.delete(DictConstant.DICT_KEY_PREFIX + key);
    }

    /**
     * 输入校验
     *
     * @param dict
     * @return
     */
    private String checkInput(Dict dict) {
        StringBuffer msg = new StringBuffer();

        String nameCn = dict.getNameCn();
        if(StringUtils.isEmpty(nameCn)) {
            msg.append(MessageUtil.getMessage("dict.nameCn.null", null));
        }

        String pcode = dict.getPcode();
        if(StringUtils.isEmpty(pcode)) {
            msg.append(MessageUtil.getMessage("dict.pcode.null", null));
        }

        String code = dict.getCode();
        if(StringUtils.isEmpty(code)) {
            msg.append(MessageUtil.getMessage("dict.code.null", null));
        }

        if(null == dict.getStatus()) {
            msg.append(MessageUtil.getMessage("dict.status.null", null));
        }

        return msg.toString();
    }

}
