package com.rtu.chalkac.domain.reply.model;

import com.rtu.chalkac.domain.comment.model.Comment;
import com.rtu.chalkac.domain.users.model.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reply")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reply {
    @Id
    @Column(name = "reply_id")
    private String replyId;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "like_cnt", nullable = false)
    private long likeCnt;

    @Column(name = "dislike_cnt", nullable = false)
    private long dislikeCnt;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;
}
