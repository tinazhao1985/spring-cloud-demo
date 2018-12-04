package com.ztj.jwtdemo.common.bean;

import com.ztj.jwtdemo.common.constant.CommonConstant;

public class ErrorResponse extends BaseResponse {

    public ErrorResponse() {
        this.setCode(CommonConstant.OP_FAILED);
        String message = getMessage("operation.failed", null);
        this.setMessage(message);
    }

    public ErrorResponse(String message, Object[] params) {
        this.setCode(CommonConstant.OP_FAILED);
        String msg = getMessage(message, params);
        this.setMessage(msg);
    }

    public ErrorResponse(String code, String message, Object[] params) {
        this.setCode(code);
        String msg = getMessage(message, params);
        this.setMessage(msg);
    }

}
