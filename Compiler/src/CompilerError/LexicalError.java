package CompilerError;

/**
 *
 * @author Solomon
 */
public class LexicalError extends CompilerError {

    public LexicalError(String message) {
        super(message);
    }

    public static LexicalError invalidCharacter(int line, char c) {
        return new LexicalError("Error: Invalid character " + c + ". Line "
                + line);
    }

    public static LexicalError illFormedConstant(int line, String s) {
        return new LexicalError("Error: Ill-formed constant" + s + ". Line "
                + line);
    }

    public static LexicalError illFormedComment(int line, String type, char brace) {
        if (type.equals("unterminated")) {
            return new LexicalError("Error: Unterminated comment. Line "
                    + line);
        }
        if (brace == '}') {
            return new LexicalError("Error: Cannot include " + brace + " without { preceeding it. Line "
                    + line);
        }
        return new LexicalError("Error: Cannot include " + brace + " in a comment. Line "
                    + line);
    }
    
    public static LexicalError identifierTooLong(int line, String identifier) {
        return new LexicalError("Error: Identifier " + identifier 
                + " too long. Line " + line);
    }
}
