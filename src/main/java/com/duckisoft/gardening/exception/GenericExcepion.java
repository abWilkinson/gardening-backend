package com.duckisoft.gardening.exception;

public class GenericExcepion extends RuntimeException {
    public GenericExcepion() {
        super("There was a problem with this request.");
    }
}
