package com.example.demo.dto.lesson;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddLessonDto {
    private String name ;
    private String description;
    private Long groupId;
}
