package com.rtu.chalkac.domain.reply.controller;

import com.rtu.chalkac.domain.reply.dto.request.CreateCommentRequestDto;
import com.rtu.chalkac.domain.reply.dto.response.ReplyResponseDto;
import com.rtu.chalkac.domain.reply.service.ReplyService;
import com.rtu.chalkac.global.util.PageableDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reply")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class ReplyController {
    private final ReplyService replyService;

    // 해당 댓글의 답글 조회 - 최신순
    @GetMapping("/{commentId}/by-date")
    public ResponseEntity<PageableDto<ReplyResponseDto>> findRepliesByComment(
            @PathVariable String commentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageableDto<ReplyResponseDto> replies = replyService.findRepliesByComment(commentId, page, size);
        return ResponseEntity.ok(replies);
    }

    // 답글 하나 조회
    @GetMapping("/{replyId}")
    public ResponseEntity<ReplyResponseDto> findReplyById(@PathVariable String replyId) {
        ReplyResponseDto reply = replyService.findReplyById(replyId);
        return ResponseEntity.ok(reply);
    }

    // 답글 추가
    @PostMapping
    public ResponseEntity<ReplyResponseDto> createReply(@RequestBody CreateCommentRequestDto requestDto) {
        ReplyResponseDto createdReply = replyService.createReply(requestDto);
        return ResponseEntity.ok(createdReply);
    }

    // 답글 삭제
    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable String replyId) {
        replyService.deleteReply(replyId);
        return ResponseEntity.noContent().build();
    }

    // 답글 likeCnt +1 or -1
    @PatchMapping("/{replyId}/like")
    public ResponseEntity<Void> updateLikeCount(
            @PathVariable String replyId,
            @RequestParam boolean increment) {
        replyService.updateLikeCount(replyId, increment);
        return ResponseEntity.ok().build();
    }

    // 답글 dislikeCnt +1 or -1
    @PatchMapping("/{replyId}/dislike")
    public ResponseEntity<Void> updateDislikeCount(
            @PathVariable String replyId,
            @RequestParam boolean increment) {
        replyService.updateDislikeCount(replyId, increment);
        return ResponseEntity.ok().build();
    }
}
