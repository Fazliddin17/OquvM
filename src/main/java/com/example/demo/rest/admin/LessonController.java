package com.example.demo.rest.admin;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.lesson.AddLessonDto;
import com.example.demo.dto.lesson.LessonDto;
import com.example.demo.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/lesson")
@CrossOrigin
public class LessonController {
    @Autowired
    private LessonService lessonService;
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<Page<LessonDto>>> findAll(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(lessonService.findAll(page, size));
    }
    @GetMapping("/get-lessons/{groupId}")
    public ResponseEntity<ResponseDto<Page<LessonDto>>> findAllByGroupId(@PathVariable Long groupId, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(lessonService.findAllByGroupId(groupId, page, size));
    }
    @PutMapping("/edit-lessons/{lessonId}")
    public ResponseEntity<ResponseDto<Void>> editLesson(@PathVariable Long lessonId, @RequestBody AddLessonDto dto) {
        return ResponseEntity.ok(lessonService.editLesson(lessonId, dto));
    }
    @PostMapping("/add-lesson")
    public ResponseEntity<ResponseDto<Void>> addLesson(
            @RequestParam String name,
            @RequestParam String desc,
            @RequestParam Long groupId,
            @RequestParam MultipartFile file
    ) {
        AddLessonDto dto = new AddLessonDto();
        dto.setName(name);
        dto.setDescription(desc);
        dto.setGroupId(groupId);
        return ResponseEntity.ok(lessonService.addLesson(dto, file));
    }
    @DeleteMapping("/delete-lesson/{lessonId}")
    public ResponseEntity<ResponseDto<Void>> deleteLesson(@PathVariable Long lessonId) {
        return ResponseEntity.ok(lessonService.deleteLesson(lessonId));
    }
    @PutMapping("/edit-video-lesson")
    public ResponseEntity<ResponseDto<Void>> editVideoLesson(@RequestParam Long lessonId, @RequestParam MultipartFile file) {
        return ResponseEntity.ok(lessonService.editLessonVideo(lessonId, file));
    }
    
}