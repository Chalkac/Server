package com.rtu.chalkac.domain.reply.repository;

import com.rtu.chalkac.domain.reply.model.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, String> {
    Page<Reply> findByCommentCommentIdOrderByDateDesc(String commentId, Pageable pageable);
}
