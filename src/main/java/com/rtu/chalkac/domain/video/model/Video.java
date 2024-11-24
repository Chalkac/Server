package com.rtu.chalkac.domain.video.model;

import com.rtu.chalkac.domain.category.model.Category;
import com.rtu.chalkac.domain.users.model.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Video")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Video {
    @Id
    @Column(name = "video_id")
    private String videoId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "original_url")
    private String originalUrl;

    @Column(name = "convert_url")
    private String convertUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "like_cnt")
    private long likeCnt;

    @Column(name = "dislike_cnt")
    private long dislikeCnt;

    @Column(name = "view_cnt")
    private long viewCnt;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "description")
    private String description;

    @Column(name = "title")
    private String title;
}
