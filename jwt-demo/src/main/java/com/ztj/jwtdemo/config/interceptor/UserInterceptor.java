package com.ztj.jwtdemo.config.interceptor;

import com.ztj.jwtdemo.common.jwt.JWTInfo;
import com.ztj.jwtdemo.common.jwt.JWTProperties;
import com.ztj.jwtdemo.common.jwt.JWTUtils;
import com.ztj.jwtdemo.config.context.BaseContextHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTProperties jwtProperties;

    @Autowired
    private JWTUtils jwtUtils;

    /**
     *  在Controller方法调用之前处理
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 是否携带token
        String token = request.getHeader(jwtProperties.getHeader());
        if(StringUtils.isBlank(token)) {
            return false;
        }

        // token是否过期
        if(jwtUtils.isTokenExpired(token)) {
            return false;
        }

        JWTInfo info = jwtUtils.getInfoFromToken(token);
        BaseContextHandler.setUserAccount(info.getUserAccount());
        BaseContextHandler.setUserName(info.getUserName());
        BaseContextHandler.setUserDept(info.getUserDept());
        return true;
    }

    /**
     * 在Controller方法调用之后处理
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // todo
    }

    /**
     * 在请求结束后调用
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }
}
