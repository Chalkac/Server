package com.rtu.chalkac.domain.comment.controller;

import com.rtu.chalkac.domain.comment.dto.request.CreateCommentRequestDto;
import com.rtu.chalkac.domain.comment.dto.request.UpdateCommentRequestDto;
import com.rtu.chalkac.domain.comment.dto.response.CommentResponseDto;
import com.rtu.chalkac.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 해당 비디오의 댓글 조회 - likeCnt 높은 순
    @GetMapping("/{videoId}/by-likes")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByLikes(
            @PathVariable String videoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size); // 페이지 정보 생성
        Page<CommentResponseDto> comments = commentService.findCommentsByLikes(videoId, pageable);
        return ResponseEntity.ok(comments);
    }

    // 해당 비디오의 댓글 조회 - date 최신 순
    @GetMapping("/{videoId}/by-date")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByDate(
            @PathVariable String videoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size); // 페이지 정보 생성
        Page<CommentResponseDto> comments = commentService.findCommentsByDate(videoId, pageable);
        return ResponseEntity.ok(comments);
    }

    // id로 댓글 하나 조회
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable String commentId) {
        CommentResponseDto comment = commentService.findCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @RequestBody CreateCommentRequestDto requestDto) {
        CommentResponseDto createdComment = commentService.createComment(requestDto);
        return ResponseEntity.ok(createdComment);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @RequestBody UpdateCommentRequestDto requestDto) {
        CommentResponseDto updatedComment = commentService.updateComment(requestDto);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    // like +1 or -1
    @PatchMapping("/{commentId}/like")
    public ResponseEntity<Void> updateLikeCount(
            @PathVariable String commentId, @RequestParam boolean increment) {
        commentService.updateLikeCount(commentId, increment);
        return ResponseEntity.ok().build();
    }

    // dislike +1 or -1
    @PatchMapping("/{commentId}/dislike")
    public ResponseEntity<Void> updateDislikeCount(
            @PathVariable String commentId, @RequestParam boolean increment) {
        commentService.updateDislikeCount(commentId, increment);
        return ResponseEntity.ok().build();
    }

    // 댓글 reply count 증가
    @PatchMapping("/{commentId}/reply")
    public ResponseEntity<Void> incrementReplyCount(@PathVariable String commentId) {
        commentService.incrementReplyCount(commentId);
        return ResponseEntity.ok().build();
    }
}
