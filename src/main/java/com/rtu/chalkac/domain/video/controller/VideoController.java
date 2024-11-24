package com.rtu.chalkac.domain.video.controller;

import com.rtu.chalkac.domain.video.dto.request.*;
import com.rtu.chalkac.domain.video.model.Video;
import com.rtu.chalkac.domain.video.service.ConvertService;
import com.rtu.chalkac.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final ConvertService convertService;

    @PostMapping("/convert")
    public ResponseEntity<String> convertVideo(@RequestBody ConvertRequestDto dto) {
        String jobId = convertService.startMediaConvertJob(dto.getInputS3Url(), dto.getOutputS3Url());
        return ResponseEntity.ok("MediaConvert Job Started with ID: " + jobId);
    }

    // 1. 모든 영상 정보 조회 (페이징 처리)
    @GetMapping
    public ResponseEntity<Page<Video>> getAllVideos(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(videoService.findAllVideos(page, size));
    }

    // 2. 영상 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable String id) {
        return ResponseEntity.ok(videoService.findVideoById(id));
    }

    // 3. 업로드 API
    @PostMapping
    public ResponseEntity<Video> createVideo(@RequestBody CreateVideoRequestDto requestDto) {
        return ResponseEntity.ok(videoService.createVideo(requestDto));
    }

    // 4. 카테고리 수정
    @PutMapping("/category")
    public ResponseEntity<Void> updateVideoCategory(@RequestBody UpdateVideoCategoryRequestDto requestDto) {
        videoService.updateVideoCategory(requestDto);
        return ResponseEntity.noContent().build();
    }

    // 5. 썸네일 수정
    @PutMapping("/thumbnail")
    public ResponseEntity<Void> updateVideoThumbnail(@RequestBody UpdateVideoThumbnailRequestDto requestDto) {
        videoService.updateVideoThumbnail(requestDto);
        return ResponseEntity.noContent().build();
    }

    // 6. 제목 수정
    @PutMapping("/title")
    public ResponseEntity<Void> updateVideoTitle(@RequestBody UpdateVideoTitleRequestDto requestDto) {
        videoService.updateVideoTitle(requestDto);
        return ResponseEntity.noContent().build();
    }

    // 7. 설명 수정
    @PutMapping("/description")
    public ResponseEntity<Void> updateVideoDescription(@RequestBody UpdateVideoDescriptionRequestDto requestDto) {
        videoService.updateVideoDescription(requestDto);
        return ResponseEntity.noContent().build();
    }

    // 8. likeCnt / dislikeCnt +1, -1
    @PutMapping("/{id}/like")
    public ResponseEntity<Void> updateVideoLikes(@PathVariable String id,
                                                 @RequestParam boolean isLike,
                                                 @RequestParam boolean isIncrement) {
        videoService.updateVideoLikes(id, isLike, isIncrement);
        return ResponseEntity.noContent().build();
    }

    // 9. viewCnt +1
    @PutMapping("/{id}/view")
    public ResponseEntity<Void> incrementViewCount(@PathVariable String id) {
        videoService.incrementViewCount(id);
        return ResponseEntity.noContent().build();
    }

    // 10. 영상 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable String id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }
}
