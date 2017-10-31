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
public class ConstantEntry extends SymbolTableEntry {
    String name;
    TokenType type;
    
    public ConstantEntry(String name, TokenType type) {
        super(name);
        this.type = type;
        this.setIsConstant(true);
    }
}
