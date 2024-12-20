package com.example.demo.rest.student;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.course.CourseDto;
import com.example.demo.dto.group.GroupDto;
import com.example.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/student/course")
public class StudentCourseRestController {
    @Autowired
    private CourseService courseService;
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<Page<CourseDto>>> list(@RequestParam int page, @RequestParam int size) throws Exception {
        return ResponseEntity.ok(courseService.findAll(true , page , size ));
    }
    @GetMapping("/get-course-by-id/{courseId}")
    public ResponseEntity<ResponseDto<CourseDto>> findByName(@PathVariable Long courseId) throws Exception {
        return ResponseEntity.ok(courseService.findById(courseId));
    }
    @GetMapping("/get-course-by-name/{courseName}")
    public ResponseEntity<ResponseDto<CourseDto>> getCourse(@PathVariable String courseName) throws Exception {
        return ResponseEntity.ok(courseService.findByName(courseName));
    }
    @GetMapping("/groups-from-course/{courseId}")
    public ResponseEntity<ResponseDto<Page<GroupDto>>>findAllByCourseId(
            @PathVariable Long courseId, @RequestParam int page, @RequestParam int size
    ) throws Exception {
        return ResponseEntity.ok(courseService.findAllByCourseId(courseId, page, size));
    }
}
