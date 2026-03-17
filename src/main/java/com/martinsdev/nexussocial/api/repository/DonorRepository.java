package com.martinsdev.nexussocial.api.repository;

import com.martinsdev.nexussocial.api.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorRepository extends JpaRepository<Donor, Long> {
}
