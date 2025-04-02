package org.zafu.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zafu.userservice.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
