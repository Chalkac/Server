package com.rtu.chalkac.domain.comment.repository;

import com.rtu.chalkac.domain.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
    Page<Comment> findByVideoVideoIdOrderByLikeCntDesc(String videoId, Pageable pageable);

    Page<Comment> findByVideoVideoIdOrderByDateDesc(String videoId, Pageable pageable);
}
