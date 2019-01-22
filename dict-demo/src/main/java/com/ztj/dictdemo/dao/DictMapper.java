package com.ztj.dictdemo.dao;

import com.ztj.dictdemo.model.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictMapper {

    /**
     * 获取全部字典
     *
     * @return
     */
    List<Dict> selectAll();

    /**
     * 根据父节点code获取子数据字典
     *
     * @param pcode
     * @return
     */
    List<Dict> selectByPcode(String pcode);

    /**
     * 根据父节点编码获取编码不是code的子数据字典
     *
     * @param pcode
     * @return
     */
    List<Dict> selectByPcodeExclude(@Param("pcode") String pcode, @Param("code") String code);

    /**
     * 根据code获取数据字典
     *
     * @param code
     * @return
     */
    List<Dict> selectByCode(String code);

    /**
     * 根据code和pcode获取数据字典
     *
     * @param pcode
     * @param code
     * @return
     */
    Dict selectByCodeAndPcode(@Param("pcode") String pcode, @Param("code") String code);

    /**
     * 新增
     *
     * @param record
     */
    int insertSelective(Dict record);

    /**
     * 修改
     * @param record
     */
    void updateSelective(Dict record);

    /**
     * 根据code删除
     *
     * @param code
     */
    void deleteByCodeAndPCode(@Param("code") String code, @Param("pcode") String pcode);

}
