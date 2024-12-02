package com.rtu.chalkac.domain.comment.model;

import com.rtu.chalkac.domain.users.model.Users;
import com.rtu.chalkac.domain.video.model.Video;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Comment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @Column(name = "comment_id")
    private String commentId;

    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "like_cnt", nullable = false)
    private long likeCnt;

    @Column(name = "dislike_cnt", nullable = false)
    private long dislikeCnt;

    @Column(name = "reply_cnt", nullable = false)
    private long replyCnt;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;
}
