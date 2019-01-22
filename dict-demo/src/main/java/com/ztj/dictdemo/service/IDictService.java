package com.ztj.dictdemo.service;

import com.ztj.dictdemo.common.bean.BaseResponse;
import com.ztj.dictdemo.model.Dict;
import com.ztj.dictdemo.vo.DictTree;

import java.util.List;

public interface IDictService {

    List<DictTree> getTree(String code);

    void getChildrenByPcode(List<Dict> dictAll, String code, List<Dict> dictResult);

    BaseResponse addDict(Dict dict);

    BaseResponse getDict(String code);

    BaseResponse updateDict(Dict dict);

    BaseResponse deleteBothByPcode(String code, String pcode);

    void getDictList();

    BaseResponse updStatus(String code, String pcode, int status);

}
