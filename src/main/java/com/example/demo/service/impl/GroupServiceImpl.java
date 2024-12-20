package com.example.demo.service.impl;

import com.example.demo.db.domain.*;
import com.example.demo.db.repository.CourseRepository;
import com.example.demo.db.repository.GroupRepository;
import com.example.demo.db.repository.UserRepository;
import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.group.AddGroupDto;
import com.example.demo.dto.group.EditGroupDto;
import com.example.demo.dto.group.GroupDto;
import com.example.demo.service.GroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;

    private List<Lesson> getLessons(Group group) {
        return groupRepository.findActiveLessonsByGroupId(group.getId());
    }

    private List<User> getUsers(Group group) {
        return groupRepository.findActiveUsersByGroupId(group.getId());
    }

    @Override
    public ResponseDto<Page<GroupDto>> findAll(Boolean active, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Group> gp = groupRepository.findAllByActiveOrderById(active, pageable);
            Page<GroupDto> res = gp.map(group -> {

                List<Long> userIds = getUsers(group).stream()
                        .map(User::getId)
                        .toList();
                List<Long> lessonIds = getLessons(group).stream()
                        .map(Lesson::getId)
                        .toList();
                return GroupDto
                        .builder()
                        .id(group.getId())
                        .name(group.getName())
                        .description(group.getDescription())
                        .userIds(userIds)
                        .lessonIds(lessonIds)
                        .courseId(group.getCourse().getId())
                        .build();
            });
            return new ResponseDto<>(true, "Ok", res);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> addGroup(AddGroupDto groupDto) {
        try {
            Optional<User> uOp = userRepository.findById(groupDto.getTeacherId());
            if (uOp.isEmpty())
                return new ResponseDto<>(false, "Not found teacher id");
            boolean isTeacher = false;
            User user = uOp.get();
            List<Group> groups = user.getGroups();
            for (Role role : user.getRoles()) {
                if (role.getName().equals("ROLE_TEACHER")) {
                    isTeacher = true;
                    break;
                }
            }
            if (!isTeacher)
                return new ResponseDto<>(false, "Not found teacher");
            Group group = new Group();
            group.setName(groupDto.getName());
            group.setDescription(groupDto.getDescription());
            group.setActive(true);
            Optional<Course> cOp = courseRepository.findById(groupDto.getCourseId());
            if (cOp.isEmpty())
                return new ResponseDto<>(false, "Not found course");
            Course course = cOp.get();
            group.setCourse(course);
            List<User> users = new ArrayList<>();
            users.add(user);
            group.setUsers(users);
            groupRepository.save(group);
            groups.add(group);
            user.setGroups(groups);
            userRepository.save(user);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> removeUserFromGroup(Long groupId, Long userId) {
        try {
            Optional<Group> gOp = groupRepository.findById(groupId);
            if (gOp.isEmpty())
                return new ResponseDto<>(false, "Not found group");
            Group group = gOp.get();
            if (!group.getActive()) {
                return new ResponseDto<>(false, "Not found group");
            }
            Optional<User> uOp = userRepository.findById(userId);
            if (uOp.isEmpty()) {
                return new ResponseDto<>(false, "Not found student");
            }
            User user = uOp.get();
            boolean isUser = false;
            for (Role role : user.getRoles()) {
                if (role.getName().equals("ROLE_STUDENT")) {
                    isUser = true;
                    break;
                }
            }
            if (!isUser)
                return new ResponseDto<>(false, "Not found student");
            group.getUsers().remove(user);
            groupRepository.save(group);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Page<GroupDto>> studentGroups(Long studentId, int page, int size) {
        try {
            Optional<User> uOp = userRepository.findById(studentId);
            boolean success = false;
            if (uOp.isEmpty())
                return new ResponseDto<>(false, "Not found teacher");
            for (Role role : uOp.get().getRoles()) {
                if (role.getName().equals("ROLE_STUDENT")) {
                    success = true;
                    break;
                }
            }
            if (!success) {
                return new ResponseDto<>(false, "Not found teacher role");
            }
            User teacher = uOp.get();
            Pageable pageable = PageRequest.of(page, size);
            Page<Group> groupPage = groupRepository.getAllGroupsFromUser(studentId, pageable);
            return new ResponseDto<>(true, "Ok", groupPage.map(group -> {
                List<Long> userIds = new ArrayList<>();
                List<Long> lessonIds = new ArrayList<>();
                List<User> users = getUsers(group);
                for (User user : users) {
                    userIds.add(user.getId());
                }
                List<Lesson> lessons = getLessons(group);
                for (Lesson lesson : lessons) {
                    lessonIds.add(lesson.getId());
                }
                return GroupDto
                        .builder()
                        .id(group.getId())
                        .name(group.getName())
                        .description(group.getDescription())
                        .userIds(userIds)
                        .lessonIds(lessonIds)
                        .courseId(group.getCourse().getId())
                        .build();
            }));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }

    }

    @Override
    public ResponseDto<Page<GroupDto>> teacherGroups(Long teacherId, int page, int size) {
        try {
            Optional<User> uOp = userRepository.findById(teacherId);
            boolean success = false;
            if (uOp.isEmpty())
                return new ResponseDto<>(false, "Not found teacher");
            for (Role role : uOp.get().getRoles()) {
                if (role.getName().equals("ROLE_TEACHER")) {
                    success = true;
                    break;
                }
            }
            if (!success) {
                return new ResponseDto<>(false, "Not found teacher role");
            }
            User teacher = uOp.get();
            Pageable pageable = PageRequest.of(page, size);
            Page<Group> groupPage = groupRepository.getAllGroupsFromUser(teacherId, pageable);
            return new ResponseDto<>(true, "Ok", groupPage.map(group -> {
                List<Long> userIds = new ArrayList<>();
                List<Long> lessonIds = new ArrayList<>();
                List<User> users = getUsers(group);
                for (User user : users) {
                    userIds.add(user.getId());
                }
                List<Lesson> lessons = getLessons(group);
                for (Lesson lesson : lessons) {
                    lessonIds.add(lesson.getId());
                }
                return GroupDto
                        .builder()
                        .id(group.getId())
                        .name(group.getName())
                        .description(group.getDescription())
                        .userIds(userIds)
                        .lessonIds(lessonIds)
                        .courseId(group.getCourse().getId())
                        .build();
            }));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> editTeacherFromGroup(Long teacherId, Long groupId) {
        try {
            Optional<User> uOp = userRepository.findById(teacherId);
            if (uOp.isEmpty()) {
                return new ResponseDto<>(false, "Not found teacher id");
            }
            Optional<Group> gOp = groupRepository.findById(groupId);
            if (gOp.isEmpty()) return new ResponseDto<>(false, "Not found group id");
            User user = uOp.get();
            Group group = gOp.get();
            User oldTeacher = null;
            for (User groupUser : getUsers(group)) {
                for (Role role : groupUser.getRoles()) {
                    if (role.getName().equals("ROLE_TEACHER")) {
                        oldTeacher = groupUser;
                        break;
                    }
                }

            }
            if (oldTeacher == null)
                return new ResponseDto<>(false, "Not found teacher because all role student");
            group.getUsers().remove(oldTeacher);
            group.getUsers().add(user);
            groupRepository.save(group);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> deleteGroup(Long groupId) {
        try {
            Optional<Group> gOp = groupRepository.findById(groupId);
            if (gOp.isEmpty()) {
                return new ResponseDto<>(false, "Not found group id");
            }
            Group group = gOp.get();
            if (!group.getActive())
                return new ResponseDto<>(false, "Allaqachon o'chirib bo'lgansiz");
            group.setActive(false);
            groupRepository.save(group);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }


    @Override
    public ResponseDto<Void> editGroup(EditGroupDto dto, Long groupId) {
        try {
            Optional<Group> gOp = groupRepository.findById(groupId);
            if (gOp.isEmpty())
                return new ResponseDto<>(false, "Not found group");
            Group group = gOp.get();
            group.setName(dto.getName());
            group.setDescription(dto.getDescription());
            groupRepository.save(group);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<GroupDto> findById(Long id) {
        try {
            Optional<Group> gp = groupRepository.findById(id);
            if (gp.isPresent()) {
                List<Long> userIds = getUsers(gp.get()).stream()
                        .map(User::getId)
                        .toList();
                List<Long> lessonIds = getLessons(gp.get()).stream()
                        .map(Lesson::getId)
                        .toList();
                GroupDto groupDto = GroupDto
                        .builder()
                        .id(gp.get().getId())
                        .name(gp.get().getName())
                        .description(gp.get().getDescription())
                        .userIds(userIds)
                        .lessonIds(lessonIds)
                        .courseId(gp.get().getCourse().getId())
                        .build();
                return new ResponseDto<>(true, "Ok", groupDto);
            }
            return new ResponseDto<>(false, "Not found group id");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> joinGroup(Long userId, Long groupId) {
        try {
            Optional<User> uOp = userRepository.findById(userId);
            if (uOp.isEmpty())
                return new ResponseDto<>(false, "Not found user id");
            Optional<Group> gOp = groupRepository.findById(groupId);
            if (gOp.isEmpty())
                return new ResponseDto<>(false, "Not found group id");
            Group group = gOp.get();
            User user = uOp.get();
            user.getGroups().add(group);
            group.getUsers().add(user);
            userRepository.save(user);
            groupRepository.save(group);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> removeGroup(Long userId, Long groupId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                return new ResponseDto<>(false, "User not found with id: " + userId);
            }

            Optional<Group> groupOptional = groupRepository.findById(groupId);
            if (groupOptional.isEmpty()) {
                return new ResponseDto<>(false, "Group not found with id: " + groupId);
            }

            User user = userOptional.get();
            Group group = groupOptional.get();

            if (user.getGroups().contains(group)) {
                user.getGroups().remove(group);
            } else {
                return new ResponseDto<>(false, "Group is not associated with the user");
            }

            if (group.getUsers().contains(user)) {
                group.getUsers().remove(user);
            } else {
                return new ResponseDto<>(false, "User is not associated with the group");
            }

            userRepository.save(user);
            groupRepository.save(group);

            return new ResponseDto<>(true, "Group successfully removed from user");
        } catch (Exception e) {
            log.error("Error while removing group from user", e);
            return new ResponseDto<>(false, "Error: " + e.getMessage());
        }
    }

}
