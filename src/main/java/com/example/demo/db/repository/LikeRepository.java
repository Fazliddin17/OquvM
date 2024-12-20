package com.example.demo.db.repository;

import com.example.demo.db.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findAllByLessonIdAndActiveTrueOrderById(Long lessonId);
    Optional<Like> findByLessonIdAndUserIdAndActiveTrue(Long lessonId, Long userId);

}
