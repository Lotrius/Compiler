/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SymbolTable;
import java.util.List;
/**
 *
 * @author Solomon
 */
public class ProcedureEntry extends SymbolTableEntry {
    
    String name;
    int numberOfParameters;
    List paramterInfo;
    public ProcedureEntry(String name, int numberOfParamters, List parameterInfo) {
        this.name = name;
        this.numberOfParameters = numberOfParamters;
        this.paramterInfo = parameterInfo;
    }
}
