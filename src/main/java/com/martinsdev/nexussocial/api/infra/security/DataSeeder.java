package com.martinsdev.nexussocial.api.infra.security;

import com.martinsdev.nexussocial.api.model.user.User;
import com.martinsdev.nexussocial.api.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataSeeder implements ApplicationRunner {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (repository.findByLogin("professor") == null){
            User user = new User(); 
            user.setLogin("professor@nexus.com");
            user.setPassword(passwordEncoder.encode("professorNexus123"));
            repository.save(user);
        }
    }
}
