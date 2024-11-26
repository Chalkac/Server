package com.rtu.chalkac.domain.comment.dto.response;

import com.rtu.chalkac.domain.comment.model.Comment;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
public class CommentResponseDto {
    private String commentId;
    private String userId;
    private String videoId;
    private String content;
    private long likeCnt;
    private long dislikeCnt;
    private long replyCnt;
    private LocalDateTime date;

    public CommentResponseDto(Comment comment){
        this.commentId = comment.getCommentId();
        this.userId = comment.getUser().getUserId();
        this.videoId = comment.getVideo().getVideoId();
        this.content = comment.getContent();
        this.likeCnt = comment.getLikeCnt();
        this.dislikeCnt = comment.getDislikeCnt();
        this.replyCnt = comment.getReplyCnt();
        this.date = comment.getDate();
    }
}
