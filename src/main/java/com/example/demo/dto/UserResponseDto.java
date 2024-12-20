package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
   private Long pkey ;
   private String firstname;
   private String lastname;
   private String username;
   private List<String>roles ;
   private List<ProfileDto>profileImages ;
}