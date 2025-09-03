package com.andres.notes.dto;

import com.andres.notes.persistence.entity.NoteEntity;
import com.andres.notes.persistence.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class CategoryRequestDto {

    private Long id;
    private String name;
    private List<NoteEntity> notes;
}
