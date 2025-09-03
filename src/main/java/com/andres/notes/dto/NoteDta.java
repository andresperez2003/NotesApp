package com.andres.notes.dto;

import com.andres.notes.persistence.entity.CategoryEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Builder
public class NoteDta {

    private Long id;
    private String name;
    private String description;
    @JsonIgnore
    private UserDto user;
    private CategoryEntity category;

}
