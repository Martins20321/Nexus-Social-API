package com.martinsdev.nexussocial.api.repository;

import com.martinsdev.nexussocial.api.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
