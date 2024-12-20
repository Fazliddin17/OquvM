package com.example.demo.rest.student;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.group.GroupDto;
import com.example.demo.dto.lesson.LessonDto;
import com.example.demo.service.GroupService;
import com.example.demo.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/student/group")
public class StudentGroupRestController {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private GroupService groupService;

    @GetMapping("/get-lessons-from-group/{groupId}")
    public ResponseEntity<ResponseDto<Page<LessonDto>>> getLessonsFromGroup(
            @PathVariable Long groupId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(lessonService.findAllByGroupId(groupId, page, size));
    }

    @GetMapping("/student-groups/{student_id}")
    public ResponseEntity<ResponseDto<Page<GroupDto>>> userGroups(
            @PathVariable Long student_id,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(groupService.studentGroups(student_id, page, size));
    }

    @PostMapping("/join-group")
    public ResponseEntity<ResponseDto<Void>> joinGroup(@RequestParam Long userId, @RequestParam Long groupId) {
        return ResponseEntity.ok(groupService.joinGroup(userId, groupId));
    }

    @DeleteMapping("/remove-group")
    public ResponseEntity<ResponseDto<Void>> removeGroup(@RequestParam Long userId, @RequestParam Long groupId) {
        return ResponseEntity.ok(groupService.removeGroup(userId, groupId));
    }

}
