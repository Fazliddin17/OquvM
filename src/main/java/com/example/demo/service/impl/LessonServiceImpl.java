package com.example.demo.service.impl;

import com.example.demo.db.domain.*;
import com.example.demo.db.repository.*;
import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.commentAndLike.AddCommentDto;
import com.example.demo.dto.commentAndLike.CommentDto;
import com.example.demo.dto.lesson.AddLessonDto;
import com.example.demo.dto.lesson.LessonDto;
import com.example.demo.service.LessonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;


    @Value("${course.video.directory}")
    private String courseVideoUrl;

    @Override
    public ResponseDto<Page<LessonDto>> findAll(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Lesson> lessonPage = lessonRepository.findAllByActiveOrderById(true, pageable);
            return new ResponseDto<>(true, "Ok", lessonPage.map(lesson -> LessonDto
                    .builder()
                    .id(lesson.getId())
                    .name(lesson.getName())
                    .description(lesson.getDescription())
                    .videoUrl(lesson.getVideoUrl())
                    .groupId(lesson.getGroup().getId())
                    .build()));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> editLesson(Long lessonId, AddLessonDto dto) {
        try {
            Optional<Lesson> lOp = lessonRepository.findById(lessonId);
            if (lOp.isEmpty())
                return new ResponseDto<>(false, "Not found lesson id");
            Lesson lesson = lOp.get();
            lesson.setName(dto.getName());
            lesson.setDescription(dto.getDescription());
            lessonRepository.save(lesson);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> setLike(Boolean like, Long lessonId, Long userId) {
        try {
            Optional<Lesson> byId = lessonRepository.findById(lessonId);
            if (byId.isEmpty())
                return new ResponseDto<>(false, "Not found lesson id");
            Optional<User> uOp = userRepository.findById(userId);
            if (uOp.isEmpty())
                return new ResponseDto<>(false, "Not found user id");
            Optional<Like> lOp = likeRepository.findByLessonIdAndUserIdAndActiveTrue(lessonId, userId);
            Like currentLike;
            if (lOp.isEmpty()) {
                currentLike = new Like();
                currentLike.setLessonId(lessonId);
                currentLike.setActive(like);
                currentLike.setUserId(userId);
            } else {
                currentLike = lOp.get();
                currentLike.setActive(like);
            }
            likeRepository.save(currentLike);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> removeComment(Long commentId) {
        try {
            Optional<Comment> byId = commentRepository.findById(commentId);
            if (byId.isEmpty())
                return new ResponseDto<>(false, "Not found comment id");
            Comment comment = byId.get();
            if (!comment.getActive()){
                return new ResponseDto<>(false , "Not found comment id" );
            }
            comment.setActive(false);
            commentRepository.save(comment);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> setComment(AddCommentDto dto) {
        try {
            Optional<Lesson> lOp = lessonRepository.findById(dto.getLessonId());
            if (lOp.isEmpty())
                return new ResponseDto<>(false, "Not found lesson id");
            Comment comment = new Comment();
            comment.setActive(true);
            comment.setLessonId(dto.getLessonId());
            comment.setUserId(dto.getUserId());
            comment.setText(dto.getText());
            commentRepository.save(comment);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<List<CommentDto>> getCommentsFromLesson(Long lessonId) {
        try {
            List<Comment> comments = commentRepository.findAllByLessonIdAndActiveTrueOrderById(lessonId);
            List<CommentDto> r = new ArrayList<>();
            for (Comment comment : comments) {
                CommentDto commentDto = new CommentDto();
                commentDto.setId(comment.getId());
                commentDto.setLessonId(comment.getLessonId());
                commentDto.setUserId(comment.getUserId());
                commentDto.setText(comment.getText());
                r.add(commentDto);
            }
            return new ResponseDto<>(true, "Ok", r);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<LessonDto> findById(Long lessonId, Long userId) {
        try {
            Optional<Lesson> lOp = lessonRepository.findById(lessonId);
            if (lOp.isEmpty())
                return new ResponseDto<>(false, "Not found lesson id");
            List<Like> likes = likeRepository.findAllByLessonIdAndActiveTrueOrderById(lessonId);
            int likeCount = likes.size();
            boolean isLike = likeRepository.findByLessonIdAndUserIdAndActiveTrue(lessonId, userId).isPresent();
            List<Comment> comments = commentRepository.findAllByLessonIdAndActiveTrueOrderById(lessonId);
            List<CommentDto> resComments = new ArrayList<>();
            for (Comment comment : comments) {
                CommentDto dto = new CommentDto();
                dto.setId(comment.getId());
                dto.setLessonId(comment.getLessonId());
                dto.setUserId(comment.getUserId());
                dto.setText(comment.getText());
                resComments.add(dto);
            }
            Lesson lesson = lOp.get();
            LessonDto lessonDto = new LessonDto();
            lessonDto.setId(lesson.getId());
            lessonDto.setName(lesson.getName());
            lessonDto.setDescription(lesson.getDescription());
            lessonDto.setVideoUrl(lesson.getVideoUrl());
            lessonDto.setGroupId(lesson.getGroup().getId());
            lessonDto.setLikeCount(likeCount);
            lessonDto.setSuccessLike(isLike);
            lessonDto.setComments(resComments);
            return new ResponseDto<>(true, "Ok", lessonDto);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> editLessonVideo(Long lessonId, MultipartFile file) {
        try {
            Optional<Lesson> lOp = lessonRepository.findById(lessonId);
            if (lOp.isEmpty())
                return new ResponseDto<>(false, "Not found lesson id");
            String directory = courseVideoUrl;
            Path uploadDir = Paths.get(directory);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".mp4")) {
                return new ResponseDto<>(false, "Fayl formati noto‘g‘ri! Faqat .mp4 format qabul qilinadi.");
            }
            Lesson lesson = lOp.get();
            long id = lesson.getId();
            String newFileName = "video-" + id + ".mp4";
            Path targetFile = uploadDir.resolve(newFileName);
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            String videoUrl = "/course-videos/" + newFileName;
            lesson.setVideoUrl(videoUrl);
            lessonRepository.save(lesson);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> deleteLesson(Long lessonId) {
        try {
            Optional<Lesson> lOp = lessonRepository.findById(lessonId);
            if (lOp.isEmpty())
                return new ResponseDto<>(false, "Not found lesson id");
            Lesson lesson = lOp.get();
            lesson.setActive(false);
            lessonRepository.save(lesson);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Page<LessonDto>> findAllByGroupId(Long groupId, int page, int size) {
        try {
            if (groupRepository.findById(groupId).isEmpty()) {
                return new ResponseDto<>(false, "Not found group id");
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Lesson> lessonPage = lessonRepository.findAllByGroupIdAndActiveOrderById(groupId, true, pageable);
            return new ResponseDto<>(true, "Ok", lessonPage.map(lesson -> LessonDto
                    .builder()
                    .id(lesson.getId())
                    .name(lesson.getName())
                    .description(lesson.getDescription())
                    .videoUrl(lesson.getVideoUrl())
                    .groupId(lesson.getGroup().getId())
                    .build()));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> addLesson(AddLessonDto lessonDto, MultipartFile file) {
        try {
            String directory = courseVideoUrl;
            Path uploadDir = Paths.get(directory);

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".mp4")) {
                return new ResponseDto<>(false, "Fayl formati noto‘g‘ri! Faqat .mp4 format qabul qilinadi.");
            }

            List<Lesson> all = lessonRepository.findAllByOrderById();
            long id = all.isEmpty() ? 1 : all.get(all.size() - 1).getId() + 1;
            String newFileName = "video-" + id + ".mp4";
            Path targetFile = uploadDir.resolve(newFileName);

            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

            Lesson lesson = new Lesson();
            lesson.setName(lessonDto.getName());
            lesson.setVideoUrl("/course-videos/" + newFileName);
            lesson.setDescription(lessonDto.getDescription());
            lesson.setActive(true);

            Optional<Group> gOp = groupRepository.findById(lessonDto.getGroupId());
            if (gOp.isEmpty()) {
                return new ResponseDto<>(false, "Berilgan ID bo‘yicha guruh topilmadi.");
            }
            lesson.setGroup(gOp.get());
            lessonRepository.save(lesson);
            Group group = gOp.get();
            group.getLessons().add(lesson);
            groupRepository.save(group);
            return new ResponseDto<>(true, "Dars muvaffaqiyatli qo‘shildi.");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }
}
