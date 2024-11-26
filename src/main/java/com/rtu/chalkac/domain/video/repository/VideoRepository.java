package com.rtu.chalkac.domain.video.repository;

import com.rtu.chalkac.domain.video.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, String> {
    Page<Video> findByTitleContainingIgnoreCaseOrCategoryNameContainingIgnoreCaseOrUserNicknameContainingIgnoreCase(
            String titleKeyword,
            String categoryKeyword,
            String userKeyword,
            Pageable pageable
    );
}
