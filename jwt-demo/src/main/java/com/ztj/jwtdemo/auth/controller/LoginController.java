package com.ztj.jwtdemo.auth.controller;

import com.ztj.jwtdemo.auth.service.IAuthService;
import com.ztj.jwtdemo.auth.vo.LoginRequest;
import com.ztj.jwtdemo.common.bean.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登陆和登出
 */
@RestController
public class LoginController {

    @Autowired
    private IAuthService authService;

    /**
     * 登陆
     *
     * @param request
     * @return
     */
    public SuccessResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

}
