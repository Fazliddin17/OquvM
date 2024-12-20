package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.dto.form.LoginForm;
import com.example.demo.dto.form.SignUpForm;
import com.example.demo.dto.student.EditProfileDto;
import com.example.demo.dto.teacher.AddTeacherDto;
import com.example.demo.dto.teacher.TeacherDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResponseDto<LoginResponseDto> signin(LoginForm form) throws Exception;
    ResponseDto<LoginResponseDto>signUp(SignUpForm form) throws Exception;
    ResponseDto<UserResponseDto> currentUserInfo() throws Exception;

    ResponseDto<Page<TeacherDto>> getAllTeachers(int page, int size) throws Exception;

    ResponseDto<Void> addTeacher(AddTeacherDto teacherDto) throws Exception;

    ResponseDto<Void> editTeacher(TeacherDto teacherDto, Long teacherId) throws Exception;

    ResponseDto<Void> deleteTeacher(Long teacherId) throws Exception;
    ResponseDto<UserDto>findById(Long userId) throws Exception ;
    ResponseDto<Page<UserDto>>findAllUsers(int page, int size) throws Exception;
    ResponseDto<Void>editProfile(EditProfileDto dto) throws Exception;
    ResponseDto<Void>addProfileImage(MultipartFile file, Long userId) ;
    ResponseDto<Void>deleteProfileImage(Long profileImageId) throws Exception;

}
