package com.martinsdev.nexussocial.api.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {

    //Performs user lookup in the database
    UserDetails findByLogin(String login);
}
