package com.piraxx.tinyurl.services;

import org.springframework.stereotype.Service;

@Service
public interface UrlService {
    String getUrlKey(String url);
}
