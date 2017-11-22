/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CompilerError;

import SemanticActions.SemanticActions.etype;

/**
 *
 * @author Solomon
 */
public class SemanticError extends CompilerError {

    public SemanticError(String message) {
        super(message);
    }

    public static SemanticError undeclaredVariable(String value) {
        return new SemanticError("Error: Undeclared variable " + value);
    }

    public static SemanticError mismatch(String value1, String value2) {
        return new SemanticError("Error: Type mismatch " + value1 + " and " + value2);
    }

    public static SemanticError eTypeMismatch(etype et) {
        return new SemanticError("Error: Etype mismatch with " + et);
    }

    public static SemanticError modError(String value1, String value2) {
        return new SemanticError("Error: Mod error with " + value1 + " and " + value2);
    }
    
    public static SemanticError undeclaredError(String value) {
        return new SemanticError("Error: Undeclared variable " + value);
    }
}
