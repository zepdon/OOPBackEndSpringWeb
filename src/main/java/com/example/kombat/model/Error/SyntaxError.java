package com.example.kombat.model.Error;

public class SyntaxError extends RuntimeException {
    public SyntaxError(String message) {
        super(message);
    }
}