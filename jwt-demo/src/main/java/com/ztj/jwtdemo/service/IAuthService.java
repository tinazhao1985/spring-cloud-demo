package com.ztj.jwtdemo.service;

import com.ztj.jwtdemo.vo.LoginRequest;
import com.ztj.jwtdemo.common.bean.SuccessResponse;

public interface IAuthService {

    SuccessResponse login(LoginRequest request);

    void logout();
}
