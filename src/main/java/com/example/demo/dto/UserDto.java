package com.example.demo.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String firstname;
    private String lastname;
    private String username;
    private List<String>roles;
}
