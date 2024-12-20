package com.example.demo.rest.teacher;
import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.course.CourseDto;
import com.example.demo.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@RestController
@RequestMapping("/teacher/course")
public class TeacherCourseRestController {
    private final CourseService courseService;
    public TeacherCourseRestController(CourseService courseService) {
        this.courseService = courseService;
    }
    @GetMapping("/list")
    public ResponseDto<Page<CourseDto>>getAllCourses(@RequestParam int page , @RequestParam int size) throws Exception {
        return courseService.findAll(true , page ,size);
    }
    @GetMapping("/get-by-id/{courseId}")
    public ResponseDto<CourseDto>getCourseById(@PathVariable Long courseId) {
        return courseService.findById(courseId);
    }
    @GetMapping("/get-by-name/{course_name}")
    public ResponseDto<CourseDto>getCourseByName(@PathVariable String course_name) {
        return courseService.findByName(course_name);
    }
}