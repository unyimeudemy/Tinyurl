package com.piraxx.tinyurl.controllers;


import com.piraxx.tinyurl.services.UrlService;
import com.piraxx.tinyurl.utils.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/url")
public class UrlController {

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Autowired
    private UrlService urlService;

    @GetMapping("/test")
    public String test(){
        String url = "";
        return urlService.getUrlKey(url);
    }
}
