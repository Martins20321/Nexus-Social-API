package com.martinsdev.nexussocial.api.infra.security;

import com.martinsdev.nexussocial.api.model.enums.UserRole;
import com.martinsdev.nexussocial.api.model.user.User;
import com.martinsdev.nexussocial.api.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataSeeder implements ApplicationRunner {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (repository.findByRole(UserRole.ADMIN).isEmpty()){
            User user = User.builder()
                    .name("ADMIN Nexus")
                    .email("admin@nexus.com")
                    .password(passwordEncoder.encode("admin1090"))
                    .role(UserRole.ADMIN)
                    .enabled(true)
                    .createdAt(LocalDateTime.now())
                    .build();
            repository.save(user);
        }
    }
}
