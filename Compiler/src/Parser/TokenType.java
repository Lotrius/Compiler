/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

/**
 *
 * @author Solomon
 */
public enum TokenType implements GrammarSymbol
{
	PROGRAM(0), BEGIN(1), END(2), VAR(3), FUNCTION(4), PROCEDURE(5), RESULT(6), INTEGER(7), REAL(8), ARRAY(9), OF(10),
	IF(11), THEN(12), ELSE(13), WHILE(14), DO(15), NOT(16), IDENTIFIER(17), INTCONSTANT(18), REALCONSTANT(19), RELOP(20),
	MULOP(21), ADDOP(22), ASSIGNOP(23), COMMA(24), SEMICOLON(25), COLON(26), RIGHTPAREN(27), LEFTPAREN(28), RIGHTBRACKET(29),
	LEFTBRACKET(30), UNARYMINUS(31), UNARYPLUS(32), DOUBLEDOT(33), ENDMARKER(34), ENDOFFILE(35), FILE(36) ;

	private int n;
	private TokenType(int i) { n = i; }
	public int getIndex () { return n; }
	public boolean isToken () { return true; }
	public boolean isNonTerminal() { return false; }
	public boolean isAction() { return false; }
}
