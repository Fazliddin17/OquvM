package com.example.demo.service;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.commentAndLike.AddCommentDto;
import com.example.demo.dto.commentAndLike.CommentDto;
import com.example.demo.dto.lesson.AddLessonDto;
import com.example.demo.dto.lesson.LessonDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LessonService {
    ResponseDto<Page<LessonDto>> findAll(int page, int size) ;
    ResponseDto<Page<LessonDto>>findAllByGroupId(Long groupId, int page, int size) ;
    ResponseDto<Void>addLesson(AddLessonDto lessonDto, MultipartFile file) ;
    ResponseDto<Void>editLesson(Long lessonId, AddLessonDto dto) ;
    ResponseDto<Void>editLessonVideo(Long lessonId, MultipartFile file) ;
    ResponseDto<Void>deleteLesson(Long lessonId) ;
    ResponseDto<LessonDto>findById(Long lessonId, Long userId) ;

    ResponseDto<Void> setLike(Boolean like, Long lessonId, Long userId);
    ResponseDto<List<CommentDto>>getCommentsFromLesson(Long lessonId) ;
    ResponseDto<Void>setComment(AddCommentDto dto);
    ResponseDto<Void>removeComment(Long commentId);

}
