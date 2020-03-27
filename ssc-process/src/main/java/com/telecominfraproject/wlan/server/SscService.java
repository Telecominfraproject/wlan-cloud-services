package com.telecominfraproject.wlan.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * SscService Spring Application start class
 * 
 */
@ComponentScan(basePackages = { "com.telecominfraproject.wlan" })
@EnableAutoConfiguration
public class SscService {
    public static void main(String[] args) {
        SpringApplication.run(SscService.class, args);
    }
}
