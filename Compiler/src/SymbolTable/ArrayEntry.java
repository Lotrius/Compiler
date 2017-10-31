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
public class ArrayEntry extends SymbolTableEntry {
    String name;
    int address;
    TokenType type;
    int upperBound;
    int lowerBound;
    
    public ArrayEntry(String name, int address, TokenType type, int upperBound, int lowerBound) {
        super(name);
        this.address = address;
        this.type = type;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.setIsArray(true);
    }
}
