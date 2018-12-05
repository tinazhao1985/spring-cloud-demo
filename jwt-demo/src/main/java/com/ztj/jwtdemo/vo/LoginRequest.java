package com.ztj.jwtdemo.vo;

import lombok.Data;

@Data
public class LoginRequest {

    private String account;

    private String password;
}
