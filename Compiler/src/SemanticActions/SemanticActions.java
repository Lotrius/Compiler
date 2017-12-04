/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticActions;

import Lexer.Token;
import java.lang.*;
import java.util.*;
import SymbolTable.*;
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
        globalTable.installBuiltins();
    }

    public void execute(SemanticAction action, Token token) throws SemanticError, SymbolTableError {

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
                    Token u = (Token) semanticStack.pop();
                    int ub = Integer.parseInt(u.getValue());
                    Token l = (Token) semanticStack.pop();
                    int lb = Integer.parseInt(l.getValue());
                    int msize = (ub - lb) + 1;
                    while (!semanticStack.isEmpty()) {
                        Token id = (Token) semanticStack.pop();
                        if (global) {
                            ArrayEntry entry = new ArrayEntry(id.getValue(), globalMemory, typ, ub, lb);
                            globalTable.insert(entry);
                            globalMemory += msize;
                        } else {
                            ArrayEntry entry = new ArrayEntry(id.getValue(), localMemory, typ, ub, lb);
                            localMemory += msize;
                        }
                    }
                } else {
                    while (!semanticStack.isEmpty()) {
                        Token id = (Token) semanticStack.pop();
                        if (global) {
                            VariableEntry entry = new VariableEntry(id.getValue(), globalMemory, typ);
                            globalTable.insert(entry);
                            globalMemory += 1;
                        } else {
                            VariableEntry entry = new VariableEntry(id.getValue(), localMemory, typ);
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
                SymbolTableEntry idsec = (SymbolTableEntry) semanticStack.pop();
                SymbolTableEntry offset = (SymbolTableEntry) semanticStack.pop();
                SymbolTableEntry idfir = (SymbolTableEntry) semanticStack.pop();

                if (typecheck(idfir, idsec) == 3) {
                    throw SemanticError.mismatch(idfir.getName(), idsec.getName());
                }
                if (typecheck(idfir, idsec) == 2) {
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
                SymbolTableEntry idsecc = (SymbolTableEntry) semanticStack.pop();
                SymbolTableEntry idfirr = (SymbolTableEntry) semanticStack.pop();
                if (typecheck(idfirr, idsecc) == 0) {
                    SymbolTableEntry $$TEMP = create("TEMP", TokenType.INTEGER);
                    gen(token.getType().toString(), idfirr, idsecc, $$TEMP);
                    semanticStack.push($$TEMP);
                }

                if (typecheck(idfirr, idsecc) == 1) {
                    SymbolTableEntry $$TEMP = create("TEMP", TokenType.REAL);
                    gen("f" + token.getType(), idfirr, idsecc, $$TEMP);
                    semanticStack.push($$TEMP);
                }

                if (typecheck(idfirr, idsecc) == 2) {
                    SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.REAL);
                    gen("ltof", idsecc, $$TEMP1);
                    SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.REAL);
                    gen("f" + token.getType(), idfirr, $$TEMP1, $$TEMP2);
                    semanticStack.push($$TEMP2);
                }

                if (typecheck(idfirr, idsecc) == 3) {
                    SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.REAL);
                    gen("ltof", idfirr, $$TEMP1);
                    SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.REAL);
                    gen("f" + token.getType(), $$TEMP1, idsecc, $$TEMP2);
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
                SymbolTableEntry idseccc = (SymbolTableEntry) semanticStack.pop();
                Token op = (Token) semanticStack.pop();
                SymbolTableEntry idfirrr = (SymbolTableEntry) semanticStack.pop();

                if ((typecheck(idfirrr, idseccc) != 0) && (op.getType().equals("mod"))) {
                    throw SemanticError.modError(idfirrr.getType().toString(), idseccc.getType().toString());
                }

                if ((typecheck(idfirrr, idseccc)) == 0) {
                    if (op.getType().equals("mod")) {
                        SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.INTEGER);
                        gen("mov", idfirrr, $$TEMP1);
                        SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.INTEGER);
                        gen("move", $$TEMP1, $$TEMP2);
                        gen("sub", $$TEMP2, idseccc, $$TEMP1);
                        gen("bge", $$TEMP1, idseccc, quadruple.getNextQuad() - 2);
                        semanticStack.push($$TEMP2);
                    } else if (op.getType().equals("/")) {
                        SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.REAL);
                        gen("ltof", idfirrr, $$TEMP1);
                        SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.REAL);
                        gen("ltof", idseccc, $$TEMP2);
                        SymbolTableEntry $$TEMP3 = create("TEMP3", TokenType.REAL);
                        gen("fdiv", $$TEMP1, $$TEMP2, $$TEMP3);
                        semanticStack.push($$TEMP2);
                    } else {
                        SymbolTableEntry $$TEMP = create("TEMP1", TokenType.INTEGER);
                        gen(token.getType().toString(), idfirrr, idseccc, $$TEMP);
                        semanticStack.push($$TEMP);
                    }
                }

                if ((typecheck(idfirrr, idseccc)) == 1) {
                    if (op.getType().equals("div")) {
                        SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.INTEGER);
                        gen("ftol", idfirrr, $$TEMP1);
                        SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.INTEGER);
                        gen("ftol", idseccc, $$TEMP2);
                        SymbolTableEntry $$TEMP3 = create("TEMP3", TokenType.INTEGER);
                        gen("div", $$TEMP1, $$TEMP2, $$TEMP3);
                        semanticStack.push($$TEMP2);
                    } else {
                        SymbolTableEntry $$TEMP = create("TEMP", TokenType.REAL);
                        gen("f" + token.getType(), idfirrr, idseccc, $$TEMP);
                        semanticStack.push($$TEMP);
                    }
                }

                if ((typecheck(idfirrr, idseccc)) == 2) {
                    if (op.getType().equals("div")) {
                        SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.INTEGER);
                        gen("ftol", idfirrr, $$TEMP1);
                        SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.INTEGER);
                        gen("div", $$TEMP1, idseccc, $$TEMP2);
                        semanticStack.push($$TEMP2);
                    } else {
                        SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.REAL);
                        gen("ltof" + token.getType(), idseccc, $$TEMP1);
                        SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.REAL);
                        gen("f" + token.getType(), idfirrr, $$TEMP1, $$TEMP2);
                        semanticStack.push($$TEMP2);
                    }
                }

                if ((typecheck(idfirrr, idseccc)) == 3) {
                    if (op.getType().equals("div")) {
                        SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.INTEGER);
                        gen("ftol", idseccc, $$TEMP1);
                        SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.INTEGER);
                        gen("div", idfirrr, $$TEMP1, $$TEMP2);
                        semanticStack.push($$TEMP2);
                    } else {
                        SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.REAL);
                        gen("ltof" + token.getType(), idfirrr, $$TEMP1);
                        SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.REAL);
                        gen("f" + token.getType(), $$TEMP1, idseccc, $$TEMP2);
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
                SymbolTableEntry offset2 = (SymbolTableEntry) semanticStack.pop();
                SymbolTableEntry id4 = (SymbolTableEntry) semanticStack.pop();
                SymbolTableEntry eType = (SymbolTableEntry) semanticStack.pop();
                if (offset2 == null) {
                    SymbolTableEntry $$TEMP = create("TEMP", id4.getType());
                    gen("load", id4, offset2, $$TEMP);
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

    public VariableEntry create(String name, TokenType type) throws SymbolTableError {
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
    
    //45
    private void gen(String tviCode, SymbolTableEntry op1, int in) {
        String[] quad = {tviCode, op1, in, null};
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
    
        public int typecheck(SymbolTableEntry id1, SymbolTableEntry id2) {
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
