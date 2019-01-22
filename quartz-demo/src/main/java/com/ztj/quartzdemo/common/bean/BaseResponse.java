package com.ztj.quartzdemo.common.bean;

import com.ztj.quartzdemo.common.util.MessageUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 响应基本信息
 */
@Data
@Slf4j
public class BaseResponse {

    private Integer code;

    private String message;

    public String getMessage(String result, Object[] params) {
        return MessageUtil.getMessage(result, params);
    }

}