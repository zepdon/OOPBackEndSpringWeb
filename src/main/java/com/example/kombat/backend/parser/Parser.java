package com.example.kombat.backend.parser;
//import AST.*;
import com.example.kombat.backend.AST.*;

import java.util.List;
public interface Parser {
    List<Node.StateNode> parse() ;
}
