package com.rtu.chalkac.domain.comment.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequestDto {
    private String userId;
    private String videoId;
    private String content;
}
