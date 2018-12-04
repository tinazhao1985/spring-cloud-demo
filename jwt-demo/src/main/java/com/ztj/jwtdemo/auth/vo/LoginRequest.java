package com.ztj.jwtdemo.auth.vo;

import lombok.Data;

@Data
public class LoginRequest {

    private String account;

    private String password;
}
