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
public interface GrammarSymbol {

    public boolean isToken();

    public boolean isNonTerminal();

    public boolean isAction();
}
