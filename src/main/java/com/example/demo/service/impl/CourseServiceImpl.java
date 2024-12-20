package com.example.demo.service.impl;

import com.example.demo.db.domain.Course;
import com.example.demo.db.domain.Group;
import com.example.demo.db.domain.Lesson;
import com.example.demo.db.domain.User;
import com.example.demo.db.repository.CourseRepository;
import com.example.demo.db.repository.GroupRepository;
import com.example.demo.db.repository.UserRepository;
import com.example.demo.service.CourseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.course.AddCourseDto;
import com.example.demo.dto.course.CourseDto;
import com.example.demo.dto.group.GroupDto;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    private List<Lesson> getLessons(Group group) {
        return groupRepository.findActiveLessonsByGroupId(group.getId());
    }

    private List<User> getUsers(Group group) {
        return groupRepository.findActiveUsersByGroupId(group.getId());
    }

    @Override
    public ResponseDto<Void> addCourse(AddCourseDto courseDto) {
        try {
            Optional<Course> byName = courseRepository.findByName(courseDto.getName());
            if (byName.isPresent()) {
                return new ResponseDto<>(false, "Bunday nomli kurs mavjud, iltimos boshqa nom kiriting");
            }
            Course course = new Course();
            course.setName(courseDto.getName());
            course.setDescription(courseDto.getDescription());
            course.setGroups(new ArrayList<>());
            course.setActive(true);
            courseRepository.save(course);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> edit(CourseDto courseDto) {
        try {
            Optional<Course> cOp = courseRepository.findById(courseDto.getId());
            if (cOp.isEmpty()) {
                return new ResponseDto<>(false, "Course not found id");
            }
            Course course = cOp.get();
            course.setName(courseDto.getName());
            course.setId(courseDto.getId());
            course.setDescription(courseDto.getDescription());
            List<Group> groups = new ArrayList<>();
            for (Long groupId : courseDto.getGroupIds()) {
                Optional<Group> gOp = groupRepository.findById(groupId);
                gOp.ifPresent(groups::add);
            }
            course.setGroups(groups);
            courseRepository.save(course);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    private List<Group> getGroups(Course course) {
        return courseRepository.findActiveGroupsByCourseId(course.getId());
    }

    @Override
    public ResponseDto<CourseDto> findById(Long courseId) {
        try {
            Optional<Course> cOp = courseRepository.findById(courseId);
            if (cOp.isEmpty())
                return new ResponseDto<>(false, "Not found course id");
            Course course = cOp.get();
            List<Long> ids = new ArrayList<>();
            for (Group group : getGroups(course)) {
                ids.add(group.getId());
            }
            CourseDto courseDto = CourseDto
                    .builder()
                    .id(course.getId())
                    .name(course.getName())
                    .description(course.getDescription())
                    .groupIds(ids)
                    .build();

            return new ResponseDto<>(true, "Ok", courseDto);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<CourseDto> findByName(String name) {
        try {
            Optional<Course> cOp = courseRepository.findByName(name);
            if (cOp.isEmpty())
                return new ResponseDto<>(false, "Not found course name");
            Course course = cOp.get();
            List<Long> ids = new ArrayList<>();
            for (Group group : getGroups(course)) {
                ids.add(group.getId());
            }
            CourseDto courseDto = CourseDto
                    .builder()
                    .id(course.getId())
                    .name(course.getName())
                    .description(course.getDescription())
                    .groupIds(ids)
                    .build();

            return new ResponseDto<>(true, "Ok", courseDto);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }

    }

    @Override
    public ResponseDto<Void> deleteById(Long id) {
        try {
            Optional<Course> cOp = courseRepository.findById(id);
            if (cOp.isEmpty()) {
                return new ResponseDto<>(false, "Not found course id");
            }
            Course course = cOp.get();
            course.setName(UUID.randomUUID().toString());
            course.setActive(false);
            courseRepository.save(course);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Page<CourseDto>> findAll(Boolean active, int page, int size) throws Exception {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Course> coursePage = courseRepository.findAllByActiveOrderById(active, pageable);
            Page<CourseDto> courseDtoPage = coursePage.map(course -> {
                List<Long> groupIds = getGroups(course).stream()
                        .map(Group::getId)
                        .toList();

                return CourseDto.builder()
                        .id(course.getId())
                        .name(course.getName())
                        .description(course.getDescription())
                        .groupIds(groupIds)
                        .build();
            });

            return new ResponseDto<>(true, "Ok", courseDtoPage);
        } catch (Exception e) {
            log.error("Error while fetching courses: ", e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Page<GroupDto>> findAllByCourseId(Long courseId, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Group> groupPage = groupRepository.findAllByCourseIdAndActiveOrderById(courseId, true, pageable);
            Page<GroupDto> dto = groupPage.map(group -> {
                List<Long> userIds = getUsers(group).stream()
                        .map(User::getId)
                        .toList();
                List<Long> lessonIds = getLessons(group).stream()
                        .map(Lesson::getId)
                        .toList();
                return GroupDto.builder()
                        .id(group.getId())
                        .name(group.getName())
                        .description(group.getDescription())
                        .courseId(group.getCourse().getId())
                        .userIds(userIds)
                        .lessonIds(lessonIds)
                        .build();
            });
            return new ResponseDto<>(true, "Ok", dto);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

}
