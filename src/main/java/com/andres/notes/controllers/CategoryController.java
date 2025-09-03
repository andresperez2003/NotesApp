package com.andres.notes.controllers;


import com.andres.notes.dto.CategoryRequestDto;
import com.andres.notes.dto.MessageResponse;
import com.andres.notes.persistence.entity.CategoryEntity;
import com.andres.notes.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping
    public ResponseEntity<MessageResponse> getCategoriesByUser(Principal principal){
        return ResponseEntity.ok().body(
            MessageResponse.builder()
                    .data(categoryService.getCategoriesByUser(principal.getName()))
                    .build()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getCategoryById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(
                MessageResponse.builder()
                        .data(categoryService.findCategoryById(id))
                        .build()
        );
    }



    @PostMapping
    public ResponseEntity<MessageResponse> createCategory(@RequestBody CategoryRequestDto category, Principal principal){
        categoryService.createCategory(category, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                MessageResponse.builder()
                        .message("Category successful created")
                        .build()
        );
    }



    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateCategory(@PathVariable Long id,@RequestBody CategoryEntity category, Principal principal){
        categoryService.updateCategory(category,id,principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(
                MessageResponse.builder()
                        .message("Category successful updated")
                        .build()
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable Long id, Principal principal){
        categoryService.deleteCategory(id, principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(
                MessageResponse.builder()
                        .message("Category successful deleted")
                        .build()
        );
    }
}
