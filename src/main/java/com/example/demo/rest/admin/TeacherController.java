package com.example.demo.rest.admin;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.teacher.AddTeacherDto;
import com.example.demo.dto.teacher.TeacherDto;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/admin/teacher")
public class TeacherController {
    @Autowired
    private UserService userService;

    @GetMapping("/get-all-teachers")
    public ResponseEntity<ResponseDto<Page<TeacherDto>>> getAllTeacher(
            @RequestParam int page,
            @RequestParam int size
    ) throws Exception {
        return ResponseEntity.ok(userService.getAllTeachers(page, size));
    }

    @PostMapping("/add-teacher")
    public ResponseEntity<ResponseDto<Void>> addTeacher(@RequestBody AddTeacherDto dto) throws Exception {
        return ResponseEntity.ok(userService.addTeacher(dto));
    }

    @PutMapping("/edit-teacher")
    public ResponseEntity<ResponseDto<Void>> editTeacher(
            @RequestBody TeacherDto dto, @RequestParam Long teacherId
    ) throws Exception {
        return ResponseEntity.ok(userService.editTeacher(dto, teacherId));
    }
    @DeleteMapping("/delete-teacher")
    public ResponseEntity<ResponseDto<Void>>deleteTeacher(@RequestParam Long teacherId) throws Exception {
        return ResponseEntity.ok(userService.deleteTeacher(teacherId));
    }
}
