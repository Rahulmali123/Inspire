package com.crm.repo;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);    // for login
    boolean existsByEmail(String email);         // check duplicate
}
