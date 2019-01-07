package com.ztj.jwtdemo.dao;

import com.ztj.jwtdemo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    User selectByAccount(String account);

    void updateLoginStatus(@Param("loginStatus") Boolean loginStatus, @Param("account") String account);

}