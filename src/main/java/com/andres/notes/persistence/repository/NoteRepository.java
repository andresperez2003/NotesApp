package com.andres.notes.persistence.repository;

import com.andres.notes.persistence.entity.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<NoteEntity, Long> {
    boolean existsByName(String name);
    List<NoteEntity> findByUserEmail(String email);
    Optional<NoteEntity> findByIdAndUserEmail(Long id, String email);
}
