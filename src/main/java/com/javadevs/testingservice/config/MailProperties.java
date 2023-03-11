package com.javadevs.testingservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "email.exam")
@Getter
@Setter
public class MailProperties {
    private String titleReady;
    private String titleStart;
    private String testResult;
    private String adminMail;
    private String from;
}
