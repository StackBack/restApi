package com.example.restapi.repository;

import com.example.restapi.domain.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByName(@Param("name") String name);

    Optional<CustomUser> findByEmail(@Param("email") String email);
}
