package com.rtu.chalkac.domain.reply.service;

import com.rtu.chalkac.domain.comment.model.Comment;
import com.rtu.chalkac.domain.comment.service.CommentService;
import com.rtu.chalkac.domain.reply.dto.request.CreateCommentRequestDto;
import com.rtu.chalkac.domain.reply.dto.response.ReplyResponseDto;
import com.rtu.chalkac.domain.reply.model.Reply;
import com.rtu.chalkac.domain.reply.repository.ReplyRepository;
import com.rtu.chalkac.domain.users.model.Users;
import com.rtu.chalkac.domain.users.service.UserService;
import com.rtu.chalkac.global.util.PageableDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final CommentService commentService;
    private final UserService userService;

    public Reply getReply(String replyId){
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("Reply not found with id"));
    }

    // 해당 댓글의 답글 조회, Page, ReplyResponseDto 사용, date 최신순
    public PageableDto<ReplyResponseDto> findRepliesByComment(String commentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new PageableDto<>(replyRepository.findByCommentCommentIdOrderByDateAsc(commentId, pageable)
                .map(ReplyResponseDto::new));
    }

    // 답글 하나 조회, ReplyResponseDto 사용
    public ReplyResponseDto findReplyById(String replyId) {
        Reply reply = getReply(replyId);
        return new ReplyResponseDto(reply);
    }

    // 답글 추가, CommentId에 해당하는 Comment의 replyCnt도 +1, CreateCommentRequestDto 사용
    @Transactional
    public ReplyResponseDto createReply(CreateCommentRequestDto requestDto) {
        Comment comment = commentService.getComment(requestDto.getCommentId());
        Users user = userService.getUser(requestDto.getUserId());

        Reply reply = Reply.builder()
                .replyId(UUID.randomUUID().toString())
                .comment(comment)
                .user(user)
                .content(requestDto.getContent())
                .likeCnt(0)
                .dislikeCnt(0)
                .date(LocalDateTime.now())
                .build();

        // Reply 저장
        Reply savedReply = replyRepository.save(reply);

        // Comment의 replyCnt 증가
        commentService.incrementReplyCount(comment.getCommentId());

        return new ReplyResponseDto(savedReply);
    }

    // 답글 삭제, CommentId에 해당하는 Comment의 replyCnt도 -1
    @Transactional
    public void deleteReply(String replyId) {
        Reply reply = getReply(replyId);

        replyRepository.delete(reply);

        // Comment의 replyCnt 감소
        commentService.decrementReplyCount(reply.getComment().getCommentId());
    }

    // 답글 likeCnt +1 or -1
    @Transactional
    public void updateLikeCount(String replyId, boolean increment) {
        Reply reply = getReply(replyId);

        if (increment) {
            reply.setLikeCnt(reply.getLikeCnt() + 1);
        } else {
            if(reply.getLikeCnt() == 0){
                throw new IllegalArgumentException("Like cnt are both 0");
            }
            reply.setLikeCnt(Math.max(0, reply.getLikeCnt() - 1)); // 최소값 0
        }

        replyRepository.save(reply);
    }

    // 답글 dislikeCnt +1 or -1
    @Transactional
    public void updateDislikeCount(String replyId, boolean increment) {
        Reply reply = getReply(replyId);

        if (increment) {
            reply.setDislikeCnt(reply.getDislikeCnt() + 1);
        } else {
            if(reply.getDislikeCnt() == 0){
                throw new IllegalArgumentException("Dislike cnt are both 0");
            }
            reply.setDislikeCnt(Math.max(0, reply.getDislikeCnt() - 1)); // 최소값 0
        }

        replyRepository.save(reply);
    }
}
