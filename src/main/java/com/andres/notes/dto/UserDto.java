package com.andres.notes.dto;


import com.andres.notes.persistence.entity.RoleEntity;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private RoleEntity role;
}
