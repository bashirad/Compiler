package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Lexer {

    private static String path;

    private static String OPEN_BLOCK;
    private static String CLOSE_BLOCK;
    private static String ID;
    private static String I_TYPE;
    private static String EOP;
    private static String ASSIGN_OP;
    private static String OPEN_BRACKET;
    private static String CLOSE_BRACKET;
    private static String PRINT_OP;
    private static String OPEN_QUOTES;
    private static String CLOSE_QUOTES;
    private static String INT;
    private static String WHITE_SPACE;


    public static void init() {

        /** Get the absolute path of the source code */
        //System.out.println("Enter the absolute path of the source code: ");
        //Scanner scan = new Scanner(System.in);
        //path = scan.nextLine();

    }

    /** Read the source code that is the file */
    public static void readFile (String pathname) {
        try {
            File myObj = new File(pathname);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
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
            //if (progNum == 1) System.out.println("INFO Lexer - Lexing program " + progNum + " ... ");
            System.out.println("DEBUG " + compilerStage + " - " + tokenName + " [ " + tokenSymbol + " ] found at ("
                                + tokenLineNum + ":" + tokenPosNum + ")");
        }
    }

}
