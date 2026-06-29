package com.martinsdev.nexussocial.api.service;

import com.martinsdev.nexussocial.api.dto.RegisterRequestUserDTO;
import com.martinsdev.nexussocial.api.dto.UpdateRequestUserDTO;
import com.martinsdev.nexussocial.api.dto.UserResponseDTO;
import com.martinsdev.nexussocial.api.infra.exception.EmailAlreadyExistsException;
import com.martinsdev.nexussocial.api.infra.exception.ResourceNotFoundException;
import com.martinsdev.nexussocial.api.infra.exception.UnauthorizedException;
import com.martinsdev.nexussocial.api.model.enums.UserRole;
import com.martinsdev.nexussocial.api.model.user.User;
import com.martinsdev.nexussocial.api.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO findMe(User client) {
        return new UserResponseDTO(client);
    }

    @Transactional
    public UserResponseDTO register(RegisterRequestUserDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException();
        }
        if (!donor.getId().equals(client.getId())) {
            throw new UnauthorizedException();
        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .phone(dto.phone())
                .role(UserRole.DONOR)
                .createdAt(LocalDateTime.now())
                .enabled(true)
                .build();
        repository.save(user);
        return new UserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO update(Long id, UpdateRequestUserDTO dto, User client) {
        User donor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        if (!donor.getId().equals(client.getId())) {
            throw new UnauthorizedException();
        }
        //Verificando se o email é o mesmo e se já existe no banco
        if (!donor.getEmail().equals(dto.email()) && repository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        if (dto.name() != null) donor.setName(dto.name());
        if (dto.phone() != null) donor.setPhone(dto.phone());
        if (dto.email() != null) donor.setEmail(dto.email());

        repository.save(donor);
        return new UserResponseDTO(donor);
    }

    @Transactional
    public void deactivate(Long id, User client) {
        User donor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        if (!donor.getId().equals(client.getId())) {
            throw new UnauthorizedException();
        }
        donor.setEnabled(false);
        repository.save(donor);
    }
}
