package com.example.demo.dto.student;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
}
