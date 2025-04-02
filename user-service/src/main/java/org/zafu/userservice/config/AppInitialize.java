package org.zafu.userservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zafu.userservice.model.Role;
import org.zafu.userservice.model.User;
import org.zafu.userservice.repository.UserRepository;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j(topic = "APP-INITIALIZE")
public class AppInitialize {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(){
        log.info("Initializing user application");
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()){
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("150802"))
                        .email("admin@gmail.com")
                        .isActivated(true)
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(user);
                log.info("Admin user created!");
            }
            log.info("Admin user already exists");
        };
    }
}
