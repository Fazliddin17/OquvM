package com.example.demo.rest;

import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.form.LoginForm;
import com.example.demo.dto.form.SignUpForm;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/auth")
@CrossOrigin
public class AuthRestController {
    @Autowired
    private UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseDto<LoginResponseDto>> signIn(@RequestBody LoginForm form) throws Exception {
        return ResponseEntity.ok(userService.signin(form));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto<LoginResponseDto>> signUp(@RequestBody SignUpForm signUpForm) throws Exception {
        return ResponseEntity.ok(userService.signUp(signUpForm));
    }
}