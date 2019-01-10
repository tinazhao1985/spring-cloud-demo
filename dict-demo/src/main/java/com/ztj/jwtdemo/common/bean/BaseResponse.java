package com.ztj.jwtdemo.common.bean;

import com.ztj.jwtdemo.common.util.MessageUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

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