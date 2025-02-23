package com.example.kombat.backend.Error;

public class SyntaxError extends RuntimeException {
    public SyntaxError(String message) {
        super(message);
    }
}