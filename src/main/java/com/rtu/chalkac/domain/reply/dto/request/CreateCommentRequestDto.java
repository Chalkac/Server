package com.rtu.chalkac.domain.reply.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequestDto {
    private String commentId;
    private String userId;
    private String content;
}
