package com.example.kombat.backend.parser;

//import AST.*;
//import AST.Node;
//import Error.*;
//import GameState.Direction;
import com.example.kombat.backend.AST.*;
import com.example.kombat.backend.Error.*;
import com.example.kombat.backend.Error.*;
import com.example.kombat.backend.GameState.Direction;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class GameParser implements Parser {
    private final GameTokenizer tokenizer;
    HashSet<String> word = new HashSet<String>();
    public GameParser(GameTokenizer tokenizer) {
        this.tokenizer = tokenizer;
        word.add("ally");
        word.add("done");
        word.add("down");
        word.add("downleft");
        word.add("downright");
        word.add("else");
        word.add("if");
        word.add("move");
        word.add("nearby");
        word.add("opponent");
        word.add("shoot");
        word.add("then");
        word.add("up");
        word.add("upleft");
        word.add("upright");
        word.add("while");
    }

    @Override
    public List<Node.StateNode> parse() throws SyntaxError {
        List<Node.StateNode> result = parseStrategy();
        if(tokenizer.hasNextToken()){
            throw new SyntaxError("Leftover token " + tokenizer.leftToken() + " next is " + tokenizer.peek());
        }
        return result;
    }

    //Strategy → Statement+
    private List<Node.StateNode> parseStrategy()throws SyntaxError {
        List<Node.StateNode> action = new LinkedList<>();
        action.add(parseStatement());
        while (tokenizer.hasNextToken() && !tokenizer.peek1("}")) {
            action.add(parseStatement());
        }
        return action;
    }
    //Statement → Command | BlockStatement | IfStatement | WhileStatement
    private Node.StateNode parseStatement()throws SyntaxError {
        if(tokenizer.peek1("{")){
            return parseBlockStatement();
        }else if(tokenizer.peek1("if")){
            return  parseIfStatement();
        }else if(tokenizer.peek1("while")){
            return parseWhileStatement();
        }else{
            return parseCommand();
        }
    }
    //Command → AssignmentStatement | ActionCommand
    private Node.StateNode parseCommand()throws SyntaxError {
        // you can add function here
        boolean check = false;
        for(String i : word){
            if (word.contains(tokenizer.peek())) {
                check = true;
            }
        }
        if(check){
            return parseActionCommand();
        }else {
            return parseAssignmentStatement();
        }
    }
    //AssignmentStatement → <identifier> = Expression
    private Node.StateNode parseAssignmentStatement()throws SyntaxError {
        String identifier = tokenizer.consume();
        tokenizer.consume1("=");
        Node.Expr expr = parseExpression();
        return new AssignmentStatementNode(identifier,expr);//ast ast ที่ เอา expression ยัดใส่ใน identifier ใช้ map
    }
    //ActionCommand → done | MoveCommand | AttackCommand
    private Node.StateNode parseActionCommand()throws SyntaxError {
        if(tokenizer.peek1("done")){
            tokenizer.consume();
            return new DoneNode();
//            System.out.println("done"); //add function done here
        }else if(tokenizer.peek1("move")){
            tokenizer.consume();
            return parseMoveCommand();
        }else if (tokenizer.peek1("shoot")){
            tokenizer.consume();
            return parseAttackCommand();
        }else {
            throw new SyntaxError("Unknown "+ tokenizer.consume() +" command");
        }
    }
    //MoveCommand → move parser.Direction
    private Node.StateNode parseMoveCommand()throws SyntaxError {
//        System.out.println("move to "+ );
        return new MoveCommandNode(parseDirection()); //ast move + direction
    }
    //AttackCommand → shoot parser.Direction Expression
    private Node.StateNode parseAttackCommand() throws SyntaxError {
//        System.out.println("shoot to "+  + "deal " + );
        return new AttackCommandNode(parseDirection(),parseExpression()); //ast shoot + direction + expression
    }
    //parser.Direction → up | down | upleft | upright | downleft | downright
    private Direction parseDirection() throws SyntaxError {
        String direction = tokenizer.consume();
        return switch (direction){
            case "up" -> Direction.up;
            case "down" -> Direction.down;
            case "upleft" -> Direction.upleft;
            case "upright" -> Direction.upright;
            case "downleft" -> Direction.downleft;
            case "downright" -> Direction.downright;
            default -> throw new SyntaxError("Unknown direction");
        };
    }
    //BlockStatement → { Statement* }
    private Node.StateNode parseBlockStatement()throws SyntaxError {
        List<Node.StateNode> block = new LinkedList<>();
        tokenizer.consume1("{");
        while (tokenizer.hasNextToken() && !tokenizer.peek1("}")) {
            block.add(parseStatement());
        }
        tokenizer.consume1("}");
        return new BlockStatementNode(block);//ast ของ block ด้านบน
    }
    //IfStatement → if ( Expression ) then Statement else Statement
    private Node.StateNode parseIfStatement()throws SyntaxError {
        tokenizer.consume1("if");
        tokenizer.consume1("(");
        Node.Expr a = parseExpression();
        tokenizer.consume1(")");
        tokenizer.consume1("then");
        Node.StateNode b = parseStatement();
        tokenizer.consume1("else");
        Node.StateNode c = parseStatement();
        return new IfStatementNode(a,b,c);// ast ของ if เอา expression มาคิด แล้วดูว่าจะทำ then or else
    }
    //WhileStatement → while ( Expression ) Statement
    private Node.StateNode parseWhileStatement()throws SyntaxError {
        tokenizer.consume1("while");
        tokenizer.consume1("(");
        Node.Expr a = parseExpression();
        tokenizer.consume1(")");
        Node.StateNode b = parseStatement();
        return new WhileStatementNode(a,b);// ast ของ รับค่า expression มาคำนวณ แล้วก็ทำ statement
    }
    //Expression → Expression + Term | Expression - Term | Term
    private Node.Expr parseExpression() throws SyntaxError {
        Node.Expr v = parseTerm();
        while (tokenizer.peek1("+") || tokenizer.peek1("-")){
            String operator = tokenizer.consume();
            v = new BinaryArithExpr(v,operator,parseTerm());
        }
        return v;
    }
    //Term → Term * Factor | Term / Factor | Term % Factor | Factor
    private Node.Expr parseTerm() throws SyntaxError {
        Node.Expr v = parseFactor();
        while (tokenizer.peek1("*") || tokenizer.peek1("/")|| tokenizer.peek1("%")){
            String operator = tokenizer.consume();
            v = new BinaryArithExpr(v,operator,parseFactor());
        }
        return v;
    }
    //Factor → Power ^ Factor | Power
    private Node.Expr parseFactor() throws SyntaxError {
        Node.Expr v = parsePower();
        while (tokenizer.peek1("^")){
            String operator = tokenizer.consume();
            v = new BinaryArithExpr(v,operator,parsePower());
        }
        return v;
    }
    //Power → <number> | <identifier> | ( Expression ) | InfoExpression
    private Node.Expr parsePower() throws SyntaxError {
        if(Character.isDigit(tokenizer.peek().charAt(0))){
            return new NumberNode(Long.parseLong(tokenizer.consume()));
        }else if(tokenizer.peek1("(")){
            tokenizer.consume1("(");
            Node.Expr v = parseExpression();
            tokenizer.consume1(")");
            return v;
        }else if(tokenizer.peek1("ally") || tokenizer.peek1("opponent") || tokenizer.peek1("nearby")){
            return parseInfoExpression();
        }else if(Character.isAlphabetic(tokenizer.peek().charAt(0))){
            String o = tokenizer.consume();
            return new IdentifierNode(o);
            //System.out.println(o);
            //ast ที่ return ค่าของ identifier เป็น ตัวเลข
        }
        return null;
    }
    //InfoExpression → ally | opponent | nearby parser.Direction
    private Node.Expr parseInfoExpression() throws SyntaxError {
        if(tokenizer.peek1("ally")){
            tokenizer.consume();
            return new AllyNode();
//            System.out.println("ally");
            //ast เรียก function
        }else if(tokenizer.peek1("opponent")){
            tokenizer.consume();
//            System.out.println("opponent");
            return new OpponentNode();//ast เรียก function
        }else if(tokenizer.peek1("nearby")){
            tokenizer.consume();
            Direction direction = parseDirection();
//            System.out.println("nearby" + direction);
            return new NearbyNode(direction);//ast เรียก function
        }else
            throw new SyntaxError("Unknown expression");
    }
}