/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SymbolTable;

import CompilerError.SymbolTableError;
import Parser.TokenType;
import java.util.Collections;
import java.util.Hashtable;

/**
 *
 * @author Solomon
 */
public class SymbolTable {

    private Hashtable<String, SymbolTableEntry> ht;

    public SymbolTable(int size) {
        ht = new Hashtable(size);
    }

    public SymbolTableEntry lookup(String entry) {
        return ht.get(entry);
    }

    public void insert(SymbolTableEntry entry) throws SymbolTableError {
        if (lookup(entry.getName()) == null) {
            ht.put(entry.getName(), entry);
        } else {
            throw SymbolTableError.multipleEntry(entry.getName());
        }
    }

    public int size() {
        return ht.size();
    }

    public void dumpTable() {
        System.out.println(ht);
    }
    
    public void delete() {
        ht.clear();
    }

    public void installBuiltins() {
        SymbolTableEntry main = new ProcedureEntry("MAIN", 0, Collections.emptyList());
        SymbolTableEntry read = new ProcedureEntry("READ", 0, Collections.emptyList());
        SymbolTableEntry write = new ProcedureEntry("WRITE", 0, Collections.emptyList());

        SymbolTableEntry input = new IODeviceEntry("INPUT");
        SymbolTableEntry output = new IODeviceEntry("OUTPUT");

        main.setIsReserved(true);
        read.setIsReserved(true);
        write.setIsReserved(true);
        input.setIsReserved(true);
        output.setIsReserved(true);

        ht.put(main.getName(), main);
        ht.put(read.getName(), read);
        ht.put(write.getName(), write);
        ht.put(input.getName(), input);
        ht.put(output.getName(), output);

    }

    public static void main(String[] args) throws SymbolTableError {
        SymbolTable h = new SymbolTable(100);
        SymbolTableEntry a = new ConstantEntry("a", TokenType.IDENTIFIER);
        SymbolTableEntry A = new ConstantEntry("A", TokenType.IDENTIFIER);
        SymbolTableEntry b = new ConstantEntry("b", TokenType.IDENTIFIER);
        SymbolTableEntry aA = new ConstantEntry("aA", TokenType.IDENTIFIER);
        SymbolTableEntry Aa = new ConstantEntry("Aa", TokenType.IDENTIFIER);
        SymbolTableEntry BA = new ConstantEntry("Aa", TokenType.IDENTIFIER);
        
        h.insert(a);
        h.insert(A);
        h.insert(b);
        h.insert(aA);
        h.insert(Aa);
        // Uncommenting the code below causes the error that we want
        // h.insert(BA);
        h.dumpTable();

    }

}
