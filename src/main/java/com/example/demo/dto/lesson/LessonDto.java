package com.example.demo.dto.lesson;

import com.example.demo.dto.commentAndLike.CommentDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonDto {
    private Long id;
    private String name;
    private String description;
    private String videoUrl;
    private Long groupId;
    private int likeCount ;
    private Boolean successLike ;
    private List<CommentDto> comments;
}
