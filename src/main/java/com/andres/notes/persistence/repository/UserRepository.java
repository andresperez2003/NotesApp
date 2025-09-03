package com.andres.notes.persistence.repository;

import com.andres.notes.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    boolean existsByEmail(String email);
    long countByRoleName(String roleName);
    Optional<UserEntity> findByEmail(String email);
}
