package com.example.kombat.model.parser;

import com.example.kombat.model.AST.*;
import com.example.kombat.model.Error.*;

import java.util.NoSuchElementException;

public class GameTokenizer implements Tokenizer {
    private String input, next;  private int pos;
    public GameTokenizer(String src) {
        this.input = src;  pos = 0;
        computeNext();
    }
    public boolean hasNextToken() {
        return next != null;
    }

    public String leftToken(){
        return input.substring(pos);
    }
    public void checkNextToken() {
        if (!hasNextToken()) throw new
                NoSuchElementException("no more tokens");
    }
    public String peek() {
        checkNextToken();
        return next;
    }
    public boolean peek1(String s) {
        if (!hasNextToken()) return false;
        return peek().equals(s);
    }
    public void consume1(String s) throws SyntaxError {
        if (peek1(s))
            consume();
        else
            throw new SyntaxError(s + " expected");
    }


    public String consume() {
        checkNextToken();
        String result = next;
        computeNext();
        return result;
    }
    private void computeNext(){
        StringBuilder s = new StringBuilder();
        while (pos < input.length() && isSpace(input.charAt(pos))){
            pos++;
        }
        if (pos == input.length()) {
            next = null;
            return;
        }
        char c = input.charAt(pos);

        if (isDigit(c)) {
            s.append(c);
            for (pos++; pos < input.length() && isDigit(input.charAt(pos)); pos++) {
                s.append(input.charAt(pos));
            }
        }else if (isCharacter(c)) {
            s.append(c);
            for(pos++; pos < input.length() && isCharacter(input.charAt(pos)); pos++) {
                s.append(input.charAt(pos));
            }
        } else if (isOparator(c)) {
            s.append(input.charAt(pos));
            pos++;
        } else {
            throw new LexicalError("Unknown character: " + c);
        }
        next = s.toString();
    }
    private boolean isDigit(char c) {
        return Character.isDigit(c);
    }
    private boolean isCharacter(char c) {
        return Character.isLetter(c);
    }
    private boolean isOparator(char c) {
        String match = "+-*/(){}^=%";
        return match.contains(Character.toString(c));
    }
    private boolean isSpace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }
}