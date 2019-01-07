package com.ztj.jwtdemo.service.impl;

import com.ztj.jwtdemo.common.bean.BaseResponse;
import com.ztj.jwtdemo.common.bean.ErrorResponse;
import com.ztj.jwtdemo.common.jwt.JWTProperties;
import com.ztj.jwtdemo.config.context.BaseContextHandler;
import com.ztj.jwtdemo.dao.UserMapper;
import com.ztj.jwtdemo.model.User;
import com.ztj.jwtdemo.service.IAuthService;
import com.ztj.jwtdemo.vo.LoginRequest;
import com.ztj.jwtdemo.common.bean.SuccessResponse;
import com.ztj.jwtdemo.common.jwt.JWTInfo;
import com.ztj.jwtdemo.common.jwt.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 认证和鉴权
 */
@Service
public class AuthService implements IAuthService {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private JWTProperties jwtProperties;

    @Autowired
    private UserMapper userMapper;

    /**
     * 登陆
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse login(LoginRequest request) {
        // 验证登陆用户
        String account = request.getAccount();
        User user = userMapper.selectByAccount(account);
        if(null == user) {
            return new ErrorResponse("user.not.exists", null);
        }

        String password = user.getPassword();
        if(password.equals(request.getPassword())) {
            // 生成token
            JWTInfo info = new JWTInfo(account, user.getName(), user.getDepartment());
            String token = jwtUtils.generateToken(info);

            // 修改登录状态
            userMapper.updateLoginStatus(true, account);

            return new SuccessResponse(token);
        }

        return new ErrorResponse("login.error", null);
    }

    /**
     * 登出
     *
     */
    @Override
    public void logout() {
        String account = BaseContextHandler.getUserAccount();
        userMapper.updateLoginStatus(false, account);
    }

}
