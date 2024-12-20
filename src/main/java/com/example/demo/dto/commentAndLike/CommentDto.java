package com.example.demo.dto.commentAndLike;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String text;
    private Long userId ;
    private Long lessonId ;
}
