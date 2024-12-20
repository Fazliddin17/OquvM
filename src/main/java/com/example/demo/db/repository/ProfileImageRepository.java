package com.example.demo.db.repository;

import com.example.demo.db.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
    List<ProfileImage> findAllByUserIdAndActiveOrderById(Long userId, Boolean active);
    
}
