package com.example.demo.rest.teacher;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.group.AddGroupDto;
import com.example.demo.dto.group.EditGroupDto;
import com.example.demo.dto.lesson.AddLessonDto;
import com.example.demo.dto.lesson.LessonDto;
import com.example.demo.service.GroupService;
import com.example.demo.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin

@RequestMapping("/teacher/lesson")
@RestController
public class TeacherLesson {
    @Autowired
    private LessonService lessonService;

    @GetMapping("/get-lesson")
    public ResponseEntity<ResponseDto<LessonDto>> getLesson(@RequestParam Long lessonId, @RequestParam Long userId) {
        return ResponseEntity.ok(lessonService.findById(lessonId, userId));
    }

    @GetMapping("/get-lessons-from-group/{groupId}")
    public ResponseEntity<ResponseDto<Page<LessonDto>>> getLessonsFromGroup(
            @PathVariable Long groupId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(lessonService.findAllByGroupId(groupId, page, size));
    }

    @PostMapping("/add-lesson")
    public ResponseEntity<ResponseDto<Void>> addGroup(
            @RequestParam String lessonName,
            @RequestParam String description,
            @RequestParam Long groupId,
            @RequestParam MultipartFile file
    ) {
        AddLessonDto dto = new AddLessonDto();
        dto.setName(lessonName);
        dto.setDescription(description);
        dto.setGroupId(groupId);
        return ResponseEntity.ok(lessonService.addLesson(dto, file));
    }

    @PutMapping("/edit-lesson/{lessonId}")
    public ResponseEntity<ResponseDto<Void>> editGroup(@RequestBody AddLessonDto dto, @PathVariable Long lessonId) {
        return ResponseEntity.ok(lessonService.editLesson(lessonId, dto));
    }

    @PutMapping("/edit-lesson-video")
    public ResponseEntity<ResponseDto<Void>> editGroup(@RequestParam Long lessonId, @RequestParam MultipartFile file) {
        return ResponseEntity.ok(lessonService.editLessonVideo(lessonId, file));
    }

    @DeleteMapping("/delete-lesson/{lessonId}")
    public ResponseEntity<ResponseDto<Void>> deleteGroup(@PathVariable Long lessonId) {
        return ResponseEntity.ok(lessonService.deleteLesson(lessonId));
    }
}
