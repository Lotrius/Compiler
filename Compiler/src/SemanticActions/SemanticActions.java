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

            case 1:
                insert = true;
                break;
            case 2:
                insert = false;
                break;
            case 3:
                TokenType typ = (TokenType) semanticStack.pop();
                if (isArray) {
                    ConstantEntry ub = semanticStack.pop();
                    ConstantEntry lb = semanticStack.pop();
                    int msize = (ub - lb) + 1;
                    while (!semanticStack.isEmpty()) {
                        Token id = (Token) semanticStack.pop();
                        if (global) {
                            ArrayEntry entry = new ArrayEntry(id.getValue(), globalMemory, typ, ub, lb);
                            entry.setType(typ);
                            entry.upperBound = ub;
                            entry.lowerBound = lb;
                            entry.address = globalMemory;
                            globalTable.insert(entry);
                            globalMemory += msize;
                        } else {
                            ArrayEntry entry = new ArrayEntry(id.getValue(), localMemory, typ, ub, lb);
                            entry.address = localMemory;
                            localMemory += msize;
                        }
                    }
                } else {
                    while (!semanticStack.isEmpty()) {
                        Token id = (Token) semanticStack.pop();
                        if (global) {
                            VariableEntry entry = new VariableEntry(id.getValue(), globalMemory, typ);
                            entry.setType(typ);
                            entry.address = globalMemory;
                            globalTable.insert(entry);
                            globalMemory += 1;
                        } else {
                            VariableEntry entry = new VariableEntry(id.getValue(), localMemory, typ);
                            entry.address = localMemory;
                            localMemory += 1;
                        }
                    }
                }
                isArray = false;
                break;
            case 4:
                semanticStack.push(token.getType());
                break;
            case 6:
                isArray = true;
                break;
            case 7:
                ConstantEntry cons = new ConstantEntry(token.getValue(), token.getType());
                semanticStack.push(cons);
                break;
            case 9:
                Token id1 = (Token) semanticStack.pop();
                IODeviceEntry iod1 = new IODeviceEntry(id1.getValue());
                globalTable.insert(iod1);
                iod1.setIsReserved(true);

                Token id2 = (Token) semanticStack.pop();
                IODeviceEntry iod2 = new IODeviceEntry(id2.getValue());
                globalTable.insert(iod2);
                iod2.setIsReserved(true);

                Token id3 = (Token) semanticStack.pop();
                ProcedureEntry pe = new ProcedureEntry(id3.getValue(), 0, new LinkedList<>());

                insert = false;
                break;

            case 13:
                semanticStack.push(token);
                break;

        }
    }

    public void semanticStackDump() {
        for (Object obj : semanticStack) {
            System.out.println(obj + " ");
        }
    }
}
