package com.upc.cedorehab.config;

import com.upc.cedorehab.security.entities.Role;
import com.upc.cedorehab.security.entities.User;
import com.upc.cedorehab.security.repositories.RoleRepository;
import com.upc.cedorehab.security.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Role roleUser = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
                Role r = new Role();
                r.setName("ROLE_USER");
                return roleRepository.save(r);
            });

            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
                Role r = new Role();
                r.setName("ROLE_ADMIN");
                return roleRepository.save(r);
            });

            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setRoles(Set.of(roleAdmin));
                userRepository.save(admin);
            }

            if (userRepository.findByUsername("user1").isEmpty()) {
                User user1 = new User();
                user1.setUsername("user1");
                user1.setPassword(passwordEncoder.encode("password"));
                user1.setRoles(Set.of(roleUser));
                userRepository.save(user1);
            }
        } catch (Exception ignored) {
        }
    }
}
