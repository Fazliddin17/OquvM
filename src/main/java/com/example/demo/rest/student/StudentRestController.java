package com.example.demo.rest.student;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.group.GroupDto;
import com.example.demo.dto.student.EditProfileDto;
import com.example.demo.service.GroupService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/student")
public class StudentRestController {
    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @GetMapping("/my-groups")
    public ResponseEntity<ResponseDto<Page<GroupDto>>> getAllGroups(
            @RequestParam Long studentId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(groupService.teacherGroups(studentId, page, size));
    }

    @PostMapping("/edit-profile")
    public ResponseEntity<ResponseDto<Void>> editProfile(@RequestBody EditProfileDto dto) throws Exception {
        return ResponseEntity.ok(userService.editProfile(dto));
    }
    @PostMapping("/add-profile-image")
    public ResponseEntity<ResponseDto<Void>> addProfile(@RequestParam MultipartFile file, @RequestParam Long userId) throws Exception {
        return ResponseEntity.ok(userService.addProfileImage(file, userId));
    }
    @DeleteMapping("/add-profile-image/{profileImageId}")
    public ResponseEntity<ResponseDto<Void>> deleteProfileImage(@PathVariable Long profileImageId) throws Exception {
        return ResponseEntity.ok(userService.deleteProfileImage(profileImageId));
    }
}
