package com.example.demo.service;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.course.AddCourseDto;
import com.example.demo.dto.course.CourseDto;
import com.example.demo.dto.group.GroupDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {
    ResponseDto<Page<CourseDto>> findAll(Boolean active, int page, int size) throws Exception;
    ResponseDto<Void> addCourse(AddCourseDto courseDto);
    ResponseDto<Void> edit(CourseDto courseDto);
    ResponseDto<Void>deleteById(Long id);
    ResponseDto<CourseDto>findById(Long courseId) ;
    ResponseDto<CourseDto>findByName(String name) ;
    ResponseDto<Page<GroupDto>>findAllByCourseId(Long courseId, int page, int size) ;

}
