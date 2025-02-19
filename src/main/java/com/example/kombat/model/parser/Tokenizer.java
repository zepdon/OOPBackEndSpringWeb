package com.example.kombat.model.parser;

public interface Tokenizer {
    boolean hasNextToken();
    String peek();
    String consume();
    boolean peek1(String s);
    void consume1(String s);
}