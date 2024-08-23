package com.piraxx.tinyurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TinyurlApplication {
	public static void main(String[] args) {
		SpringApplication.run(TinyurlApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer webMvcConfigurer(){
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry corsRegistry){
				corsRegistry
						.addMapping("/**")
						.allowedOrigins("http://localhost:5173", "https://t-2t2q.onrender.com");
			}
		};
	}
}
