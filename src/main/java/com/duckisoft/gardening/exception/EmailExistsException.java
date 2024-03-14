package com.duckisoft.gardening.exception;

public class EmailExistsException extends RuntimeException {

    public EmailExistsException() {
        super("This email address is already registered.");
    }
}
