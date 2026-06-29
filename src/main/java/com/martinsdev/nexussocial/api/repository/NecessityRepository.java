package com.martinsdev.nexussocial.api.repository;

import com.martinsdev.nexussocial.api.model.Institution;
import com.martinsdev.nexussocial.api.model.Necessity;
import com.martinsdev.nexussocial.api.model.enums.NecessityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NecessityRepository extends JpaRepository<Necessity, Long> {

    boolean existsByTitle(String name);

    List<Necessity> findByInstitutionAndNecessityStatusIn(Institution institution, List<NecessityStatus> statuses);
}
