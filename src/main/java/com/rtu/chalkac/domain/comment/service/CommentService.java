package com.rtu.chalkac.domain.comment.service;

import com.rtu.chalkac.domain.comment.dto.request.CreateCommentRequestDto;
import com.rtu.chalkac.domain.comment.dto.request.UpdateCommentRequestDto;
import com.rtu.chalkac.domain.comment.dto.response.CommentResponseDto;
import com.rtu.chalkac.domain.comment.model.Comment;
import com.rtu.chalkac.domain.comment.repository.CommentRepository;
import com.rtu.chalkac.domain.users.model.Users;
import com.rtu.chalkac.domain.users.service.UserService;
import com.rtu.chalkac.domain.video.model.Video;
import com.rtu.chalkac.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final VideoService videoService;
    private final UserService userService;

    public Comment getComment(String commentId){
        return commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Comment not found with id: "));
    }

    // 해당 비디오의 댓글 조회, Page, CommentResponseDto 사용, likeCnt 높은순
    public Page<CommentResponseDto> findCommentsByLikes(String videoId, Pageable pageable) {
        return commentRepository.findByVideoVideoIdOrderByLikeCntDesc(videoId, pageable)
                .map(CommentResponseDto::new);
    }

    // 해당 비디오의 댓글 조회, Page, CommentResponseDto 사용, date 최신순
    public Page<CommentResponseDto> findCommentsByDate(String videoId, Pageable pageable) {
        return commentRepository.findByVideoVideoIdOrderByDateDesc(videoId, pageable)
                .map(CommentResponseDto::new);
    }

    // id로 댓글 하나 조회, CommentResponseDto 사용
    public CommentResponseDto findCommentById(String commentId) {
        return new CommentResponseDto(getComment(commentId));
    }

    // 댓글 생성, CreateCommentRequestDto
    @Transactional
    public CommentResponseDto createComment(CreateCommentRequestDto requestDto) {
        Video video = videoService.getVideo(requestDto.getVideoId());
        Users user = userService.getUser(requestDto.getUserId());

        Comment comment = Comment.builder()
                .commentId(UUID.randomUUID().toString())
                .video(video)
                .user(user)
                .content(requestDto.getContent())
                .likeCnt(0)
                .dislikeCnt(0)
                .replyCnt(0)
                .date(LocalDateTime.now())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDto(savedComment);
    }

    // 댓글 수정, UpdateCommentRequestDto
    @Transactional
    public CommentResponseDto updateComment(UpdateCommentRequestDto requestDto) {
        Comment comment = getComment(requestDto.getCommentId());
        comment.setContent(requestDto.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return new CommentResponseDto(updatedComment);
    }

    // 댓글 삭제, id로 삭제
    @Transactional
    public void deleteComment(String commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("Comment not found with id: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }

    // like +1 or -1
    @Transactional
    public void updateLikeCount(String commentId, boolean increment) {
        Comment comment = getComment(commentId);

        if (increment) {
            comment.setLikeCnt(comment.getLikeCnt() + 1);
        } else {
            comment.setLikeCnt(Math.max(0, comment.getLikeCnt() - 1)); // 최소값 0
        }

        commentRepository.save(comment);
    }

    // dislike +1 or -1
    @Transactional
    public void updateDislikeCount(String commentId, boolean increment) {
        Comment comment = getComment(commentId);

        if (increment) {
            comment.setDislikeCnt(comment.getDislikeCnt() + 1);
        } else {
            comment.setDislikeCnt(Math.max(0, comment.getDislikeCnt() - 1)); // 최소값 0
        }

        commentRepository.save(comment);
    }

    // reply
    @Transactional
    public void incrementReplyCount(String commentId) {
        Comment comment = getComment(commentId);
        comment.setReplyCnt(comment.getReplyCnt() + 1);
        commentRepository.save(comment);
    }

    @Transactional
    public void decrementReplyCount(String commentId) {
        Comment comment = getComment(commentId);
        comment.setReplyCnt(comment.getReplyCnt() - 1);
        commentRepository.save(comment);
    }
}
