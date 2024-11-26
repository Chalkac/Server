package com.rtu.chalkac.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI ChalkacOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Chalkac API")
                        .description("Chalkac API 명세서"));
    }

    @Bean
    public GroupedOpenApi categoryGroup(){
        return GroupedOpenApi.builder()
                .group("Category")
                .pathsToMatch("/api/v1/category/**")
                .build();
    }

    @Bean
    public GroupedOpenApi UsersGroup(){
        return GroupedOpenApi.builder()
                .group("User")
                .pathsToMatch("/api/v1/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi VideoGroup(){
        return GroupedOpenApi.builder()
                .group("Video")
                .pathsToMatch("/api/v1/video/**")
                .build();
    }

    @Bean
    public GroupedOpenApi CommentGroup(){
        return GroupedOpenApi.builder()
                .group("Comment")
                .pathsToMatch("/api/v1/comment/**")
                .build();
    }

    @Bean
    public GroupedOpenApi ReplyGroup(){
        return GroupedOpenApi.builder()
                .group("Reply")
                .pathsToMatch("/api/v1/reply/**")
                .build();
    }
}
