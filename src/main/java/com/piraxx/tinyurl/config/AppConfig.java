package com.piraxx.tinyurl.config;


import com.piraxx.tinyurl.utils.Encoder;
import com.piraxx.tinyurl.utils.SnowflakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator(){
        long workerId = 1L;
        long dataCenterId = 1L;
        return new SnowflakeIdGenerator(workerId, dataCenterId);
    }

    @Bean
    public Encoder encoder(){
        return new Encoder();
    }

}
