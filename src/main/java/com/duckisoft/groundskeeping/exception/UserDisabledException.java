package com.duckisoft.groundskeeping.exception;

public class UserDisabledException extends RuntimeException {

    public UserDisabledException() {
        super("Account disabled");
    }
}
