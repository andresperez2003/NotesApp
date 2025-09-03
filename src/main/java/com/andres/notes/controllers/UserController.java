package com.andres.notes.controllers;

import com.andres.notes.dto.MessageResponse;
import com.andres.notes.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Permitir solo al administrador acceder
    @PreAuthorize("hasRole('admin')")
    @GetMapping()
    public ResponseEntity<MessageResponse> getUsers(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageResponse.builder()
                        .data(userService.getUsers())
                        .build());
    }

    @GetMapping("/me")
    public ResponseEntity<MessageResponse> getMe(Principal principal){
        String email = principal.getName();
        return ResponseEntity.status(HttpStatus.OK).body(
                MessageResponse.builder()
                        .data(userService.toDto(userService.getUserByEmail(email)))
                        .build()
        );
    }
}
