package com.piraxx.tinyurl.services.ServicesImpl;

import com.piraxx.tinyurl.services.UrlService;
import com.piraxx.tinyurl.utils.Base58Map;
import com.piraxx.tinyurl.utils.Encoder;
import com.piraxx.tinyurl.utils.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UrlServiceImpl implements UrlService {

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Autowired
    private Encoder encoder;


    @Override
    public String getUrlKey(String url) {
        long id = snowflakeIdGenerator.nextId();
        return encoder.mapToBase58Value(id);
    }
}
