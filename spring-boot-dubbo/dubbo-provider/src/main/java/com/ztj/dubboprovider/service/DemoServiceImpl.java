package com.ztj.dubboprovider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.ztj.api.DemoService;

@Service
public class DemoServiceImpl implements DemoService {

    @Override
    public String hello(String name) {
        return "Hello, " + name;
    }

}
