package com.example.demo.dto.teacher;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class TeacherDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private List<Long> groupIds;
}
