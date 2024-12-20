package com.example.demo.rest.admin;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.course.AddCourseDto;
import com.example.demo.dto.course.CourseDto;
import com.example.demo.dto.group.GroupDto;
import com.example.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;;

import java.util.List;

@RestController
@RequestMapping("/admin/course")
@CrossOrigin
public class CourseController {
    @Autowired
    private CourseService courseService;
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<Page<CourseDto>>> getAllCourses(
            @RequestParam Boolean active,
            @RequestParam int page,
            @RequestParam int size
    ) throws Exception {
        return ResponseEntity.ok(courseService.findAll(active, page, size));
    }
    @PostMapping("/add-course")
    public ResponseEntity<ResponseDto<Void>> addCourse(@RequestBody AddCourseDto courseDto) {
        return ResponseEntity.ok(courseService.addCourse(courseDto));
    }
    @PutMapping("/edit-course/{courseId}")
    public ResponseEntity<ResponseDto<Void>> editCourse(@RequestBody CourseDto courseDto, @PathVariable Long courseId) {
        courseDto.setId(courseId);
        return ResponseEntity.ok(courseService.edit(courseDto));
    }
    @DeleteMapping("/delete-course/{courseId}")
    public ResponseEntity<ResponseDto<Void>> deleteCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.deleteById(courseId));
    }
    @GetMapping("/get-course-name")
    public ResponseEntity<ResponseDto<CourseDto>> findByCourseName(@RequestParam String courseName) {
        return ResponseEntity.ok(courseService.findByName(courseName));
    }
    @GetMapping("/get-course-id")
    public ResponseEntity<ResponseDto<CourseDto>> findById(@RequestParam Long courseId) {
        return ResponseEntity.ok(courseService.findById(courseId));
    }

    @GetMapping("/get-all-by-course-id/{courseId}")
    public ResponseEntity<ResponseDto<Page<GroupDto>>> findAllByCourseId(@PathVariable Long courseId, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(courseService.findAllByCourseId(courseId, page, size));
    }
}
