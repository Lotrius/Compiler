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
    VariableEntry result;
    
    public FunctionEntry(String name, int numberOfParameters, List parameterInfo, VariableEntry result) {
        super(name);
        this.numberOfParameters = numberOfParameters;
        this.parameterInfo = parameterInfo;
        this.type = TokenType.FUNCTION;
        this.result = result;
        this.setIsFunction(true);
    }
    
    public FunctionEntry(String name) {
        super(name);
    }
    
    public void setResult(VariableEntry r) {
        this.result = r;
    }
    
    public VariableEntry getResult() {
        return this.result;
    }
}
