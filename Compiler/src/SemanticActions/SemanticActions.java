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
import static Parser.TokenType.*;

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
    private SymbolTable localTable;
    private SymbolTable constantTable;
    private int tableSize = 100;
    private Quadruple quadruple;
    private int GLOBAL_STORE = 0;

    public enum etype {
        ARITHMETIC, RELATIONAL
    };

    public SemanticActions() {
        semanticStack = new Stack<Object>();
//		quads = new Quadruples();
        insert = true;
        isArray = false;
        isParam = false;
        global = true;
        globalMemory = 0;
        localMemory = 0;
        globalTable = new SymbolTable(tableSize);
        localTable = new SymbolTable(tableSize);
        constantTable = new SymbolTable(tableSize);
        installBuiltins(globalTable);
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
            case 30:
                String name = token.getValue();
                SymbolTableEntry id;
                if (global) {
                    id = globalTable.lookup(name);
                } else {
                    id = localTable.lookup(name);
                }

                if (id == null) {
                    throw SemanticError.undeclaredVariable(name);
                }

                semanticStack.push(id);
                break;
            case 31:
                SymbolTableEntry idsec = semanticStack.pop();
                SymbolTableEntry offset = semanticStack.pop();
                SymbolTableEntry idfir = semanticStack.pop();

                if (typcheck(idfir, idsec) == 3) {
                    throw SemanticError.mismatch(idfir, idsec);
                }
                if (typcheck(idfir, idsec) == 2) {
                    SymbolTableEntry $$TEMP = create("TEMP", TokenType.REAL);
                    gen("ltof", idsec, $$TEMP);
                    if (offset == null) {
                        gen("move", idsec, idfir);
                    } else {
                        gen("store", $$TEMP, offset, idfir);
                    }
                } else if (offset == null) {
                    gen("move", idsec, idfir);
                } else {
                    gen("store", idsec, offset, idfir);
                }
                break;
            case 40:
                semanticStack.push(token);
                break;
            case 42:
                semanticStack.push(token);
                break;
            case 43:
                SymbolTableEntry idsec = semanticStack.pop();
                SymbolTableEntry idfir = semanticStack.pop();
                if (typecheck(idfir, idsec) == 0) {
                    SymbolTableEntry $$TEMP = create("TEMP", TokenType.INTEGER);
                    gen(token.getType(), idfir, idsec, $$TEMP);
                    semanticStack.push($$TEMP);
                }

                if (typecheck(idfir, idsec) == 1) {
                    SymbolTableEntry $$TEMP = create("TEMP", TokenType.REAL);
                    gen("f" + token.getType(), idfir, idsec, $$TEMP);
                    semanticStack.push($$TEMP);
                }

                if (typecheck(idfir, idsec) == 2) {
                    SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.REAL);
                    gen("ltof", idsec, $$TEMP1);
                    SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.REAL);
                    gen("f" + token.getType(), idfir, $$TEMP1, $$TEMP2);
                    semanticStack.push($$TEMP2);
                }

                if (typecheck(idfir, idsec) == 3) {
                    SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.REAL);
                    gen("ltof", idfir, $$TEMP1);
                    SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.REAL);
                    gen("f" + token.getType(), $$TEMP1, idsec, $$TEMP2);
                    semanticStack.push($$TEMP2);
                }
                break;
            case 44:
                semanticStack.push(token);
                break;
            case 45:
                etype et = (etype) semanticStack.pop();
                if (et != etype.ARITHMETIC) {
                    throw SemanticError.eTypeMismatch(et);
                }
                SymbolTableEntry idsec = semanticStack.pop();
                Token op = (Token) semanticStack.pop();
                SymbolTableEntry idfir = semanticStack.pop();

                if ((typecheck(idfir, idsec) != 0) && (op.getType().equals("mod"))) {
                    throw SemanticError.modError(idfir.getType(), idsec.getType());
                }

                if ((typecheck(idfir, idsec)) == 0) {
                    if (op.getType().equals("mod")) {
                        SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.INTEGER);
                        gen("mov", idfir, $$TEMP1);
                        SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.INTEGER);
                        gen("move", $$TEMP1, $$TEMP2);
                        gen("sub", $$TEMP2, idsec, $$TEMP1);
                        gen("bge", $$TEMP1, idsec, quadruple.getNextQuad() - 2);
                        semanticStack.push($$TEMP2);
                    } else if (op.getType().equals("/")) {
                        SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.REAL);
                        gen("ltof", idfir, $$TEMP1);
                        SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.REAL);
                        gen("ltof", idsec, $$TEMP2);
                        SymbolTableEntry $$TEMP3 = create("TEMP3", TokenType.REAL);
                        gen("fdiv", $$TEMP1, $$TEMP2, $$TEMP3);
                        semanticStack.push($$TEMP2);
                    } else {
                        SymbolTableEntry $$TEMP = create("TEMP1", TokenType.INTEGER);
                        gen(token.getType(), idfir, idsec, $$TEMP);
                        semanticStack.push($$TEMP);
                    }
                }

                if ((typecheck(idfir, idsec)) == 1) {
                    if (op.getType().equals("div")) {
                        SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.INTEGER);
                        gen("ftol", idfir, $$TEMP1);
                        SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.INTEGER);
                        gen("ftol", idsec, $$TEMP2);
                        SymbolTableEntry $$TEMP3 = create("TEMP3", TokenType.INTEGER);
                        gen("div", $$TEMP1, $$TEMP2, $$TEMP3);
                        semanticStack.push($$TEMP2);
                    } else {
                        SymbolTableEntry $$TEMP = create("TEMP", TokenType.REAL);
                        gen("f" + token.getType(), idfir, idsec, $$TEMP);
                        semanticStack.push($$TEMP);
                    }
                }

                if ((typecheck(idfir, idsec)) == 2) {
                    if (op.getType().equals("div")) {
                        SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.INTEGER);
                        gen("ftol", idfir, $$TEMP1);
                        SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.INTEGER);
                        gen("div", $$TEMP1, idsec, $$TEMP2);
                        semanticStack.push($$TEMP2);
                    } else {
                        SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.REAL);
                        gen("ltof" + token.getType(), idsec, $$TEMP1);
                        SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.REAL);
                        gen("f" + token.getType(), idfir, $$TEMP1, $$TEMP2);
                        semanticStack.push($$TEMP2);
                    }
                }

                if ((typecheck(idfir, idsec)) == 3) {
                    if (op.getType().equals("div")) {
                        SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.INTEGER);
                        gen("ftol", idsec, $$TEMP1);
                        SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.INTEGER);
                        gen("div", idfir, $$TEMP1, $$TEMP2);
                        semanticStack.push($$TEMP2);
                    } else {
                        SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.REAL);
                        gen("ltof" + token.getType(), idfir, $$TEMP1);
                        SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.REAL);
                        gen("f" + token.getType(), $$TEMP1, idsec, $$TEMP2);
                        semanticStack.push($$TEMP2);
                    }
                }
                break;
            case 46:
                if (token.getType() == TokenType.IDENTIFIER) {
                    if (global) {
                        id = globalTable.lookup(token.getValue());
                    } else {
                        id = localTable.lookup(token.getValue());
                    }
                    if (id == null) {
                        throw SemanticError.undeclaredVariable(token.getValue());
                    } else {
                        semanticStack.push(id);
                    }
                }

                if ((token.getType() == TokenType.INTCONSTANT)
                        || (token.getType() == TokenType.REALCONSTANT)) {
                    id = constantTable.lookup(token.getValue());
                    ConstantEntry entry;
                    if (id == null) {
                        if (token.getType() == TokenType.INTCONSTANT) {
                            entry = new ConstantEntry(token.getValue(), TokenType.INTEGER);
                        } else {
                            entry = new ConstantEntry(token.getValue(), TokenType.REAL);
                        }
                        constantTable.insert(entry);
                        semanticStack.push(entry);
                    }
                }
                break;
            case 48:
                SymbolTableEntry offset = semanticStack.pop();
                SymbolTableEntry id = semanticStack.pop();
                SymbolTableEntry eType = semanticStack.pop();
                if (offset == null) {
                    SymbolTableEntry $$TEMP = create("TEMP", id.getType());
                    gen("load", id, offset, $$TEMP);
                    semanticStack.push($$TEMP);
                }
                break;
            case 55:
                backpatch(GLOBAL_STORE, globalMemory);
                gen("free", globalMemory);
                gen("PROCEND");
                break;
            case 56:
                gen("PROCBEGIN", "main");
                GLOBAL_STORE = quadruple.getNextQuad();
                gen("alloc", "_");
                break;

        }
    }

    public VariableEntry create(String name, TokenType type) {
        VariableEntry $$NAME = new VariableEntry(name, -globalMemory, type);
        globalTable.insert($$NAME);
        globalMemory++;
        return $$NAME;
    }

    public void gen(String tviCode) {
        String[] quad = {tviCode, null, null, null};
        quadruple.addQuad(quad);
    }

    public void gen(String tviCode, int operand1) {
        String[] quad = {tviCode, String.valueOf(operand1), null, null};
        quadruple.addQuad(quad);
    }

    private void gen(String tviCode, String op) {
        String[] quad = {tviCode, op, null, null};
        quadruple.addQuad(quad);
    }

    //31
    private void gen(String tviCode, SymbolTableEntry op1, SymbolTableEntry op2) {
        String[] quad = {tviCode, op1, op2, null};
        quadruple.addQuad(quad);
    }

    private void gen(String tviCode, SymbolTableEntry op1, SymbolTableEntry op2, SymbolTableEntry op3) {
        String[] quad = {tviCode, op1, op2, op3};
        quadruple.addQuad(quad);
    }

    public int typecheck(Token id1, Token id2) {
        if (id1.getType().equals(INTEGER) && id2.getType().equals(INTEGER)) {
            return 0;
        } else if (id1.getType().equals(REAL) && id2.getType().equals(REAL)) {
            return 1;
        } else if (id1.getType().equals(REAL) && id2.getType().equals(INTEGER)) {
            return 2;
        } else {
            return 3;
        }
    }

    public void backpatch(int p, int i) {
        quadruple.setField(i, 1, Integer.toString(p));
    }

    public void semanticStackDump() {
        for (Object obj : semanticStack) {
            System.out.println(obj + " ");
        }
    }
}
