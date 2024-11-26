package com.rtu.chalkac.domain.reply.dto.response;

import com.rtu.chalkac.domain.reply.model.Reply;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
public class ReplyResponseDto {
    private String replyId;
    private String commentId;
    private String userId;
    private String content;
    private long likeCnt;
    private long dislikeCnt;
    private LocalDateTime date;

    public ReplyResponseDto(Reply reply){
        this.replyId = reply.getReplyId();
        this.commentId = reply.getComment().getCommentId();
        this.userId = reply.getUser().getUserId();
        this.content = reply.getContent();
        this.likeCnt = reply.getLikeCnt();
        this.dislikeCnt = reply.getDislikeCnt();
        this.date = reply.getDate();
    }
}
