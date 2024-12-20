package com.example.demo.service;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.group.AddGroupDto;
import com.example.demo.dto.group.EditGroupDto;
import com.example.demo.dto.group.GroupDto;
import org.springframework.data.domain.Page;

public interface GroupService {
    ResponseDto<Page<GroupDto>> findAll(Boolean active, int page, int size) ;
    ResponseDto<GroupDto>findById(Long id) ;
    ResponseDto<Void>addGroup(AddGroupDto groupDto) ;
    ResponseDto<Void>editGroup(EditGroupDto dto, Long groupId) ;
    ResponseDto<Void>deleteGroup(Long groupId) ;
    ResponseDto<Void>editTeacherFromGroup(Long teacherId, Long groupId) ;
    ResponseDto<Page<GroupDto>> teacherGroups(Long studentId, int page, int size) ;
    ResponseDto<Page<GroupDto>>studentGroups(Long teacherId, int page, int size) ;
    ResponseDto<Void>removeUserFromGroup(Long groupId, Long userId) ;
    ResponseDto<Void>joinGroup(Long userId, Long groupId) ;
    ResponseDto<Void>removeGroup(Long userId, Long groupId) ;
}
