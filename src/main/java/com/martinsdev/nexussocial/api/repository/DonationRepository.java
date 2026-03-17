package com.martinsdev.nexussocial.api.repository;

import com.martinsdev.nexussocial.api.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long> {
}
