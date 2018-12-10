package com.ztj.jwtdemo.common.bean;

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

    /**
     * 国际化
     *
     * @param result
     * @return
     */
    public String getMessage(String result, Object[] params) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setCacheSeconds(-1);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setBasenames("/i18n/messages");

        String message = "";
        try {
            Locale locale = LocaleContextHolder.getLocale();
            message = messageSource.getMessage(result, params, locale);
        } catch (Exception e) {
            log.error("parse message error! ", e);
        }
        return message;
    }

}