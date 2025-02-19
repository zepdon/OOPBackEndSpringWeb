package com.example.kombat.model.Error;

public class LexicalError extends IllegalArgumentException {
    public LexicalError(String message) {
        super(message);
    }
}