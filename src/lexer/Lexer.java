package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Lexer {

    private static String path;

    public static void init() {

        /** Get the absolute path of the source code */
        //System.out.println("Enter the absolute path of the source code: ");
        //Scanner scan = new Scanner(System.in);
        //path = scan.nextLine();


        /** Read the file data */
        Lexer.readFile("C:\\Users\\Bashir\\Documents\\Bashirs_Code_all\\Java\\cmpt432\\src\\code.txt");
        Tokens token1;
        if (1 == 1) {
            token1 = new Tokens("OPEN BLOCK", "{", 1, 1);
        }

        /** Log information out to the output screen */
        log(1, true, "Lexer", token1.lexemeName, token1.symbol,
                token1.lineNum, token1.positionNum);
    }

    /** Read the source code that is the file */
    public static void readFile (String pathname) {
        try {
            File myObj = new File(pathname);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                /** System.out.println(data);   */
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /** Concrete implementation for the log method    */
    public static void log(int progNum, Boolean debug, String compilerStage, String tokenName, String tokenSymbol,
                    int tokenLineNum, int tokenPosNum) {
        if (debug) {
            System.out.println("INFO Lexer - Lexing program " + progNum + " ... ");
            System.out.println("DEBUG Lexer - OPEN_BLOCK [ { ] found at (1:1)");
            System.out.println("DEBUG " + compilerStage + " - " + tokenName + " [ " + tokenSymbol + " ] found at ("
                                + tokenLineNum + ":" + tokenPosNum + ")");
        }
    }

}
