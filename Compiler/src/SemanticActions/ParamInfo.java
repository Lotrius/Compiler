/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticActions;

import Parser.TokenType;
import SymbolTable.SymbolTableEntry;

/**
 *
 * @author Solomon
 */
public class ParamInfo extends SymbolTableEntry{
    int ub;
    int lb;
    boolean array;
    TokenType type;

    public ParamInfo() {
        ub = 0;
        lb = 0;
        array = false;
        type = null;
    }

    public void setUB(int ub) {
        this.ub = ub;
    }
    
    public void setLB(int lb) {
        this.lb = lb;
    }
    
    public void setArray(boolean array) {
        this.array = array;
    }
    
    public void setType(TokenType type) {
        this.type = type;
    }
    
    
}
