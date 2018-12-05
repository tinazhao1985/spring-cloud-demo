package com.ztj.jwtdemo.config.context;

import com.ztj.jwtdemo.common.jwt.JWTConstant;
import com.ztj.jwtdemo.common.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class BaseContextHandler {
    public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    public static Object get(String key){
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        return map.get(key);
    }

    public static String getUserAccount(){
        Object value = get(JWTConstant.USER_ACCOUNT);
        return StringUtils.returnObjectValue(value);
    }

    public static void setUserAccount(String userAccount){
        set(JWTConstant.USER_ACCOUNT, userAccount);
    }

    public static String getUserName(){
        Object value = get(JWTConstant.USER_NAME);
        return StringUtils.returnObjectValue(value);
    }

    public static void setUserName(String userName){
        set(JWTConstant.USER_NAME, userName);
    }

    public static String getUserDept(){
        Object value = get(JWTConstant.USER_DEPT);
        return StringUtils.returnObjectValue(value);
    }

    public static void setUserDept(String userDept){
        set(JWTConstant.USER_DEPT, userDept);
    }

}
