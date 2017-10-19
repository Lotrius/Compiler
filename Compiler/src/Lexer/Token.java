/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lexer;

import Parser.TokenType;

/**
 *
 * @author Solomon
 */
public class Token {

    private String value;
    private TokenType type;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public Token() {
        super();
    }

    public TokenType getType() {
        return this.type;
    }

    public void setType(TokenType t) {
        this.type = t;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String v) {
        this.value = v;
    }

    public String toString() {
        return "<" + this.type + ", " + this.value + ">";
    }
}
