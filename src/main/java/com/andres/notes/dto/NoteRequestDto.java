package com.andres.notes.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class NoteRequestDto {
    private Long id;
    private String name;
    private String description;
    private Long category;
}
