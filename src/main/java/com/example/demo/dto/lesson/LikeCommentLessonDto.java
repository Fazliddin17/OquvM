package com.example.demo.dto.lesson;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeCommentLessonDto {
    private Long lessonId;
    private String comment;
}
