/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticActions;

import Lexer.Token;
import java.lang.*;
import java.util.*;
import symbolTable.*;
import CompilerError.*;
import Parser.*;

public class SemanticActions {

    private Stack<Object> semanticStack;
//	Quadruples not used until Phase 2
//	private Quadruples quads ;
    private boolean insert;
    private boolean isArray;
    private boolean global;
    private boolean isParam;
    private int globalMemory;
    private int localMemory;
    private SymbolTable globalTable;
    private SymbolTable constantTable;
    private int tableSize = 100;

    public SemanticActions() {
        semanticStack = new Stack<Object>();
//		quads = new Quadruples();
        insert = false;
        isArray = false;
        isParam = false;
        global = true;
        globalMemory = 0;
        localMemory = 0;
        globalTable = new SymbolTable(tableSize);
        constantTable = new SymbolTable(tableSize);
        InstallBuiltins(globalTable);
    }

    public void execute(SemanticAction action, Token token) throws SemanticError {

        int actionNumber = action.getIndex();

//		System.out.println("calling action : " + actionNumber + " with token " + token.getType());

        switch (actionNumber) {

            case 1: {

                // fill in semantic action code here 
            }

        }
    }
}
