package com.example.kombat.backend.Error;

public class EvalError extends RuntimeException {
    public EvalError(String message) {
        super(message);
    }
}