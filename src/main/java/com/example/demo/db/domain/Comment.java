package com.example.demo.db.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "comments")
@Entity
@Getter
@Setter
public class Comment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String text;
    private Long userId;
    private Long lessonId;
    private Boolean active;
}
