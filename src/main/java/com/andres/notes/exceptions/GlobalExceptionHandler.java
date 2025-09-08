package com.andres.notes.exceptions;


import com.andres.notes.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<MessageResponse> handlerEntityAlreadyExistsException(EntityAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(MessageResponse.builder()
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<MessageResponse> handlerEntityNotFoundException(EntityNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(MessageResponse.builder()
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<MessageResponse> handlerSamePasswordException(SamePasswordException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(MessageResponse.builder()
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(UserMismatchException.class)
    public ResponseEntity<MessageResponse> handlerUserMismatchException(UserMismatchException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(MessageResponse.builder()
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(MailServiceException.class)
    public ResponseEntity<MessageResponse> handlerMailServiceException(MailServiceException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(MessageResponse.builder()
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<MessageResponse> handlerInvalidPasswordException(InvalidPasswordException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(MessageResponse.builder()
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class) // fallback para cualquier otro error
    public ResponseEntity<MessageResponse> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(MessageResponse.builder()
                        .message("Unexpected error: " + ex.getMessage())
                        .build());
    }
}
