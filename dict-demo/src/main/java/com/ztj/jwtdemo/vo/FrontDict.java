package com.ztj.jwtdemo.vo;

import com.ztj.jwtdemo.model.Dict;
import lombok.Data;

import java.util.List;

/**
 * 前端的数据字典结构
 */
@Data
public class FrontDict {

    private String code;

    private String pcode;

    private String nameCn;

    private String nameEn;

    private String tips;

    private Integer orderNum;

    private Integer status;

    /**
     * 子集
     */
    private List<Dict> children;

}
