package com.ztj.jwtdemo.service;

import com.ztj.jwtdemo.common.bean.BaseResponse;
import com.ztj.jwtdemo.model.Dict;
import com.ztj.jwtdemo.vo.DictTree;

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
