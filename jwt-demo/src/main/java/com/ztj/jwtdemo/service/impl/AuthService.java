package com.ztj.jwtdemo.service.impl;

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

    @Override
    public SuccessResponse login(LoginRequest request) {
        // todo: 验证登陆用户

        // 生成token
        JWTInfo info = new JWTInfo(request.getAccount(), "Admin", "Admin");
        String token = jwtUtils.generateToken(info);

        return new SuccessResponse(token);
    }

    @Override
    public void logout() {

    }

}
