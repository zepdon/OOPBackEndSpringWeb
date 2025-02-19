package com.example.kombat.model.Error;

public class EvalError extends RuntimeException {
    public EvalError(String message) {
        super(message);
    }
}