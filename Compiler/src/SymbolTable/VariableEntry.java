/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SymbolTable;

import Parser.TokenType;

/**
 *
 * @author Solomon
 */
public class VariableEntry extends SymbolTableEntry {

    String name;
    int address;
    TokenType type;

    public VariableEntry(String name, int address, TokenType type) {
        super(name);
        this.address = address;
        this.type = type;
        this.setIsVariable(true);
    }
}
