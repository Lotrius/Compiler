/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lexer;

import Parser.TokenType;
import SymbolTable.ConstantEntry;
import java.util.StringJoiner;

/**
 *
 * @author Solomon
 */
public class Token {

    public enum OperatorType {
        EQUAL, NOTEQUAL, LESSTHAN, LESSTHANOREQUAL, GREATERTHAN, GREATERTHANOREQUAL, ADD,
        SUBTRACT, MULTIPLY, DIVIDE, INTEGERDIVIDE, MOD, AND, OR, NOT
    }

    private TokenType type;
    private String value;
    private OperatorType opType;
    private ConstantEntry entry;

    public Token() {
        super();
    }

    public Token(TokenType type, String value, OperatorType op) {
        this.type = type;
        this.value = value;
        this.opType = op;
    }

    public void setType(TokenType t) {
        type = t;
    }

    public void setValue(String s) {
        value = s;
    }

    public void setOpType(OperatorType op) {
        opType = op;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public OperatorType getOpType() {
        return opType;
    }

    public void clear() {
        type = null;
        value = null;
        opType = null;
    }

    public void copy(Token t) {
        this.type = t.getType();
        this.value = t.getValue();
    }

    public void print() {
        System.out.print("TOKEN : Type: " + this.type);
        if (this.value != null) {
            System.out.println(" Value : " + this.value);
        } else if (this.opType != null) {
            System.out.println(" OpType : " + this.opType);
        }
        System.out.println();
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(type.toString());
        joiner.add(value);
        if (opType != null) {
            joiner.add(opType.toString());
        }
        return joiner.toString();
    }

    public ConstantEntry getEntry() {
        return entry;
    }

    public void setEntry(ConstantEntry entry) {
        this.entry = entry;
    }

}
