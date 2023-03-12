package com.javadevs.testingservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hf")
@Getter
@Setter
public class HFProperties {
    private String token;
}
