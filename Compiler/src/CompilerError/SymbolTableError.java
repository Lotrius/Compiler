/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CompilerError;

/**
 *
 * @author Solomon
 */
public class SymbolTableError extends CompilerError{
    public SymbolTableError(String message) {
        super(message);
    }
    
    public static SymbolTableError multipleEntry(String entry) {
        return new SymbolTableError("Error: Tried to insert " + entry + " multiple times");
    }
}
