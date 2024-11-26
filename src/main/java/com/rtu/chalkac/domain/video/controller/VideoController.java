package com.rtu.chalkac.domain.video.controller;

import com.rtu.chalkac.domain.video.dto.request.*;
import com.rtu.chalkac.domain.video.dto.response.ConvertResponseDto;
import com.rtu.chalkac.domain.video.dto.response.VideoResponseDto;
import com.rtu.chalkac.domain.video.service.ConvertService;
import com.rtu.chalkac.domain.video.service.SearchService;
import com.rtu.chalkac.domain.video.service.VideoService;
import com.rtu.chalkac.global.util.PageableDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final ConvertService convertService;
    private final SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<PageableDto<VideoResponseDto>> searchVideos(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 검색 서비스 호출
        PageableDto<VideoResponseDto> searchResults = searchService.searchVideos(keyword, page, size);

        // 결과 반환
        return ResponseEntity.ok(searchResults);
    }

    @PostMapping("/convert")
    public ResponseEntity<ConvertResponseDto> convertVideo(@RequestBody ConvertRequestDto dto) {
        return ResponseEntity.ok(convertService.startMediaConvertJob(dto.getInputS3Url(), dto.getOutputS3Url()));
    }

    @PostMapping("/status")
    public ResponseEntity<String> getStatus(@RequestBody ConvertStatusRequestDto dto) {
        return ResponseEntity.ok(convertService.getConvertStatus(dto.getJobId()));
    }

    @PutMapping("/convertsave")
    public ResponseEntity<String> saveVideo(@RequestBody ConvertSaveRequestDto dto) {
        convertService.saveConvertUrl(dto);
        return ResponseEntity.ok("convert url saved.");
    }

    @PostMapping("/findS3files")
    public ResponseEntity<List<String>> findS3Files(@RequestBody SelectS3FilesRequestDto dto){
        return ResponseEntity.ok(videoService.findS3Files(dto.getPath()));
    }

    // 1. 모든 영상 정보 조회 (페이징 처리)
    @GetMapping
    public ResponseEntity<PageableDto<VideoResponseDto>> getAllVideos(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(videoService.findAllVideos(page, size));
    }

    // 2. 영상 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<VideoResponseDto> getVideoById(@PathVariable String id) {
        return ResponseEntity.ok(videoService.findVideoById(id));
    }

    // 3. 업로드 API
    @PostMapping
    public ResponseEntity<VideoResponseDto> createVideo(@RequestBody CreateVideoRequestDto requestDto) {
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
