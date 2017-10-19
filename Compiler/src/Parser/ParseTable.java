/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Solomon
 */
public class ParseTable {

    public static final File file = new File("ParseTable.txt");
    private static final int ROWS = 35;
    private static final int COL = 38;
    private int[][] parseTable;

    public ParseTable() throws FileNotFoundException {
        parseTable = new int[ROWS][COL];
        Scanner scanner = new Scanner(file);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COL; j++) {
                parseTable[i][j] = scanner.nextInt();
            }
        }
    }
    
    public int entry(TokenType t, NonTerminal n) {
        return parseTable[t.getIndex()][n.getIndex()];
    }
}
