package com.andres.notes.persistence.repository;

import com.andres.notes.persistence.entity.CodeActivationEntity;
import com.andres.notes.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeActivationRepository extends JpaRepository<CodeActivationEntity, Long> {
    Optional<CodeActivationEntity> findByUser(UserEntity user);
    Optional<CodeActivationEntity> findByCode(String code);
    void deleteByUser(UserEntity user);
}
