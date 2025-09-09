package com.andres.notes.services;


import com.andres.notes.dto.NoteDta;
import com.andres.notes.dto.NoteRequestDto;
import com.andres.notes.exceptions.EntityAlreadyExistsException;
import com.andres.notes.exceptions.EntityNotFoundException;
import com.andres.notes.exceptions.UserMismatchException;
import com.andres.notes.persistence.entity.NoteEntity;
import com.andres.notes.persistence.entity.UserEntity;
import com.andres.notes.persistence.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    private final UserService userService;

    private final CategoryService categoryService;

    public NoteDta toDto(NoteEntity note){
        return NoteDta.builder()
                .name(note.getName())
                .description(note.getDescription())
                .category(note.getCategory())
                .id(note.getId())
                .user(userService.toDto(note.getUser()))
                .build();
    }

    public NoteEntity findNoteById(Long id){
        return noteRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Note with id " + id + " not found")
        );
    }

    public NoteEntity findNoteByIdAndUser(Long id, String email){
        return noteRepository.findByIdAndUserEmail(id, email).orElseThrow(
                ()-> new EntityNotFoundException("Note with id " + id + " not found")
        );
    }

    public List<NoteDta> getNotes(String email){
        return noteRepository.findByUserEmail(email).stream().map(this::toDto).collect(Collectors.toList());
    }


    public void createNotes(NoteEntity note, String email){
        UserEntity user = userService.getUserByEmail(email);
        if(noteRepository.existsByNameAndUser(note.getName(), user)){
            throw  new EntityAlreadyExistsException("Note with name " + note.getName()+ " already exists");
        }
        note.setUser(user);
        noteRepository.save(note);
    }

    public void deleteNote(Long id, String email){
        UserEntity user = userService.getUserByEmail(email);
        NoteEntity note = findNoteById(id);
        if(!note.getUser().getId().equals(user.getId())){
            throw new UserMismatchException("The provided user does not match the authenticated user");
        }
        noteRepository.delete(note);
    }

    public void updateNote(NoteRequestDto note, Long id, String email){
        UserEntity user = userService.getUserByEmail(email);
        NoteEntity findNote = findNoteById(id);

        if(!findNote.getUser().getId().equals(user.getId())){
            throw new UserMismatchException("The provided user does not match the authenticated user");
        }
        if(note.getName() != null){
            findNote.setName(note.getName());
        }
        if(note.getDescription() != null){
            findNote.setName(note.getDescription());
        }
        if(note.getCategory()!= null){
            findNote.setCategory(categoryService.findCategoryById(note.getCategory()));
        }
        if(note.getCategory()!= null){
            findNote.setUser(userService.findUserByEmail(email));
        }
        noteRepository.save(findNote);
    }


}
