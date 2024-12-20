package com.example.demo.db.repository;

import com.example.demo.db.domain.Group;
import com.example.demo.db.domain.Lesson;
import com.example.demo.db.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Page<Group>findAllByActiveOrderById(Boolean active, Pageable pageable);
    Page<Group>findAllByCourseIdAndActiveOrderById(Long courseId, Boolean active, Pageable pageable);
    @Query("SELECT g.users FROM Group g WHERE g.id = :groupId AND g.active = true")
    List<User> findActiveUsersByGroupId(@Param("groupId") Long groupId);

    @Query("""
            SELECT l FROM Group g
            JOIN g.lessons l
            WHERE g.id = :groupId AND l.active = true""")
    List<Lesson> findActiveLessonsByGroupId(@Param("groupId") Long groupId);
    @Query("""
        SELECT g FROM Group g
        JOIN g.users u
        WHERE u.id = :userId AND g.active=true
        """)
    Page<Group> getAllGroupsFromUser(@Param("userId") Long userId, Pageable pageable);

}
