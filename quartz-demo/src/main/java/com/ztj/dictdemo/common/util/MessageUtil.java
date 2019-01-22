package com.ztj.jwtdemo.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Slf4j
public class MessageUtil {

    /**
     * 国际化
     *
     * @param result
     * @return
     */
    public static String getMessage(String result, Object[] params) {
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
