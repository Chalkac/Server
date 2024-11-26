package com.rtu.chalkac.domain.comment.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCommentRequestDto {
    private String commentId;
    private String content;
}
