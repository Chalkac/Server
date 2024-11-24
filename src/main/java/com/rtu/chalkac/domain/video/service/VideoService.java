package com.rtu.chalkac.domain.video.service;

import com.rtu.chalkac.domain.category.model.Category;
import com.rtu.chalkac.domain.category.service.CategoryService;
import com.rtu.chalkac.domain.users.model.Users;
import com.rtu.chalkac.domain.users.service.UserService;
import com.rtu.chalkac.domain.video.dto.request.*;
import com.rtu.chalkac.domain.video.model.Video;
import com.rtu.chalkac.domain.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final CategoryService categoryService;
    private final UserService usersService;

    // getVideo: 단일 조회 예외 처리용
    public Video getVideo(String videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Video not found with id: " + videoId));
    }

    // 1. 모든 영상 정보 조회 (페이징 처리)
    public Page<Video> findAllVideos(int page, int size) {
        return videoRepository.findAll(PageRequest.of(page, size));
    }

    // 2. 영상 단일 조회
    public Video findVideoById(String videoId) {
        return getVideo(videoId);
    }

    // 3. 업로드 API (create)
    @Transactional
    public Video createVideo(CreateVideoRequestDto requestDto) {
        Category category = categoryService.getCategory(Long.parseLong(requestDto.getCategoryId()));
        Users user = usersService.getUser(requestDto.getUserId());

        Video video = Video.builder()
                .videoId(generateVideoId()) // 별도의 ID 생성 로직 필요
                .category(category)
                .user(user)
                .originalUrl(requestDto.getOriginalUrl())
                .convertUrl(requestDto.getConvertUrl())
                .thumbnailUrl(requestDto.getThumbnailUrl())
                .likeCnt(0) // 초기값 0
                .dislikeCnt(0) // 초기값 0
                .viewCnt(0) // 초기값 0
                .description(requestDto.getDescription())
                .title(requestDto.getTitle())
                .date(LocalDateTime.now())
                .build();

        return videoRepository.save(video);
    }

    // 4. 카테고리 수정
    @Transactional
    public void updateVideoCategory(UpdateVideoCategoryRequestDto requestDto) {
        Video video = getVideo(requestDto.getVideoId());
        Category category = categoryService.getCategory(Long.parseLong(requestDto.getCategoryId()));
        video.setCategory(category);
    }

    // 5. 썸네일 수정
    @Transactional
    public void updateVideoThumbnail(UpdateVideoThumbnailRequestDto requestDto) {
        Video video = getVideo(requestDto.getVideoId());
        video.setThumbnailUrl(requestDto.getThumbnailUrl());
    }

    // 6. 제목 수정
    @Transactional
    public void updateVideoTitle(UpdateVideoTitleRequestDto requestDto) {
        Video video = getVideo(requestDto.getVideoId());
        video.setTitle(requestDto.getTitle());
    }

    // 7. 설명 수정
    @Transactional
    public void updateVideoDescription(UpdateVideoDescriptionRequestDto requestDto) {
        Video video = getVideo(requestDto.getVideoId());
        video.setDescription(requestDto.getDescription());
    }

    // 8. likeCnt / dislikeCnt +1, -1
    @Transactional
    public void updateVideoLikes(String videoId, boolean isLike, boolean isIncrement) {
        Video video = getVideo(videoId);
        if (isLike) {
            video.setLikeCnt(video.getLikeCnt() + (isIncrement ? 1 : -1));
        } else {
            video.setDislikeCnt(video.getDislikeCnt() + (isIncrement ? 1 : -1));
        }
    }

    // 9. viewCnt +1
    @Transactional
    public void incrementViewCount(String videoId) {
        Video video = getVideo(videoId);
        video.setViewCnt(video.getViewCnt() + 1);
    }

    // 10. 영상 삭제
    @Transactional
    public void deleteVideo(String videoId) {
        if (!videoRepository.existsById(videoId)) {
            throw new IllegalArgumentException("Video not found with id: " + videoId);
        }
        videoRepository.deleteById(videoId);
    }

    // Video ID 생성 (예: UUID 사용)
    private String generateVideoId() {
        return java.util.UUID.randomUUID().toString();
    }
}
