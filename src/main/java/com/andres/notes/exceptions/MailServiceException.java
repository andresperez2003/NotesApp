package com.andres.notes.exceptions;

public class MailServiceException extends RuntimeException {
    public MailServiceException(String message) {
        super(message);
    }
}
