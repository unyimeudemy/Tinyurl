package com.piraxx.tinyurl.controllers;


import com.piraxx.tinyurl.utils.SnowflakeIdGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/url")
public class UrlController {

    @GetMapping("/test")
    public long test(){

        long workerId = 1L;
        long datacenterId = 1L;

        SnowflakeIdGenerator  idGenerator = new SnowflakeIdGenerator(workerId, datacenterId);
        System.out.println("===================================" + idGenerator.nextId());
        return idGenerator.nextId();
    }
}
