package com.martinsdev.nexussocial.api.model.user;

import com.martinsdev.nexussocial.api.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //Performs user lookup in the database
    Optional<User> findByRole(UserRole userRole);

    Optional<User> findByEmail(String email);
}
