/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SymbolTable;

import Parser.TokenType;
import java.util.List;

/**
 *
 * @author Solomon
 */
public class FunctionEntry extends SymbolTableEntry{
    String name;
    int numberOfParameters;
    List parameterInfo;
    TokenType type;
    
    public FunctionEntry(String name, int numberOfParameters, List parameterInfo) {
        super(name);
        this.numberOfParameters = numberOfParameters;
        this.parameterInfo = parameterInfo;
        this.type = TokenType.FUNCTION;
        this.setIsFunction(true);
    }
}
