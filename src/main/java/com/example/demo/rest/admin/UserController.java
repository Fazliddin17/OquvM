package com.example.demo.rest.admin;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;@CrossOrigin

@RestController
@RequestMapping("/admin/user-info")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/get-all-users")
    public ResponseEntity<ResponseDto<Page<UserDto>>> getAllUsers(@RequestParam int page, @RequestParam int size) throws Exception {
        return ResponseEntity.ok(userService.findAllUsers(page, size));
    }

    @GetMapping("/get-users/{userId}")
    public ResponseEntity<ResponseDto<UserDto>> findById(@PathVariable Long userId) throws Exception {
        return ResponseEntity.ok(userService.findById(userId));
    }
}
