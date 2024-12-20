package com.example.demo.rest;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin

@RestController
@RequestMapping("/user-info")
//@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
public class AdminTeacherStudentUserInfo {
    @Autowired
    private UserService userService;
    @GetMapping("/get-user-info")
    public ResponseEntity<ResponseDto<UserResponseDto>> getUserInfo() throws Exception {
        return ResponseEntity.ok(userService.currentUserInfo());
    }
}
