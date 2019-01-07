package com.ztj.jwtdemo.model;

import lombok.Data;

@Data
public class User {

    private String account;

    private String name;

    private String department;

    private Boolean loginStatus;

    private String password;

}
