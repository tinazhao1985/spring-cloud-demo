package com.ztj.jwtdemo.controller;

import com.ztj.jwtdemo.common.bean.BaseResponse;
import com.ztj.jwtdemo.common.bean.SuccessResponse;
import com.ztj.jwtdemo.service.IAuthService;
import com.ztj.jwtdemo.vo.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    /**
     * 登出
     *
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout() {
        authService.logout();
    }

    /**
     * 测试token
     *
     * @return
     */
    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public BaseResponse token() {
        return new SuccessResponse("Testing is passed.");
    }

}
