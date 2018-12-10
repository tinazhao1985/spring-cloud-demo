package com.ztj.jwtdemo.service;

import com.ztj.jwtdemo.common.bean.BaseResponse;
import com.ztj.jwtdemo.vo.LoginRequest;

public interface IAuthService {

    BaseResponse login(LoginRequest request);

    void logout();
}
