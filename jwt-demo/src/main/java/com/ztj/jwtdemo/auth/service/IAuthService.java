package com.ztj.jwtdemo.auth.service;

import com.ztj.jwtdemo.auth.vo.LoginRequest;
import com.ztj.jwtdemo.common.bean.SuccessResponse;

public interface IAuthService {

    SuccessResponse login(LoginRequest request);

    void logout();
}
