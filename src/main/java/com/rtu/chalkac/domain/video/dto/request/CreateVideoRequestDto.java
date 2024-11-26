package com.rtu.chalkac.domain.video.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateVideoRequestDto {
    private String categoryId;
    private String userId;
    private String originalUrl;
    private String convertUrl;
    private String thumbnailUrl;
    private long likeCnt;
    private long dislikeCnt;
    private long viewCnt;
    private String description;
    private String title;
}
