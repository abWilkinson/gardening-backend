package com.duckisoft.gardening.exception;

public class UserDisabledException extends RuntimeException {

    public UserDisabledException() {
        super("Account disabled");
    }
}
