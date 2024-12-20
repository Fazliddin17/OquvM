package com.example.demo.dto.group;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddGroupDto {
    private String name;
    private String description;
    private Long teacherId;
    private Long courseId;
}
