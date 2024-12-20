package com.example.demo.rest.teacher;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.group.AddGroupDto;
import com.example.demo.dto.group.EditGroupDto;
import com.example.demo.dto.group.GroupDto;
import com.example.demo.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@CrossOrigin

@RestController
@RequestMapping("/teacher/group")
public class TeacherGroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping("/get-groups-of-teacher/{teacherId}")
    public ResponseEntity<ResponseDto<Page<GroupDto>>> getTeacherGroups(
            @PathVariable Long teacherId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(groupService.teacherGroups(teacherId, page, size));
    }

    @PostMapping("/create-group")
    public ResponseEntity<ResponseDto<Void>> createGroup(@RequestBody AddGroupDto groupDto) {
        return ResponseEntity.ok(groupService.addGroup(groupDto));
    }

    @PutMapping("/edit-group/{groupId}")
    public ResponseEntity<ResponseDto<Void>> editGroup(@RequestBody EditGroupDto dto, @PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.editGroup(dto, groupId));
    }
/*
    @PostMapping("/remove-student-from-group}")
    public ResponseEntity<ResponseDto<Void>> removeUser(@RequestParam Long groupId, @RequestParam Long userId) {
        return ResponseEntity.ok(groupService.removeUserFromGroup(groupId, userId));
    }
*/
    @DeleteMapping("/delete-group/{groupId}")
    public ResponseEntity<ResponseDto<Void>> deleteGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.deleteGroup(groupId));
    }

}
