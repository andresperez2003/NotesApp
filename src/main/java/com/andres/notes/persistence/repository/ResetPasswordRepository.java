package com.andres.notes.persistence.repository;

import com.andres.notes.persistence.entity.UserEntity;
import com.andres.notes.persistence.entity.ResetPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetPasswordRepository extends JpaRepository<ResetPasswordEntity, Long> {
    Optional<ResetPasswordEntity> findByToken(String token);
    Optional<ResetPasswordEntity> findByUser(UserEntity user);
    void deleteByUser(UserEntity user);
}
