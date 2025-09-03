package com.andres.notes.controllers;


import com.andres.notes.dto.MessageResponse;
import com.andres.notes.persistence.entity.RoleEntity;
import com.andres.notes.services.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RolController {


    private final RolService rolService;


    @GetMapping
    public ResponseEntity<MessageResponse> getRoles(){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MessageResponse.builder()
                        .data(rolService.getRoles())
                        .build());
    }



    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getRoleById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(
                MessageResponse.builder()
                        .data(rolService.findRoleById(id))
                        .build()
        );
    }

    // Solo puede acceder el admin
    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public ResponseEntity<MessageResponse> createRole(@RequestBody RoleEntity role){
        rolService.createRol(role);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MessageResponse.builder()
                        .message("Role successful created")
                        .data(role)
                        .build());
    }

    // Solo puede acceder el admin
    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateRole(@PathVariable Long id, @RequestBody RoleEntity role){
        rolService.updateRol(role,id);
        return ResponseEntity.status(HttpStatus.OK).body(
                MessageResponse.builder()
                        .message("Role successful updated")
                        .build()
        );
    }


    // Solo puede acceder el admin
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRole(@PathVariable Long id){
        rolService.deleteRol(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                MessageResponse.builder()
                        .message("Role successful deleted")
                        .build()
        );
    }

}
