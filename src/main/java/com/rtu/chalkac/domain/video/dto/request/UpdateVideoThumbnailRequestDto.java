package com.rtu.chalkac.domain.video.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVideoThumbnailRequestDto {
    private String videoId;
    private String thumbnailUrl;
}
