/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import CompilerError.LexicalError;
import Lexer.Token;
import Lexer.Tokenizer;
import java.io.IOException;
import java.util.Stack;

/**
 *
 * @author Solomon
 */
public class Parser {
//    Current token = GET Next Token                        // from lexical analyzer
//
//SET Parse Stack TO Empty stack; 
//PUSH ENDOFFILE and Start symbol ON Parse stack;
//
//WHILE Current token not equal to ENDOFFILE: 
//  
//   SET Predicted TO POP(Parse Stack)       // get the top symbol off the stack 
//   IF Predicted is a Token: 
//        // Try a match move: 
//         IF Predicted = Current token 
//             Current token = GET Next Token                         // matched 
//             ELSE IF Predicted not equal to Current token: 
//                ERROR "Expecting x, y found" 
//                                        // x is Predicted; y is current token 
//   ELSE IF Predicted is a Non-terminal: 
//        IF [Parse_table [Predicted, Current token]] = ERROR     // A 999 entry 
//           ERROR  "Uexpected :" Current token 
//       ELSE 
//           PUSH Symbols in RHS [Parse_table [Predicted, Current token]] 
//                ON Parse Stack             // Push the right hand side symbols 

    public Stack<GrammarSymbol> stack = new Stack<GrammarSymbol>();
    public boolean error = false;
    public boolean dump = false;
    public Tokenizer tokenizer;
    public ParseTable parseTable = new ParseTable();
    public RHSTable rhsTable = new RHSTable();

    public static void main(String[] args) throws IOException, LexicalError {
        String s = args[0];
        Parser parser = new Parser(s);
    }

    public Parser(String s) throws IOException, LexicalError {
        tokenizer = new Tokenizer(s);
    }

    public void parse() throws IOException, LexicalError {
        Token currentToken = tokenizer.getNextToken();
        stack.clear();
        stack.push(TokenType.ENDOFFILE);
        stack.push(NonTerminal.Goal);

        while (!stack.empty() && !error) {
            if (currentToken.getType() == null) {
                currentToken = tokenizer.getNextToken();
                continue;
            }

            GrammarSymbol predicted = stack.pop();

            if (predicted.isToken()) {
                if (predicted == currentToken.getType()) {
                    System.out.println("MATCH: " + predicted + " popped with " + currentToken);
                    currentToken = tokenizer.getNextToken();
                } else if (predicted != currentToken) {
                    System.out.println("Error: Expecting " + predicted + ", " + currentToken + " found");
                    error = true;
                }
            } else if (predicted.isNonTerminal()) {
                int entry = parseTable.entry(currentToken.getType(), (NonTerminal) predicted);
                if (entry == 999) {
                    System.out.println("Error: Unexpected " + currentToken);
                    error = true;
                } else if (entry < 0); 
                else {
                    System.out.println("Popped " + predicted);
                    GrammarSymbol[] rule = rhsTable.getRule(entry);
                    //iterate from end of array and push the GrammarSymbols onto parse stack
                    int len = rule.length - 1;
                    System.out.print("Pushed ");
                    for (int i = len; i >= 0; i--) {
                        stack.push(rule[i]);
                        System.out.print(rule[i] + " ");
                    }
                    System.out.println();
                }
            }
        }
    }
}
