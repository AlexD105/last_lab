package com.lab7.api.exception;

public class NoSuchCommandException extends RuntimeException {
    public NoSuchCommandException(String msg) {
        super(msg);
    }
}
