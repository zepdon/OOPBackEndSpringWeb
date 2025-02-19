package com.example.kombat.model.parser;

import com.example.kombat.model.AST.*;

import java.util.List;
public interface Parser {
    List<Node.StateNode> parse() ;
}
