package com.rtu.chalkac.domain.video.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConvertRequestDto {
    private String inputS3Url;
    private String outputS3Url;
}
