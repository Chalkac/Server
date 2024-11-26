package com.rtu.chalkac.domain.video.dto.response;

import com.rtu.chalkac.domain.video.model.Video;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class VideoResponseDto {
    private final String videoId;
    private final Long categoryId;
    private final String categoryName;
    private final String userId;
    private final String userName;
    private final String originalUrl;
    private final String convertUrl;
    private final String thumbnailUrl;
    private final long likeCnt;
    private final long dislikeCnt;
    private final long viewCnt;
    private final String date;
    private final String description;
    private final String title;

    public VideoResponseDto(Video video) {
        this.videoId = video.getVideoId();
        this.categoryId = video.getCategory().getCategoryId();
        this.categoryName = video.getCategory().getName();
        this.userId = video.getUser().getUserId();
        this.userName = video.getUser().getNickname();
        this.originalUrl = video.getOriginalUrl();
        this.convertUrl = video.getConvertUrl();
        this.thumbnailUrl = video.getThumbnailUrl();
        this.likeCnt = video.getLikeCnt();
        this.dislikeCnt = video.getDislikeCnt();
        this.viewCnt = video.getViewCnt();
        this.date = video.getDate().toString();
        this.description = video.getDescription();
        this.title = video.getTitle();
    }
}
