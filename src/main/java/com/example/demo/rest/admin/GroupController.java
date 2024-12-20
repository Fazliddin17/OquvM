package com.example.demo.rest.admin;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.group.AddGroupDto;
import com.example.demo.dto.group.EditGroupDto;
import com.example.demo.dto.group.GroupDto;
import com.example.demo.service.GroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@Log4j2
@RequestMapping("/admin/group")
@CrossOrigin
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping("/list")
    public ResponseEntity<ResponseDto<Page<GroupDto>>> findAll(@RequestParam Boolean active, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(groupService.findAll(active, page, size));
    }

    @GetMapping("/group-id/{groupId}")
    public ResponseEntity<ResponseDto<GroupDto>> findById(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.findById(groupId));
    }

    @PostMapping("/add-group")
    public ResponseEntity<ResponseDto<Void>> addGroup(@RequestBody AddGroupDto dto) {
        return ResponseEntity.ok(groupService.addGroup(dto));
    }

    @PutMapping("/edit-group/{groupId}")
    public ResponseEntity<ResponseDto<Void>> editGroup(@RequestBody EditGroupDto dto, @PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.editGroup(dto, groupId));
    }

    @DeleteMapping("/delete-group/{groupId}")
    public ResponseEntity<ResponseDto<Void>> deleteGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.deleteGroup(groupId));
    }

    @GetMapping("/edit-teacher-from-group")
    public ResponseEntity<ResponseDto<Void>> editTeacherFromGroup(@RequestParam Long groupId, @RequestParam Long teacherId) {
        return ResponseEntity.ok(groupService.editTeacherFromGroup(teacherId, groupId));
    }
}
