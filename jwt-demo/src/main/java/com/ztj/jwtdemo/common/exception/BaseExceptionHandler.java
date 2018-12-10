package com.ztj.jwtdemo.common.exception;

import com.ztj.jwtdemo.common.bean.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BaseException.class)
    public ErrorResponse handleBaseException(BaseException e){
        return new ErrorResponse(e.getCode(), e.getMessage(), null);
    }
}
