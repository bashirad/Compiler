package lexer;

import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// TODO 1. develop the code the will run the lexer here
// TODO 2. make use of RegEx
public class Scan {

    private static String OPEN_BLOCK;
    private static String CLOSE_BLOCK;
    private static String ID;
    private static String I_TYPE;
    private static String EOP;
    private static String ASSIGN_OP;
    private static String OPEN_PARENTHESIS;
    private static String CLOSE_PARENTHESIS;
    private static String KEYWORDS;
    private static String OPEN_QUOTES;
    private static String CLOSE_QUOTES;
    private static String DIGIT;
    private static String WHITE_SPACE;
    private static String SYMBOLS;
    private static int LINE_NUMBER = 1;
    private static int POSITION_NUMBER = 0;
    private static int PROGRAM_NUMBER = 1;

    public static void main(String[] args) {
        init();
    }
    public static void init() {
        scan();
        //readChars();
        //readAllWords();
    }
    public static void scan() {

        EOP = "\\$";
        OPEN_BLOCK = "\\{";
        CLOSE_BLOCK = "\\}";
        I_TYPE = "int | String | boolean";
        ASSIGN_OP = "=";
        OPEN_PARENTHESIS = "\\(";
        CLOSE_PARENTHESIS = "\\)";
        KEYWORDS = "print | while | true | false |";
        OPEN_QUOTES = "\"";
        CLOSE_QUOTES = "\"";
        WHITE_SPACE = "(' ' | \\t | \\n)+";

        DIGIT = "0|([1-9][0-9]*)";
        ID = "[a-z][a-z0-9]*";
        SYMBOLS = "\\$|\\(|\\)|\\{|\\}|\\=|\\!|\\+|\\/|\\*|\\\"";


        System.out.println(CLOSE_BLOCK);
        String code = readChars();

        Pattern pattern = Pattern.compile("regex_pattern", Pattern.CASE_INSENSITIVE);
        Pattern patternEOP = Pattern.compile(EOP, Pattern.CASE_INSENSITIVE);
        Pattern patternOpenBlock = Pattern.compile(OPEN_BLOCK, Pattern.CASE_INSENSITIVE);
        Pattern patternCloseBlock = Pattern.compile(CLOSE_BLOCK, Pattern.CASE_INSENSITIVE);
        Pattern patternIType = Pattern.compile(I_TYPE, Pattern.CASE_INSENSITIVE);
        Pattern patternAssign_OP = Pattern.compile(ASSIGN_OP, Pattern.CASE_INSENSITIVE);
        Pattern patternOpen_Bracket = Pattern.compile(OPEN_PARENTHESIS, Pattern.CASE_INSENSITIVE);
        Pattern patternCloseBracket = Pattern.compile(CLOSE_PARENTHESIS, Pattern.CASE_INSENSITIVE);
        Pattern patternKeywords = Pattern.compile(KEYWORDS, Pattern.CASE_INSENSITIVE);
        Pattern patternOpen_Quotes = Pattern.compile(OPEN_QUOTES, Pattern.CASE_INSENSITIVE);
        Pattern patternClose_Quotes = Pattern.compile(CLOSE_QUOTES, Pattern.CASE_INSENSITIVE);
        Pattern patternWhiteSpace = Pattern.compile(WHITE_SPACE, Pattern.CASE_INSENSITIVE);

        Pattern patternDigit = Pattern.compile(DIGIT, Pattern.CASE_INSENSITIVE);
        Pattern patternID = Pattern.compile(ID, Pattern.CASE_INSENSITIVE);
        Pattern patternSymbols = Pattern.compile(SYMBOLS, Pattern.CASE_INSENSITIVE);

        /** stream the characters in the string     */
        for (int i = 0; i < code.length(); i++) {
            char ch = code.charAt(i);
            System.out.println(ch);

            String code2 = "";
            code2 += ch;

            Matcher matchDigits = patternDigit.matcher(String.valueOf(code2));
            /** code for recognizing digit using RegEx     */
            if (matchDigits.matches()) {
                Tokens symbols = new Tokens("DIGIT", matchDigits.group(), LINE_NUMBER, POSITION_NUMBER);
                Lexer.log(PROGRAM_NUMBER, true, "Lexer", symbols.lexemeName, symbols.symbol,
                        symbols.lineNum, symbols.positionNum);
            } else {
                Matcher matchID = patternID.matcher(String.valueOf(code2));
                /** code for recognizing id using RegEx     */
                if (matchID.matches()) {
                    Tokens id = new Tokens("ID", matchID.group(), LINE_NUMBER, POSITION_NUMBER);
                    Lexer.log(PROGRAM_NUMBER, true, "Lexer", id.lexemeName, id.symbol,
                            id.lineNum, id.positionNum);
                }
                else {
                    Matcher matchSymbol = patternSymbols.matcher(String.valueOf(code2));
                    /** code for recognizing symbols using RegEx     */
                    if (matchSymbol.matches()) {
                        Tokens symbols = new Tokens("SYMBOLS", matchSymbol.group(), LINE_NUMBER, POSITION_NUMBER);
                        Lexer.log(PROGRAM_NUMBER, true, "Lexer", symbols.lexemeName, symbols.symbol,
                                symbols.lineNum, symbols.positionNum);
                    } else {
                            System.out.println("There is an error");
                        }
                    }
                }

            /** increment line numbers and position numbers     */
            POSITION_NUMBER++;
            if ('\n' == code.charAt(i)) {
                LINE_NUMBER++;
                POSITION_NUMBER = 0;
            }
        }
    }

    public static String readChars () {

        int lastPosition = 0;
        int currentPosition = 0;
        File file =new File("C:\\Users\\Bashir\\Documents\\Bashirs_Code_all\\Java\\cmpt432\\src\\code.txt");     //Creation of File Descriptor for input file
        FileReader file_read = null;   //Creation of File Reader object
        try {
            file_read = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br=new BufferedReader(file_read);  //Creation of BufferedReader object
        int c = 0;
        String code = "";
        while(true)         //Read char by Char
        {
            try {
                if (!((c = br.read()) != -1)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            char character = (char) c;          //converting integer to char
            //System.out.print(character);        //Display the Character
            code += (char)(character);
            lastPosition = currentPosition;         // update the last position
            currentPosition++;                      // increment the current position

        }
        return code;
    }

}
