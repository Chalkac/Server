package com.rtu.chalkac.domain.video.service;

import com.rtu.chalkac.domain.video.dto.response.VideoResponseDto;
import com.rtu.chalkac.domain.video.model.Video;
import com.rtu.chalkac.domain.video.repository.VideoRepository;
import com.rtu.chalkac.global.util.PageableDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final VideoRepository videoRepository;

    public PageableDto<VideoResponseDto> searchVideos(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (keyword == null || keyword.trim().isEmpty()) {
            return new PageableDto<>(videoRepository.findAll(pageable).map(VideoResponseDto::new));
        }

        // 검색 키워드를 포함한 비디오 엔티티를 검색
        Page<Video> videos = videoRepository.findByTitleContainingIgnoreCaseOrCategoryNameContainingIgnoreCaseOrUserNicknameContainingIgnoreCase(
                keyword, keyword, keyword, pageable);

        // 검색 결과를 VideoResponseDto로 매핑
        return new PageableDto<>(videos.map(VideoResponseDto::new));
    }
}
