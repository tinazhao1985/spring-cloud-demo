package com.ztj.jwtdemo.common.exception;

import lombok.Data;

@Data
public class BaseException extends RuntimeException{

    private String msg;
    private Integer code;

    public BaseException(Integer code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

}
