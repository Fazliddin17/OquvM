package com.example.demo.db.repository;

import com.example.demo.db.domain.Course;
import com.example.demo.db.domain.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findAllByActiveOrderById(Boolean active, Pageable pageable);

    Optional<Course> findByName(String name);


    @Query("""
            SELECT g FROM Group g 
            WHERE g.course.id = :courseId AND g.active = true""")
    List<Group> findActiveGroupsByCourseId(@Param("courseId") Long courseId);
}
