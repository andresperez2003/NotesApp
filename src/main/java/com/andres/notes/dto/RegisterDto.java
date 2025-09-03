package com.andres.notes.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RegisterDto {
    private String name;
    private String password;
    private String email;
}
