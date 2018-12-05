package com.ztj.jwtdemo.common.util;

/**
 * 字符串公共方法
 */
public class StringUtils {

    public static String returnObjectValue(Object obj) {
        if(null == obj) {
            return "";
        }
        return obj.toString();
    }
}
