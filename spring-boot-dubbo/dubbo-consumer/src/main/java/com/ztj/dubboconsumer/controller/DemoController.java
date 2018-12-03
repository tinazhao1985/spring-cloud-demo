package com.ztj.dubboconsumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ztj.api.DemoService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Reference
    private DemoService service;

    @RequestMapping("/hello")
    public String hello(@RequestParam String name) {
        return service.hello(name);
    }

}
