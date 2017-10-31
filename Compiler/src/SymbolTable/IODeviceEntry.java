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
public class IODeviceEntry extends SymbolTableEntry {
    String name;
    TokenType type;
    
    public IODeviceEntry(String name) {
        super(name);
        this.type = TokenType.FILE;
    }
}
