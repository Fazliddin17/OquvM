package com.example.demo.service.impl;

import com.example.demo.component.jwt.JwtService;
import com.example.demo.db.domain.Group;
import com.example.demo.db.domain.ProfileImage;
import com.example.demo.db.domain.Role;
import com.example.demo.db.domain.User;
import com.example.demo.db.repository.GroupRepository;
import com.example.demo.db.repository.ProfileImageRepository;
import com.example.demo.db.repository.RoleRepository;
import com.example.demo.db.repository.UserRepository;
import com.example.demo.dto.*;
import com.example.demo.dto.form.LoginForm;
import com.example.demo.dto.form.SignUpForm;
import com.example.demo.dto.student.EditProfileDto;
import com.example.demo.dto.teacher.AddTeacherDto;
import com.example.demo.dto.teacher.TeacherDto;
import com.example.demo.helper.SecurityHelper;
import com.example.demo.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Value("${jwt.expireDate}")
    private Long jwtExpireDate;
    @Value("${profile.image.url}")
    private String profileImageUrl;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ProfileImageRepository profileImageRepository;

    @Override
    public ResponseDto<LoginResponseDto> signUp(SignUpForm form) throws Exception {
        try {
            Optional<User> uOp = userRepository.findByUsername(form.getUsername());
            if (uOp.isPresent()) {
                return new ResponseDto<>(false, "This username is busy. Please enter a different username");
            }
            User user = new User();
            user.setUsername(form.getUsername());
            user.setPassword(passwordEncoder.encode(form.getPassword()));
            Optional<Role> rOp = roleRepository.findByName("ROLE_STUDENT");
            if (rOp.isEmpty())
                return new ResponseDto<>(false, "Not found role");
            List<Role> roles = List.of(rOp.get());
            user.setRoles(roles);
            user.setFirstname(form.getFirstname());
            user.setLastname(form.getLastname());
            userRepository.save(user);
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(form.getUsername());
                return new ResponseDto<>(
                        true, "ok", LoginResponseDto.builder()
                        .access_token(token)
                        .refresh_token(token)
                        .expireDate(jwtExpireDate)
                        .build()
                );
            } else {
                throw new Exception("invalid user request..!!");
            }
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<LoginResponseDto> signin(LoginForm form) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(form.getUsername());
                return new ResponseDto<>(
                        true, "ok", LoginResponseDto.builder()
                        .access_token(token)
                        .refresh_token(token)
                        .expireDate(jwtExpireDate)
                        .build()
                );
            } else {
                throw new Exception("invalid user request..!!");
            }
        } catch (Exception e) {

            return new ResponseDto<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseDto<Void> addTeacher(AddTeacherDto teacherDto) throws Exception {
        try {
            if (userRepository.findByUsername(teacherDto.getUsername()).isPresent()) {
                return new ResponseDto<>(false, "Bu username avaldan mavjud, boshqa username tanlang");
            }
            Optional<Role> rOp = roleRepository.findByName("ROLE_TEACHER");
            if (rOp.isEmpty())
                return new ResponseDto<>(false, "Not found role");
            List<Role> roles = List.of(rOp.get());
            User user = new User();
            user.setUsername(teacherDto.getUsername());
            user.setPassword(passwordEncoder.encode(teacherDto.getPassword()));
            user.setFirstname(teacherDto.getFirstname());
            user.setLastname(teacherDto.getLastname());
            user.setRoles(roles);
            user.setActive(true);
            userRepository.save(user);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> editTeacher(TeacherDto teacherDto, Long teacherId) throws Exception {
        try {
            Optional<User> tOp = userRepository.findById(teacherId);
            if (tOp.isEmpty()) {
                throw new Exception("Not found teacher id");
            }
            User teacher = tOp.get();
            teacher.setFirstname(teacherDto.getFirstname());
            teacher.setLastname(teacherDto.getLastname());
            teacher.setUsername(teacherDto.getUsername());
            List<Group> groups = new ArrayList<>();
            for (Long groupId : teacherDto.getGroupIds()) {
                Optional<Group> gOp = groupRepository.findById(groupId);
                if (gOp.isEmpty()) {
                    throw new Exception("Not found group id");
                }
                Group group = gOp.get();
                groups.add(group);
                List<User> users = group.getUsers();
                boolean success = false;
                for (User user : users) {
                    if (user.getId().equals(teacher.getId())) {
                        success = true;
                        break;
                    }
                }
                if (!success) {
                    users.add(teacher);
                    group.setUsers(users);
                    groupRepository.save(group);
                }

            }
            teacher.setGroups(groups);
            userRepository.save(teacher);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }
    @Override
    public ResponseDto<Page<UserDto>> findAllUsers(int page, int size) throws Exception {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> userPage = userRepository.findAll(pageable);
            Page<UserDto> res = userPage.map(user -> {
                List<String> roles = new ArrayList<>();
                for (Role role : user.getRoles()) {
                    roles.add(role.getName().substring(5));
                }
                return
                        UserDto
                                .builder()
                                .userId(user.getId())
                                .lastname(user.getLastname())
                                .firstname(user.getFirstname())
                                .username(user.getUsername())
                                .roles(roles)
                                .build();
            });
            return new ResponseDto<>(true, "Ok", res);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }
    @Override
    public ResponseDto<UserDto> findById(Long userId) throws Exception {
        try {
            Optional<User> uOp = userRepository.findById(userId);
            if (uOp.isEmpty())
                return new ResponseDto<>(false, "Not found user id");
            User user = uOp.get();
            UserDto userDto = new UserDto();
            userDto.setUserId(user.getId());
            userDto.setFirstname(user.getFirstname());
            userDto.setLastname(user.getLastname());
            userDto.setUsername(user.getUsername());
            List<String> roles = new ArrayList<>();
            for (Role role : user.getRoles()) {
                roles.add(role.getName().substring(5).toLowerCase());
            }
            userDto.setRoles(roles);
            return new ResponseDto<>(true, "Ok", userDto);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> deleteProfileImage(Long profileImageId) throws Exception {
        try {
            Optional<ProfileImage> pOp = profileImageRepository.findById(profileImageId);
            if (pOp.isEmpty()) {
                return new ResponseDto<>(false, "Not found profile image id");
            }
            ProfileImage profileImage = pOp.get();
            profileImage.setActive(false);
            profileImageRepository.save(profileImage);
            return new ResponseDto<>(true , "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false , e.getMessage() );
        }
    }

    @Override
    public ResponseDto<Void> addProfileImage(MultipartFile file, Long userId) {
        try {
            Optional<User> uOp = userRepository.findById(userId);
            if (uOp.isEmpty())
                return new ResponseDto<>(false , "not found user");
            User user = uOp.get();
            String directory = profileImageUrl;
            Path uploadDir = Paths.get(directory);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            List<ProfileImage> list = profileImageRepository.findAll(Sort.by("id"));
            String newFileName = "image-" + UUID.randomUUID() + list.get(list.size() - 1).getId() + "." + originalFilename.substring(originalFilename.length() - 3);
            Path targetFile = uploadDir.resolve(newFileName);
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            String imageUrl = "/profile-images/" + newFileName;
            ProfileImage profileImage = new ProfileImage();
            profileImage.setImageUrl(imageUrl);
            profileImage.setUser(user);
            profileImage.setActive(true);
            user.getProfileImages().add(profileImage);
            profileImageRepository.save(profileImage);
            userRepository.save(user);
            return new ResponseDto<>(true , "Ok" );
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false , e.getMessage() );
        }
    }

    @Override
    public ResponseDto<Void> editProfile(EditProfileDto dto) throws Exception {
        try {
            Optional<User> uOp = userRepository.findById(dto.getId());
            if (uOp.isEmpty())
                return new ResponseDto<>(false, "Not found user");
            User user = uOp.get();
            user.setFirstname(dto.getFirstName());
            user.setLastname(dto.getLastName());
            user.setUsername(dto.getUsername());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            userRepository.save(user);
            return new ResponseDto<>(true, "Ok");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> deleteTeacher(Long teacherId) throws Exception {
        try {
            Optional<User> tOp = userRepository.findById(teacherId);
            if (tOp.isEmpty()) {
                throw new Exception("Not found teacher id");
            }
            boolean isTeacher = false;
            for (Role role : tOp.get().getRoles()) {
                if (role.getName().equals("ROLE_TEACHER")) {
                    isTeacher = true;
                    break;
                }
            }
            if (!isTeacher) {
                throw new Exception("Not found teacher");
            }
            User user = tOp.get();
            Optional<Role> rOp = roleRepository.findByName("ROLE_STUDENT");
            if (rOp.isEmpty()) {
                throw new Exception("Not found role");
            }
            Role role = rOp.get();
            List<Role> updatedRoles = new ArrayList<>(user.getRoles());
            user.setGroups(new ArrayList<>());
            //chala  shuyerda guruhdan ushbu o'qituvchini o'chirish qismi qilinmagan
            updatedRoles.clear();
            updatedRoles.add(role);
            user.setRoles(updatedRoles);
            userRepository.save(user);
            return new ResponseDto<>(true, "Successfully delete teacher");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Page<TeacherDto>> getAllTeachers(int page, int size) throws Exception {
        try {
            Pageable pageable = PageRequest.of(page, size);

            // Sahifalangan ma'lumotlarni olish
            Page<User> teacherPage = userRepository.findAllByRoleName("ROLE_TEACHER", pageable);

            if (teacherPage.isEmpty()) {
                return new ResponseDto<>(false, "Hozirda teacher mavjud emas");
            }

            // TeacherPage ichidagi har bir foydalanuvchini TeacherDto ga o‘tkazish
            Page<TeacherDto> teacherDtoPage = teacherPage.map(teacher -> {
                List<Long> groupIds = teacher.getGroups().stream()
                        .map(Group::getId)
                        .toList();

                TeacherDto dto = new TeacherDto();
                dto.setFirstname(teacher.getFirstname());
                dto.setLastname(teacher.getLastname());
                dto.setUsername(teacher.getUsername());
                dto.setGroupIds(groupIds);
                dto.setId(teacher.getId());

                return dto;
            });

            return new ResponseDto<>(true, "Tamam", teacherDtoPage);
        } catch (Exception e) {
            log.error("Error while getting teachers: ", e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }


    @Override
    public ResponseDto<UserResponseDto> currentUserInfo() throws Exception {
        try {
            User currentUser = SecurityHelper.getCurrentUser();
            if (currentUser == null) {
                throw new Exception("Current user is null");
            }
            List<String> roles = new ArrayList<>();
            for (Role role : currentUser.getRoles()) {
                roles.add(role.getName().substring(5));
            }
            List<ProfileDto> images = new ArrayList<>();
            List<ProfileImage> list = profileImageRepository.findAllByUserIdAndActiveOrderById(currentUser.getId(), true);
            for (ProfileImage profileImage : list) {
                ProfileDto dto = new ProfileDto();
                dto.setUserId(profileImage.getUser().getId());
                dto.setProfileImageId(profileImage.getId());
                dto.setProfileImageUrl(profileImage.getImageUrl());
                images.add(dto);
            }
            UserResponseDto dto = new UserResponseDto(
                    currentUser.getId(),
                    currentUser.getFirstname(),
                    currentUser.getLastname(),
                    currentUser.getUsername(),
                    roles, images
            );
            return new ResponseDto<>(true, "ok", dto);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }
}
