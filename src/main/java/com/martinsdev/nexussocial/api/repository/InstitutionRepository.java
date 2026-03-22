package com.martinsdev.nexussocial.api.repository;

import com.martinsdev.nexussocial.api.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    boolean existsByNameOrCnpj(String name, String cnpj);
}
