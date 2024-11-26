package com.rtu.chalkac.domain.comment.repository;

import com.rtu.chalkac.domain.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, String> {
    @Query("SELECT c FROM Comment c WHERE c.video.videoId = :videoId ORDER BY (c.likeCnt - c.dislikeCnt) DESC")
    Page<Comment> findByVideoIdOrderByLikeDislikeSumDesc(@Param("videoId") String videoId, Pageable pageable);

    Page<Comment> findByVideoVideoIdOrderByDateDesc(String videoId, Pageable pageable);
}
