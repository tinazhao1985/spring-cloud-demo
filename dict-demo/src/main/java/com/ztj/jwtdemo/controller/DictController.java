package com.ztj.jwtdemo.controller;

import com.ztj.jwtdemo.common.bean.BaseResponse;
import com.ztj.jwtdemo.common.constant.DictConstant;
import com.ztj.jwtdemo.model.Dict;
import com.ztj.jwtdemo.service.IDictService;
import com.ztj.jwtdemo.vo.DictTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("dict")
public class DictController {

    @Autowired
    private IDictService dictService;

    /**
     * 查询树，查询联动下拉框
     *
     * @return
     */
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<DictTree> tree(@RequestParam(name="code",required = false) String code) {
        return dictService.getTree(code);
    }

    /**
     * 新增节点
     *
     * @param dict
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public BaseResponse addDict(@RequestBody Dict dict) {
        return dictService.addDict(dict);
    }

    /**
     * 编辑
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public BaseResponse get(@RequestParam String code) {
        return dictService.getDict(code);
    }

    /**
     * 修改节点
     *
     * @param dict
     * @return
     */
    @RequestMapping(value = "/upd",method = RequestMethod.PUT)
    public BaseResponse updateDict(@RequestBody Dict dict) {
        return dictService.updateDict(dict);
    }

    /**
     * 删除数据字典
     *
     * @param code
     * @param pcode
     * @return
     */
    @RequestMapping(value = "/del",method = RequestMethod.DELETE)
    public BaseResponse deleteDict(@RequestParam String code, @RequestParam String pcode){
        return dictService.deleteBothByPcode(code, pcode);
    }

    /**
     * 启用
     *
     * @param code
     * @param pcode
     * @return
     */
    @RequestMapping(value = "/enable",method = RequestMethod.PUT)
    public BaseResponse enableDict(@RequestParam String code, @RequestParam String pcode) {
        return dictService.updStatus(code, pcode, DictConstant.DICT_STATUS_ENABLE);
    }

    /**
     * 禁用
     *
     * @param code
     * @param pcode
     * @return
     */
    @RequestMapping(value = "/disable",method = RequestMethod.PUT)
    public BaseResponse disableDict(@RequestParam String code, @RequestParam String pcode) {
        return dictService.updStatus(code, pcode, DictConstant.DICT_STATUS_DISABLE);
    }

}
