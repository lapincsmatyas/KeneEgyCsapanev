package com.example.cb.security;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class ApplicationConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Can just allow `methods` that you need.
        registry.addMapping("/**").allowedMethods("PUT", "GET", "DELETE", "OPTIONS", "PATCH", "POST");
    }
}