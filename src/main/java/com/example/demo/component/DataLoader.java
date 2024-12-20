package com.example.demo.component;
import com.example.demo.db.domain.Role;
import com.example.demo.db.domain.User;
import com.example.demo.db.repository.RoleRepository;
import com.example.demo.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleModerator = new Role( "ROLE_TEACHER");
        Role roleUser = new Role( "ROLE_STUDENT");
        try {
            roleRepository.save(roleAdmin);
            roleRepository.save(roleModerator);
            roleRepository.save(roleUser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        User user = new User();
        user.setFirstname("Zafar");
        user.setLastname("Ziyatov");
        user.setPassword(passwordEncoder.encode("12345"));
        user.setUsername("me_zafar");
        user.setRoles(List.of(roleRepository.findByName("ROLE_ADMIN").get()));

        User user1 = new User();
        user1.setFirstname("teacher");
        user1.setPassword(passwordEncoder.encode("12345"));
        user1.setUsername("me_teacher");
        user1.setRoles(List.of(roleRepository.findByName("ROLE_TEACHER").get()));
        User user2 = new User();
        user2.setFirstname("student");
        user2.setPassword(passwordEncoder.encode("12345"));
        user2.setUsername("me_student");
        user2.setRoles(List.of(roleRepository.findByName("ROLE_STUDENT").get()));
        try {
            userRepository.save(user);
            userRepository.save(user1);
            userRepository.save(user2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
