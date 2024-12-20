package com.example.demo.dto.commentAndLike;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCommentDto {
    private Long userId;
    private Long lessonId;
    private String text;
}
