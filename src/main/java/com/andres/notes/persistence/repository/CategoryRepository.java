package com.andres.notes.persistence.repository;

import com.andres.notes.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {
    boolean existsByName(String name);
    List<CategoryEntity> findByUserEmail(String email);
}
