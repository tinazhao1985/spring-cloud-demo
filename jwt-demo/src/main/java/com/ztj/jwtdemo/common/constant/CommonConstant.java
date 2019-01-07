package com.ztj.jwtdemo.common.constant;

/**
 * 公共常量
 */
public enum CommonConstant {

    OP_SUCCESS(0, "operation.success"),
    OP_FAILED(1, "operation.failed"),
    NOT_LOGIN_ERROR(2, "not.login"),
    TOKEN_EXPIRED(3, "token.expired"),
    USER_NOT_EXIST(4, "user.not.exists");


    CommonConstant(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
