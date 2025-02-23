package com.example.kombat.backend.Error;

public class LexicalError extends IllegalArgumentException {
    public LexicalError(String message) {
        super(message);
    }
}