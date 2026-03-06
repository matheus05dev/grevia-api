package com.projeto1cc.grevia.core.security;

import com.projeto1cc.grevia.user.model.User;
import com.projeto1cc.grevia.user.model.enums.Role;
import com.projeto1cc.grevia.user.model.enums.Status;
import com.projeto1cc.grevia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("admin-config.properties")) {
            props.load(fis);
            String email = props.getProperty("admin.email");
            String password = props.getProperty("admin.password");
            String name = props.getProperty("admin.name", "System Admin");

            if (email != null && password != null) {
                if (userRepository.findByEmail(email).isEmpty()) {
                    User admin = new User();
                    admin.setName(name);
                    admin.setEmail(email);
                    admin.setPassword(passwordEncoder.encode(password));
                    admin.setRole(Role.ADMIN);
                    admin.setStatus(Status.Active);
                    userRepository.save(admin);
                    log.info("Admin user criado: {}", email);
                } else {
                    log.info("Admin user já existe: {}", email);
                }
            }
        } catch (IOException e) {
            log.info("admin-config.properties não encontrado. Pulando a criação do admin.");
        }
    }
}
