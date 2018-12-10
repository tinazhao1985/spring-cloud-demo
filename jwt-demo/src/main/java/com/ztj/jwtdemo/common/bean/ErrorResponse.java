package com.ztj.jwtdemo.common.bean;

import com.ztj.jwtdemo.common.constant.CommonConstant;

public class ErrorResponse extends BaseResponse {

    public ErrorResponse() {
        this.setCode(CommonConstant.OP_FAILED.getCode());
        String message = getMessage(CommonConstant.OP_FAILED.getMessage(), null);
        this.setMessage(message);
    }

    public ErrorResponse(String message, Object[] params) {
        this.setCode(CommonConstant.OP_FAILED.getCode());
        String msg = getMessage(message, params);
        this.setMessage(msg);
    }

    public ErrorResponse(Integer code, String message, Object[] params) {
        this.setCode(code);
        String msg = getMessage(message, params);
        this.setMessage(msg);
    }

}
