package com.ztj.dictdemo.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典树
 */
@Data
public class DictTree {

    /**
     * 编码
     */
    private String code;

    /**
     * 父节点编码
     */
    private String pcode;

    /**
     * 中文名称
     */
    private String nameCn;

    /**
     * 英文名称
     */
    private String nameEn;

    /**
     * 提示
     */
    private String tips;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 子节点
     */
    List<DictTree> children = new ArrayList<>();

    /**
     * 添加子节点
     *
     * @param node
     */
    public void add(DictTree node){
        children.add(node);
    }

}
