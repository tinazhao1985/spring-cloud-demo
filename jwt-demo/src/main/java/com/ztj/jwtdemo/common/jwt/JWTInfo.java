package com.ztj.jwtdemo.common.jwt;

import lombok.Data;

/**
 * JWT结构体
 */
@Data
public class JWTInfo {

    private String userAccount;

    private String userName;

    private String userDept;

    public JWTInfo(String userAccount, String userName, String userDept) {
        this.userAccount = userAccount;
        this.userName = userName;
        this.userDept = userDept;
    }
}
