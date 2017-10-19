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
public class RHSTable {

    GrammarSymbol[][] rules;

    public RHSTable() {
        init();
    }

    public void init() {
        rules = new GrammarSymbol[][]{
            //dummy element 0
            {},
            //production 1         
            {TokenType.PROGRAM, TokenType.IDENTIFIER, TokenType.LEFTPAREN, NonTerminal.identifier_list,
                TokenType.RIGHTPAREN, TokenType.SEMICOLON, NonTerminal.declarations,
                NonTerminal.sub_declarations, NonTerminal.compound_statement},
            //production 2		         
            {TokenType.IDENTIFIER, NonTerminal.identifier_list_tail},
            //production 3		         
            {TokenType.COMMA, TokenType.IDENTIFIER, NonTerminal.identifier_list_tail},
            //production 4		         
            {},
            //production 5		         
            {TokenType.VAR, NonTerminal.declaration_list},
            //production 6		         
            {},
            //production 7		         
            {NonTerminal.identifier_list, TokenType.COLON, NonTerminal.type,
                TokenType.SEMICOLON, NonTerminal.declaration_list_tail},
            //production 8		         SemanticAc
            {NonTerminal.identifier_list, TokenType.COLON, NonTerminal.type,
                TokenType.SEMICOLON, NonTerminal.declaration_list_tail},
            //production 9		         
            {},
            //production 10		         
            {NonTerminal.standard_type},
            //production 11		         
            {NonTerminal.array_type},
            //production 12		         
            {TokenType.INTEGER},
            //production 13		         
            {TokenType.REAL},
            //production 14		         
            {TokenType.ARRAY, TokenType.LEFTBRACKET, TokenType.INTCONSTANT,
                TokenType.DOUBLEDOT, TokenType.INTCONSTANT,
                TokenType.RIGHTBRACKET, TokenType.OF, NonTerminal.standard_type},
            //production 15		         
            {NonTerminal.subprogram_declaration, NonTerminal.sub_declarations},
            //production 16		         
            {},
            //production 17		         
            {NonTerminal.subprogram_head, NonTerminal.declarations,
                NonTerminal.compound_statement},
            //production 18		         
            {TokenType.FUNCTION, TokenType.IDENTIFIER, NonTerminal.arguments,
                TokenType.COLON, TokenType.RESULT, NonTerminal.standard_type, TokenType.SEMICOLON,},
            //production 19		         
            {TokenType.PROCEDURE, TokenType.IDENTIFIER, NonTerminal.arguments,
                TokenType.SEMICOLON},
            //production 20		         
            {TokenType.LEFTPAREN, NonTerminal.parameter_list, TokenType.RIGHTPAREN,},
            //production 21		         
            {},
            //production 22		         
            {NonTerminal.identifier_list, TokenType.COLON, NonTerminal.type,
                NonTerminal.parameter_list_tail},
            //production 23		         
            {TokenType.SEMICOLON, NonTerminal.identifier_list, TokenType.COLON, NonTerminal.type,
                NonTerminal.parameter_list_tail},
            //production 24		         
            {},
            //production 25		         
            {TokenType.BEGIN, NonTerminal.statement_list, TokenType.END},
            //production 26		         
            {NonTerminal.statement, NonTerminal.statement_list_tail},
            //production 27		         
            {TokenType.SEMICOLON, NonTerminal.statement, NonTerminal.statement_list_tail},
            //production 28		         
            {},
            //production 29		         
            {NonTerminal.elementary_statement},
            //production 30		         
            {TokenType.IF, NonTerminal.expression, TokenType.THEN,
                NonTerminal.statement, NonTerminal.else_clause},
            //production 31		         
            {TokenType.WHILE, NonTerminal.expression,
                TokenType.DO, NonTerminal.statement},
            //production 32		         
            {TokenType.ELSE, NonTerminal.statement},
            //production 33		         
            {},
            //production 34		         
            {TokenType.IDENTIFIER, NonTerminal.es_tail},
            //production 35		         
            {NonTerminal.compound_statement},
            //production 36		         
            {NonTerminal.subscript, TokenType.ASSIGNOP, NonTerminal.expression},
            //production 37		         
            {NonTerminal.parameters},
            //production 38		         
            {TokenType.LEFTBRACKET, NonTerminal.expression, TokenType.RIGHTBRACKET},
            //production 39		         
            {},
            //production 40		         
            {TokenType.LEFTPAREN, NonTerminal.expression_list, TokenType.RIGHTPAREN},
            //production 41		         
            {},
            //production 42		         
            {NonTerminal.expression, NonTerminal.expression_list_tail},
            //production 43		         
            {TokenType.COMMA, NonTerminal.expression, NonTerminal.expression_list_tail},
            //production 44		         
            {},
            //production 45		         
            {NonTerminal.simple_expression, NonTerminal.expression_tail},
            //production 46		         
            {TokenType.RELOP, NonTerminal.simple_expression},
            //production 47		         
            {},
            //production 48		         
            {NonTerminal.term, NonTerminal.simple_expression_tail},
            //production 49		         
            {NonTerminal.sign, NonTerminal.term,
                NonTerminal.simple_expression_tail},
            //production 50		         
            {TokenType.ADDOP, NonTerminal.term,
                NonTerminal.simple_expression_tail},
            //production 51		         
            {},
            //production 52		         
            {NonTerminal.factor, NonTerminal.term_tail},
            //production 53		         
            {TokenType.MULOP, NonTerminal.factor,
                NonTerminal.term_tail},
            //production 54		  
            {},
            //production 55					   
            {TokenType.IDENTIFIER, NonTerminal.factor_tail},
            //production 56		         
            {NonTerminal.constant},
            //production 57		         
            {TokenType.LEFTPAREN, NonTerminal.expression, TokenType.RIGHTPAREN},
            //production 58		         
            {TokenType.NOT, NonTerminal.factor},
            //production 59		         
            {NonTerminal.actual_parameters},
            //production 60		         
            {NonTerminal.subscript},
            //production 61		         
            {TokenType.LEFTPAREN, NonTerminal.expression_list, TokenType.RIGHTPAREN},
            //production 62		         
            {},
            //production 63		         
            {TokenType.UNARYPLUS},
            //production 64		         
            {TokenType.UNARYMINUS},
            //production 65		         
            {NonTerminal.program, TokenType.ENDMARKER},
            //production 66		         
            {TokenType.INTCONSTANT},
            //production 67		         
            {TokenType.REALCONSTANT}
        };
    }

    public GrammarSymbol[] getRule(int n) {
        return rules[n];
    }

    public void dumpTable() {
        for (int i = 1; i < rules.length; ++i) {
            System.out.print("RULE : ");
            GrammarSymbol[] rule = getRule(i);
            for (GrammarSymbol j : rule) {
                System.out.print(" " + j);
            }
            System.out.println();
        }
    }

    public void printrule(int n) {
        GrammarSymbol[] rule = getRule(n);

        System.out.print("Rule " + n);
        for (GrammarSymbol j : rule) {
            System.out.print(" " + j);
        }
        System.out.println();
    }
}
