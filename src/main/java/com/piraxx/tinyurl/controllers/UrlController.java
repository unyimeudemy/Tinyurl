package com.piraxx.tinyurl.controllers;


import com.piraxx.tinyurl.domain.requests.UrlRequestDto;
import com.piraxx.tinyurl.services.ServicesImpl.BloomFilter;
import com.piraxx.tinyurl.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/url")
public class UrlController {


    @Autowired
    private UrlService urlService;

    @Autowired
    private BloomFilter bloomFilter;


    @GetMapping("/keepAlive")
    public String test(){
        return "yes";
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generate(@RequestBody UrlRequestDto urlRequestDto){
        String shortUrl = urlService.generateShort(urlRequestDto.getUrl());
        return new ResponseEntity<>(
                UrlRequestDto.builder().url(shortUrl).build(),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/add_spam_url")
    public ResponseEntity<?> addSpamUrl(@RequestBody UrlRequestDto urlRequestDto){
        String spamUrl = urlService.addSpamUrl(urlRequestDto.getUrl());
        return new ResponseEntity<>(
                UrlRequestDto.builder().url(spamUrl).build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/get-url")
    public ResponseEntity<?> getOriginalUrl(@RequestBody UrlRequestDto urlRequestDto){
        System.out.println("------------------------------" + urlRequestDto.getUrl());
        String legitUrl = urlService.findByKey(urlRequestDto.getUrl());
        return new ResponseEntity<>(
                UrlRequestDto.builder().url(legitUrl).build(),
                HttpStatus.OK
        );
    }
}
