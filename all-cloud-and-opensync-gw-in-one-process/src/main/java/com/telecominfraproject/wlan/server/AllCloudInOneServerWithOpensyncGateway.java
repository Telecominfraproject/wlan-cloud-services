package com.telecominfraproject.wlan.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * AllCloudInOneServerWithOpensyncGateway Spring Application start class
 * 
 */
@ComponentScan(basePackages = { "com.telecominfraproject.wlan" })
@EnableAutoConfiguration
public class AllCloudInOneServerWithOpensyncGateway {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(AllCloudInOneServerWithOpensyncGateway.class, args);
        // signal start of the application context
        applicationContext.start();
    }
}
