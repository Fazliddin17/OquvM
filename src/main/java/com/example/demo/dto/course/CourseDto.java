package com.example.demo.dto.course;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
public class CourseDto {
    private Long id;
    private String name ;
    private String description;
    private List<Long> groupIds ;
}
