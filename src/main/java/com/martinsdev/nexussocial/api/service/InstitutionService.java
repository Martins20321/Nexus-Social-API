package com.martinsdev.nexussocial.api.service;

import com.martinsdev.nexussocial.api.dto.CreateInstitutionDTO;
import com.martinsdev.nexussocial.api.dto.InstitutionResponseDTO;
import com.martinsdev.nexussocial.api.dto.UpdateInstitutionDTO;
import com.martinsdev.nexussocial.api.dto.ViaCepResponseDTO;
import com.martinsdev.nexussocial.api.infra.client.ViaCepClient;
import com.martinsdev.nexussocial.api.infra.exception.EmailAlreadyExistsException;
import com.martinsdev.nexussocial.api.infra.exception.ResourceNotFoundException;
import com.martinsdev.nexussocial.api.infra.exception.UnauthorizedException;
import com.martinsdev.nexussocial.api.infra.exception.ValidationException;
import com.martinsdev.nexussocial.api.model.Institution;
import com.martinsdev.nexussocial.api.model.Necessity;
import com.martinsdev.nexussocial.api.model.embedded.Address;
import com.martinsdev.nexussocial.api.model.enums.NecessityStatus;
import com.martinsdev.nexussocial.api.model.enums.UserRole;
import com.martinsdev.nexussocial.api.model.user.User;
import com.martinsdev.nexussocial.api.model.user.UserRepository;
import com.martinsdev.nexussocial.api.repository.InstitutionRepository;
import com.martinsdev.nexussocial.api.repository.NecessityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final InstitutionRepository repository;
    private final ViaCepClient viaCepClient;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final NecessityRepository necessityRepository;

    public List<InstitutionResponseDTO> findAll() {
        return repository.findAll().stream().map(InstitutionResponseDTO::new).toList();
    }

    public InstitutionResponseDTO findById(Long id) {
        return repository.findById(id).map(InstitutionResponseDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public InstitutionResponseDTO create(CreateInstitutionDTO dto) {
        boolean alreadyExists = repository.existsByNameOrCnpj(dto.name(), dto.cnpj());

        if (alreadyExists) {
            throw new ValidationException("This institution already exists");
        }
        if (userRepository.existsByEmail(dto.userEmail())) {
            throw new EmailAlreadyExistsException(dto.userEmail());
        }
        User ownerInstitution = User.builder()
                .name(dto.userName())
                .email(dto.userEmail())
                .password(passwordEncoder.encode(dto.userPassword()))
                .role(UserRole.INSTITUTION)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(ownerInstitution);
        ViaCepResponseDTO viaCepResponseDTO = viaCepClient.findAddressByZipCode(dto.zipCode());
        Address address = Address.builder()
                .zipCode(viaCepResponseDTO.zipCode())
                .street(viaCepResponseDTO.street())
                .number(dto.number())
                .city(viaCepResponseDTO.city())
                .state(viaCepResponseDTO.state())
                .build();
        Institution institution = Institution.builder()
                .name(dto.name())
                .cnpj(dto.cnpj())
                .areaOfActivity(dto.areaOfActivity())
                .email(dto.email())
                .phone(dto.phone())
                .createdAt(LocalDateTime.now())
                .address(address)
                .user(ownerInstitution)
                .build();
        repository.save(institution);
        return new InstitutionResponseDTO(institution);
    }

    @Transactional
    public InstitutionResponseDTO update(Long id, UpdateInstitutionDTO dto, User client) {
        Institution institution = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        //Compara o usuário vinculado à institution com o client autenticado
        if (!institution.getUser().getId().equals(client.getId())) {
            throw new UnauthorizedException();
        }
        //Verificando se o phone é o mesmo e se já existe no banco
        if (!institution.getPhone().equals(dto.phone()) && repository.existsByPhone(dto.phone())) {
            throw new PhoneAlreadyExistsException(dto.phone());
        }

        if (dto.phone() != null) institution.setPhone(dto.phone());

        repository.save(institution);
        return new InstitutionResponseDTO(institution);
    }

    @Transactional
    public void deactivate(Long id, User client) {
        Institution institution = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        if (!institution.getUser().getId().equals(client.getId())) {
            throw new UnauthorizedException();
        }

        //Fechando as necessidades que estão abertas
        List<Necessity> openNecessities = findNecessitiesOpenOrInProgress(institution);
        openNecessities.forEach(necessity -> necessity.setStatus(NecessityStatus.CLOSED));
        necessityRepository.saveAll(openNecessities);

        institution.setEnabled(false);
        institution.getUser().setEnabled(false);
        repository.save(institution);
    }

    private List<Necessity> findNecessitiesOpenOrInProgress(Institution institution) {
        return necessityRepository.findByInstitutionAndNecessityStatusIn(institution,
                List.of(NecessityStatus.OPEN, NecessityStatus.IN_PROGRESS));
    }
}
