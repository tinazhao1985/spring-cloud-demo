package com.ztj.dictdemo.common.bean;

import com.ztj.dictdemo.common.constant.CommonConstant;
import lombok.Data;

@Data
public class SuccessResponse<T> extends BaseResponse {

    private T data;

    public SuccessResponse(T data) {
        this.setCode(CommonConstant.OP_SUCCESS.getCode());
        this.setData(data);
        String message = getMessage(CommonConstant.OP_SUCCESS.getMessage(), null);
        this.setMessage(message);
    }

    public SuccessResponse(T data, String message) {
        this.setCode(CommonConstant.OP_SUCCESS.getCode());
        this.setData(data);
        this.setMessage(message);
    }

}