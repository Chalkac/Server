package com.rtu.chalkac.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.mediaconvert.MediaConvertClient;
import software.amazon.awssdk.regions.Region;

@Configuration
public class MediaConvertConfig {

    @Bean
    public MediaConvertClient mediaConvertClient() {
        return MediaConvertClient.builder()
                .region(Region.of("ap-northeast-2"))
                .build();
    }
}
