package com.ztj.jwtdemo.common.util;

/**
 * 字符串公共方法
 */
public class StringUtils {

    public static String objectToString(Object obj) {
        if(null == obj) {
            return "";
        }
        return obj.toString();
    }
}
