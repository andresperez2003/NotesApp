package com.andres.notes.controllers;



import com.andres.notes.dto.NoteRequestDto;
import com.andres.notes.dto.MessageResponse;
import com.andres.notes.persistence.entity.CategoryEntity;
import com.andres.notes.persistence.entity.NoteEntity;
import com.andres.notes.persistence.entity.UserEntity;
import com.andres.notes.services.CategoryService;
import com.andres.notes.services.NoteService;
import com.andres.notes.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    private final CategoryService categoryService;

    private final UserService userService;


    @GetMapping
    public ResponseEntity<MessageResponse> getNotes(Principal principal){
        return ResponseEntity.ok().body(
                MessageResponse.builder()
                        .data(noteService.getNotes(principal.getName()))
                        .build()
        );
    }



    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getNoteById(@PathVariable Long id, Principal principal){
        return ResponseEntity.status(HttpStatus.OK).body(
                MessageResponse.builder()
                        .data(noteService.findNoteByIdAndUser(id, principal.getName()))
                        .build()
        );
    }


    @PostMapping
    public ResponseEntity<MessageResponse> createNotes(@RequestBody NoteRequestDto note, Principal principal){
        CategoryEntity category = categoryService.findCategoryById(note.getCategory());
        UserEntity user = userService.findUserByEmail(principal.getName());
        NoteEntity newNote = NoteEntity.builder().user(user).category(category).description(note.getDescription()).name(note.getName()).build();
        noteService.createNotes(newNote, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                MessageResponse.builder()
                        .message("Note successful created")
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteNote(@PathVariable Long id, Principal principal){
        noteService.deleteNote(id, principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(
                MessageResponse.builder()
                        .message("Note successful deleted")
                        .build()
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateNote(@PathVariable Long id, @RequestBody NoteRequestDto note, Principal principal){
        noteService.updateNote(note,id, principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(
                MessageResponse.builder()
                        .message("Note successful updated")
                        .build()
        );
    }

}
