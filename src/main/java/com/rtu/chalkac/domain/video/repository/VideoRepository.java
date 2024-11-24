package com.rtu.chalkac.domain.video.repository;

import com.rtu.chalkac.domain.video.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, String> {
}
