package com.example.demo.db.repository;

import com.example.demo.db.domain.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByOrderById();
    Page<Lesson> findAllByActiveOrderById(Boolean active, Pageable pageable);
    Page<Lesson>findAllByGroupIdAndActiveOrderById(Long groupId, Boolean active, Pageable pageable);
}
