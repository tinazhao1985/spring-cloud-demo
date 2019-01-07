package com.ztj.jwtdemo.config.interceptor;

import com.ztj.jwtdemo.common.constant.CommonConstant;
import com.ztj.jwtdemo.common.exception.BaseException;
import com.ztj.jwtdemo.common.jwt.JWTInfo;
import com.ztj.jwtdemo.common.jwt.JWTProperties;
import com.ztj.jwtdemo.common.jwt.JWTUtils;
import com.ztj.jwtdemo.config.context.BaseContextHandler;
import com.ztj.jwtdemo.dao.UserMapper;
import com.ztj.jwtdemo.model.User;
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

    @Autowired
    private UserMapper userMapper;

    /**
     *  在Controller方法调用之前处理
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 是否携带token
        String token = request.getHeader(jwtProperties.getHeader());
        if(StringUtils.isBlank(token)) {
            throw new BaseException(CommonConstant.NOT_LOGIN_ERROR.getCode(),
                    CommonConstant.NOT_LOGIN_ERROR.getMessage());
        }

        // token是否过期
        if(jwtUtils.isTokenExpired(token)) {
            throw new BaseException(CommonConstant.TOKEN_EXPIRED.getCode(), CommonConstant.TOKEN_EXPIRED.getMessage());
        }

        JWTInfo info = jwtUtils.getInfoFromToken(token);
        String account = info.getUserAccount();
        User user = userMapper.selectByAccount(account);
        if(null == user) {
            throw new BaseException(CommonConstant.USER_NOT_EXIST.getCode(),
                    CommonConstant.USER_NOT_EXIST.getMessage());
        }

        if(!user.getLoginStatus()) {
            throw new BaseException(CommonConstant.NOT_LOGIN_ERROR.getCode(), CommonConstant.NOT_LOGIN_ERROR.getMessage());
        }

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
    }

    /**
     * 在请求结束后调用
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
