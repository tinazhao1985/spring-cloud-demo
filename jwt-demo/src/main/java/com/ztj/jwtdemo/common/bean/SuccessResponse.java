package com.ztj.jwtdemo.common.bean;

import com.ztj.jwtdemo.common.constant.CommonConstant;
import lombok.Data;

@Data
public class SuccessResponse<T> extends BaseResponse {

    private T data;

    public SuccessResponse(T data) {
        this.setCode(CommonConstant.OP_SUCCESS);
        this.setData(data);
        String message = getMessage("operation.success", null);
        this.setMessage(message);
    }

    public SuccessResponse(T data, String message) {
        this.setCode(CommonConstant.OP_SUCCESS);
        this.setData(data);
        this.setMessage(message);
    }

}