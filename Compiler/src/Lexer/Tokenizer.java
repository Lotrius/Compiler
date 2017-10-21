package Lexer;

import CompilerError.LexicalError;
import java.io.*;
import java.util.ArrayList;
import java.util.Stack;
import Parser.TokenType;

/**
 *
 * @author Solomon
 */
public class Tokenizer {

    private static final int MAX_IDENTIFIER_LENGTH = 64;
    private static final String VALID_CHARS
            = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"
            + ".,;:<>/*[]+-=()}{\t ";
    private static final char BLANK = ' ';
    private static final char OPEN_BRACE = '{';
    private static final char CLOSE_BRACE = '}';
    private Stack<Character> stack = new Stack<Character>();
    private int lineNumber = 0;
    private int charIndex = 0;
    private char currentChar;
    private String currentLine;
    private int lineLength;
    private Token lastToken = new Token();
    private BufferedReader reader;
    private File file;
    private boolean openComment = false;
    ArrayList<Token> al = new ArrayList<Token>();
    File fout;
    FileOutputStream fos;
    BufferedWriter bw;

    public static void main(String[] args) throws IOException, LexicalError {
        String s = args[0];
        Tokenizer tokenizer = new Tokenizer(s);
    }

    public Tokenizer(String s) throws IOException, LexicalError {
        file = new File(s);
        reader = new BufferedReader(new FileReader(file));

        fout = new File("out.txt");
        fos = new FileOutputStream(fout);
        bw = new BufferedWriter(new OutputStreamWriter(fos));

        getLine();

//        while (currentLine != null) {
//            getNextToken();
//        }
//        Token end = new Token(TokenType.ENDOFFILE, null);
//        bw.write(end.toString());
//        System.out.println(end.toString());
//        bw.close();
    }

    public Token getNextToken() throws IOException, LexicalError {
        Token token = new Token();

        if ((lastToken.getType() != null)
                && (lastToken.getType().equals(TokenType.ENDMARKER))) {
            token.setType(TokenType.ENDOFFILE);
            token.setValue(null);
            bw.write(token.toString());
            //bw.close();
            return token;
        }

        getChar();

        if (currentChar == BLANK) {
            getChar();
        }
        if (Character.isLetter(currentChar)) {
            token = isLetterToken();
        } else if (!Character.isLetterOrDigit(currentChar)) {
            if ((currentChar == '+' || currentChar == '-')) {
                char oldChar = currentChar;
                int oldIndex = charIndex;
                char x = getChar();
                if (Character.isDigit(x)) {
                    currentChar = oldChar;
                    charIndex = oldIndex;
                    isNumberToken();
                } else {
                    currentChar = oldChar;
                    charIndex = oldIndex;
                    isSymbolToken();
                }
            } else {
                token = isSymbolToken();
            }
        } else {
            token = isNumberToken();
        }

        lastToken = token;
        return token;
    }

    public Token isNumberToken() throws LexicalError, IOException {
        Token token = new Token();
        String number = "";

        if (currentChar == '+' || currentChar == '-') {
            number += currentChar;
            getChar();
        }

        while (Character.isDigit(currentChar)) {
            number += currentChar;
            getChar();
        }
        token.setType(TokenType.INTCONSTANT);
        token.setValue(number);

        if (currentChar == '.') {
            char previous = currentChar;
            int previousIndex = charIndex;
            getChar();
            if (currentChar == '.') {
                pushBack(currentChar);
                currentChar = previous;
                charIndex = previousIndex - 1;
            } else {
                currentChar = previous;
                charIndex = previousIndex;
                number += currentChar;
                currentChar = getChar();
                while (Character.isDigit(currentChar)) {
                    number += currentChar;
                    currentChar = getChar();
                }
                token.setType(TokenType.REALCONSTANT);
                token.setValue(number);
            }
        } else if (Character.toLowerCase(currentChar) == 'e') {
            char oldChar = currentChar;
            int oldIndex = charIndex;
            char x = getChar();

            if (x == '+' || x == '-' || Character.isDigit(x)) {
                currentChar = oldChar;
                charIndex = oldIndex;
                number += currentChar;

                currentChar = getChar();
                if (currentChar == '+' || currentChar == '-') {
                    number += currentChar;
                    currentChar = getChar();
                }
                while (Character.isDigit(currentChar)) {
                    number += currentChar;
                    currentChar = getChar();
                }
                token.setType(TokenType.REALCONSTANT);
                token.setValue(number);
            }
        }

        pushBack(currentChar);
        System.out.println(token.toString());
        bw.write(token.toString());
        bw.newLine();
        //lastToken = token;
        return token;
    }

    public Token isLetterToken() throws LexicalError, IOException {
        Token token = new Token();
        String identifier = "";
        boolean isLetterOrDigit = Character.isLetterOrDigit(currentChar);
        while (isLetterOrDigit) {
            identifier += currentChar;
            currentChar = getChar();
            isLetterOrDigit = Character.isLetterOrDigit(currentChar);
        }
        if (identifier.length() > MAX_IDENTIFIER_LENGTH) {
            throw LexicalError.identifierTooLong(lineNumber, identifier);
        }
        if (!Character.isLetterOrDigit(currentChar)) {
            pushBack(currentChar);
            charIndex--;
        }

        switch (identifier.toLowerCase()) {
            case "program":
                token.setType(TokenType.PROGRAM);
                token.setValue(null);
                break;
            case "integer":
                token.setType(TokenType.INTEGER);
                token.setValue(null);
                break;
            case "real":
                token.setType(TokenType.REAL);
                token.setValue(null);
                break;
            case "begin":
                token.setType(TokenType.BEGIN);
                token.setValue(null);
                break;
            case "end":
                token.setType(TokenType.END);
                token.setValue(null);
                break;
            case "var":
                token.setType(TokenType.VAR);
                token.setValue(null);
                break;
            case "function":
                token.setType(TokenType.FUNCTION);
                token.setValue(null);
                break;
            case "procedure":
                token.setType(TokenType.PROCEDURE);
                token.setValue(null);
                break;
            case "result":
                token.setType(TokenType.RESULT);
                token.setValue(null);
                break;
            case "array":
                token.setType(TokenType.ARRAY);
                token.setValue(null);
                break;
            case "of":
                token.setType(TokenType.OF);
                token.setValue(null);
                break;
            case "if":
                token.setType(TokenType.IF);
                token.setValue(null);
                break;
            case "then":
                token.setType(TokenType.THEN);
                token.setValue(null);
                break;
            case "else":
                token.setType(TokenType.ELSE);
                token.setValue(null);
                break;
            case "while":
                token.setType(TokenType.WHILE);
                token.setValue(null);
                break;
            case "do":
                token.setType(TokenType.DO);
                token.setValue(null);
                break;
            case "not":
                token.setType(TokenType.NOT);
                token.setValue(null);
                break;
            case "div":
                token.setType(TokenType.MULOP);
                token.setValue("3");
                break;
            case "mod":
                token.setType(TokenType.MULOP);
                token.setValue("4");
                break;
            case "and":
                token.setType(TokenType.MULOP);
                token.setValue("5");
                break;
            case "or":
                token.setType(TokenType.ADDOP);
                token.setValue("3");
                break;
            default:
                token.setType(TokenType.IDENTIFIER);
                token.setValue(identifier);
                break;

        }
        System.out.println(token.toString());
        //lastToken = token;
        bw.write(token.toString());
        bw.newLine();
        return token;
    }

    public Token isSymbolToken() throws LexicalError, IOException {
        Token token = new Token();
        String symbol = "";
        boolean isLetterOrDigit = !Character.isLetterOrDigit(currentChar);
        symbol += currentChar;
        currentChar = getChar();
        if (currentChar == '.') {
            symbol += currentChar;
            currentChar = getChar();
        }
        pushBack(currentChar);
        charIndex--;
        switch (symbol) {
            case "[":
                token.setType(TokenType.LEFTBRACKET);
                token.setValue(null);
                break;
            case "]":
                token.setType(TokenType.RIGHTBRACKET);
                token.setValue(null);
                break;
            case ",":
                token.setType(TokenType.COMMA);
                token.setValue(null);
                break;
            case ";":
                token.setType(TokenType.SEMICOLON);
                token.setValue(null);
                break;
            case ":":
                currentChar = getChar();
                if (currentChar == '=') {
                    token.setType(TokenType.ASSIGNOP);
                    token.setValue(null);
                    currentChar = getChar();
                } else {
                    token.setType(TokenType.COLON);
                    token.setValue(null);
                }
                break;
            case "<":
                currentChar = getChar();
                if (currentChar == '>') {
                    token.setType(TokenType.RELOP);
                    token.setValue("2");
                    currentChar = getChar();
                } else if (currentChar == '=') {
                    token.setType(TokenType.RELOP);
                    token.setValue("5");
                    currentChar = getChar();
                } else {
                    token.setType(TokenType.RELOP);
                    token.setValue("3");
                }
                break;
            case ">":
                currentChar = getChar();
                if (currentChar == '=') {
                    token.setType(TokenType.RELOP);
                    token.setValue("5");
                    currentChar = getChar();
                } else {
                    token.setType(TokenType.RELOP);
                    token.setValue("4");
                }
                break;
            case "(":
                token.setType(TokenType.LEFTPAREN);
                token.setValue(null);
                break;
            case ")":
                token.setType(TokenType.RIGHTPAREN);
                token.setValue(null);
                break;
            case ".":
                if (lastToken.getType().equals(TokenType.END)) {
                    token.setType(TokenType.ENDMARKER);
                    token.setValue(null);
                }
                break;
            case "+":
                if (lastToken.getType().equals(TokenType.RIGHTPAREN)
                        || lastToken.getType().equals(TokenType.RIGHTBRACKET)
                        || lastToken.getType().equals(TokenType.IDENTIFIER)
                        || lastToken.getType().equals(TokenType.INTCONSTANT)
                        || lastToken.getType().equals(TokenType.REALCONSTANT)) {
                    token.setType(TokenType.ADDOP);
                    token.setValue("1");
                } else {
                    token.setType(TokenType.UNARYPLUS);
                    token.setValue(null);
                }
                break;
            case "-":
                if (lastToken.getType().equals(TokenType.RIGHTPAREN)
                        || lastToken.getType().equals(TokenType.RIGHTBRACKET)
                        || lastToken.getType().equals(TokenType.IDENTIFIER)
                        || lastToken.getType().equals(TokenType.INTCONSTANT)
                        || lastToken.getType().equals(TokenType.REALCONSTANT)) {
                    token.setType(TokenType.ADDOP);
                    token.setValue("2");
                } else {
                    token.setType(TokenType.UNARYMINUS);
                    token.setValue(null);
                }
                break;
            case "..":
                token.setType(TokenType.DOUBLEDOT);
                token.setValue(null);
                break;
            case "=":
                token.setType(TokenType.RELOP);
                token.setValue("1");
                break;
            case "*":
                token.setType(TokenType.MULOP);
                token.setValue("1");
                break;
            case "/":
                token.setType(TokenType.MULOP);
                token.setValue("2");
                break;
            default:
                break;
        }
        //lastToken = token;
        System.out.println(token.toString());
        bw.write(token.toString());
        bw.newLine();
        return token;
    }

    public void pushBack(char c) {
        stack.push(c);
    }

    public void getLine() throws IOException {
        charIndex = 0;
        currentLine = reader.readLine();
        if (currentLine == null) {
            return;
        }
        lineLength = currentLine.length();
        lineNumber++;
    }

    public char getChar() throws LexicalError, IOException {
        if (!stack.empty()) {
            currentChar = stack.pop();
            charIndex++;
            return currentChar;
        }

        while (lineLength == 0 || charIndex + 1 > lineLength) {
            getLine();
            return BLANK;
        }

        currentChar = currentLine.charAt(charIndex);

        if (!VALID_CHARS.contains(Character.toString(currentChar))) {
            throw LexicalError.invalidCharacter(lineNumber, currentChar);
        }
        if (currentChar == BLANK) {
            isBlank();
            return BLANK;
        }
        if (currentChar == OPEN_BRACE) {
            openComment = true;
            isComment();
        }
        if (currentChar == CLOSE_BRACE) {
            isComment();
            if (openComment) {
                throw LexicalError.illFormedComment(lineNumber, "wrong", currentChar);
            }
        }
        charIndex++;
        return currentChar;
    }

    public void isBlank() throws LexicalError, IOException {
        char next = currentChar;
        while (next == BLANK && currentLine != null && charIndex + 1 < lineLength) {
            charIndex++;
            next = currentLine.charAt(charIndex);
        }
        if (next == OPEN_BRACE) {
            openComment = true;
            currentChar = next;
            isComment();
        }
        pushBack(next);
    }

    public void isComment() throws LexicalError, IOException {
        while (currentChar != CLOSE_BRACE) {
            if (!(charIndex + 1 < lineLength)) {
                getLine();
                currentChar = currentLine.charAt(charIndex);
            } else {
                charIndex++;
                currentChar = currentLine.charAt(charIndex);
            }
            if (currentChar == OPEN_BRACE) {
                throw LexicalError.illFormedComment(lineNumber, "wrong", currentChar);
            }
        }

        if (currentChar == CLOSE_BRACE) {
            openComment = false;
        }
    }
}
