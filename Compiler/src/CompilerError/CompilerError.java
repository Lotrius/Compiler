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
public class CompilerError extends Exception{
    public CompilerError() {
        super();
    }
    
    public CompilerError(String message) {
        super(message);
    }
    
    public CompilerError(String message, Throwable cause) {
        super(message, cause);
    }
    
    public CompilerError(Throwable cause) {
        super(cause);
    }
}
