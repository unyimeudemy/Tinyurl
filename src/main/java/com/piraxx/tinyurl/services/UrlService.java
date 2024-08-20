package com.piraxx.tinyurl.services;

import com.piraxx.tinyurl.models.LegitUrls;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UrlService {
    String getUrlKey(String url);

    String generateShort(String url);

    String addSpamUrl(String url);


    String findByKey(String urlKey);
}
