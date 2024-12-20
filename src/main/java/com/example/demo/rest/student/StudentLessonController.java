package com.example.demo.rest.student;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.commentAndLike.AddCommentDto;
import com.example.demo.dto.commentAndLike.CommentDto;
import com.example.demo.dto.lesson.LessonDto;
import com.example.demo.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/student/lesson/")
public class StudentLessonController {
    @Autowired
    private LessonService lessonService;
    @GetMapping("/get-lesson")
    public ResponseEntity<ResponseDto<LessonDto>>getLesson(@RequestParam Long lessonId , @RequestParam Long userId) {
        return ResponseEntity.ok(lessonService.findById(lessonId , userId));
    }
    @PostMapping("/set-like")
    public ResponseEntity<ResponseDto<Void>>setLike(@RequestParam Boolean like , @RequestParam Long lessonId , @RequestParam Long userId) {
        return ResponseEntity.ok(lessonService.setLike(like , lessonId , userId));
    }
    @GetMapping("/get-comments-from-lesson")
    public ResponseEntity<ResponseDto<List<CommentDto>>>getCommentsFromLesson(@RequestParam Long lessonId){
        return ResponseEntity.ok(lessonService.getCommentsFromLesson(lessonId));
    }
    @PostMapping("/add-comment")
    public ResponseEntity<ResponseDto<Void>>setComment(@RequestBody AddCommentDto dto){
        return ResponseEntity.ok(lessonService.setComment(dto));
    }
    @DeleteMapping("/delete-comment/{commentId}")
    public ResponseEntity<ResponseDto<Void>>removeComment(@PathVariable Long commentId){
        return ResponseEntity.ok(lessonService.removeComment(commentId));
    }
}