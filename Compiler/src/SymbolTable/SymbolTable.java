/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SymbolTable;

import java.util.Hashtable;

/**
 *
 * @author Solomon
 */
public class SymbolTable {

    private Hashtable<String, SymbolTableEntry> ht;

    public SymbolTable() {
        ht = new Hashtable(20);
    }

    public SymbolTableEntry lookup(String entry) {
        return ht.get(entry);
    }

    public void insert(SymbolTableEntry entry) {
        if (ht.containsValue(entry)) {
            ht.put(entry.toString(), entry);
        } else {
            //TODO
        }
    }
    
    public int size() {
        return ht.size();
    }
    
    public void dumpTable() {
        System.out.println(ht);
    }
    
    public void installBuiltins() {
        
    }

}
