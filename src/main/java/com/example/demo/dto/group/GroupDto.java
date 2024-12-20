package com.example.demo.dto.group;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GroupDto {
    private Long id;
    private String name;
    private String description;
    private Long courseId;
    private List<Long> userIds;
    private List<Long> lessonIds;
}
