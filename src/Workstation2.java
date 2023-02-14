import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// TODO 1. develop the code the will run the lexer here
// TODO 2. make use of RegEx
public class Workstation2 {

    private static String OPEN_BLOCK;
    private static String CLOSE_BLOCK;
    private static String ID;
    private static String I_TYPE;
    private static String EOP;
    private static String ASSIGN_OP;
    private static String OPEN_PARENTHESIS;
    private static String CLOSE_PARENTHESIS;
    private static String OPEN_QUOTES;
    private static String CLOSE_QUOTES;
    private static String KEYWORDS;
    private static String DIGIT;
    private static String WHITE_SPACE;
    private static String SYMBOLS;
    private static int LINE_NUMBER = 1;
    private static int POSITION_NUMBER = 0;
    private static int PROGRAM_NUMBER = 1;
    private static int lastPosition = -1;
    private static int currentPosition = 0;
    private static String KEYWORD_CANDIDATE = " ";

    public static void main(String[] args ) {
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
        OPEN_QUOTES = "\"";
        CLOSE_QUOTES = "\"";

        KEYWORDS = "\\b(print|while|true|false|int|string|boolean)\\b";
        ID = "[a-z][a-z0-9]*";
        DIGIT = "0|([1-9][0-9]*)";
        SYMBOLS = "\\$|\\(|\\)|\\{|\\}|\\=|\\!|\\+|\\/|\\*|\\\"|\\.";
        WHITE_SPACE = "[\\n\\t\\s\\r ]+";

        // start wth keywords and create empty string and append the char
        System.out.println(CLOSE_BLOCK);
        String code = readChars();

        Pattern patternKeyword = Pattern.compile("\\b" + KEYWORDS + "\\b", Pattern.CASE_INSENSITIVE);
        Pattern patternID = Pattern.compile(ID, Pattern.CASE_INSENSITIVE);
        Pattern patternDigit = Pattern.compile(DIGIT, Pattern.CASE_INSENSITIVE);
        Pattern patternSymbols = Pattern.compile(SYMBOLS, Pattern.CASE_INSENSITIVE);
        Pattern patternWhiteSpace = Pattern.compile(WHITE_SPACE, Pattern.CASE_INSENSITIVE);

        // stream the characters in the string
        for (int i = 0; i < code.length(); i++) {
            char currentCharacter = code.charAt(i);
            //System.out.println(ch);


            Matcher matchLetter1 = patternID.matcher(String.valueOf(currentCharacter));
            // code for recognizing id using RegEx
            if (matchLetter1.matches()) {

                lastPosition = i -1;
                currentPosition = i;


                Matcher matchLetter2 = patternID.matcher(String.valueOf(currentCharacter));
                if (matchLetter2.matches()) {
                    if (KEYWORD_CANDIDATE.length() == 1) KEYWORD_CANDIDATE.replace(" ", String.valueOf(currentCharacter));

                    KEYWORD_CANDIDATE += currentCharacter;
                    //System.out.println(KEYWORD);

                    Matcher matchKeywords = patternKeyword.matcher(KEYWORD_CANDIDATE);
                    // code for recognizing digit using RegEx
                    if (matchKeywords.find()) {
                        Tokens keyword = new Tokens("KEYWORD", matchKeywords.group(), LINE_NUMBER, POSITION_NUMBER);
                        Lexer.log(PROGRAM_NUMBER, true, "Lexer", keyword.lexemeName, keyword.symbol,
                                keyword.lineNum, keyword.positionNum);
                        KEYWORD_CANDIDATE = "";
                        break;
                    } else {
                        Tokens id = new Tokens("ID", matchLetter2.group(), LINE_NUMBER, POSITION_NUMBER);
                        Lexer.log(PROGRAM_NUMBER, true, "Lexer", id.lexemeName, id.symbol,
                                id.lineNum, id.positionNum);
                        KEYWORD_CANDIDATE = "";
                    }
                }

                /*Matcher matchID = patternID.matcher(String.valueOf(currentCharacter));
                // code for recognizing id using RegEx
                if (matchID.matches()) {
                    Tokens id = new Tokens("ID", matchID.group(), LINE_NUMBER, POSITION_NUMBER);
                    Lexer.log(PROGRAM_NUMBER, true, "Lexer", id.lexemeName, id.symbol,
                            id.lineNum, id.positionNum);
                    KEYWORD_CANDIDATE = "";
                } else {
                // create empty string and append the char
                Tokens id = new Tokens("ID", matchID.group(), LINE_NUMBER, POSITION_NUMBER);
                Lexer.log(PROGRAM_NUMBER, true, "Lexer", id.lexemeName, id.symbol,
                        id.lineNum, id.positionNum);
                }*/
            } else {
                Matcher matchDigits = patternDigit.matcher(String.valueOf(currentCharacter));
                // code for recognizing digit using RegEx
                if (matchDigits.matches()) {
                    Tokens symbols = new Tokens("DIGIT", matchDigits.group(), LINE_NUMBER, POSITION_NUMBER);
                    Lexer.log(PROGRAM_NUMBER, true, "Lexer", symbols.lexemeName, symbols.symbol,
                            symbols.lineNum, symbols.positionNum);
                } else {
                    Matcher matchSymbol = patternSymbols.matcher(String.valueOf(currentCharacter));
                    if (matchSymbol.matches()) {
                        Tokens symbols = new Tokens("SYMBOLS", matchSymbol.group(), LINE_NUMBER, POSITION_NUMBER);
                        Lexer.log(PROGRAM_NUMBER, true, "Lexer", symbols.lexemeName, symbols.symbol,
                                symbols.lineNum, symbols.positionNum);
                    } else {
                        Matcher matchWhiteSpace = patternWhiteSpace.matcher(String.valueOf(currentCharacter));
                        // code for recognizing whitespace using RegEx
                        if (matchWhiteSpace.matches()) {
                            Tokens whiteSpace = new Tokens("WHITE_SPACE", "\' \'", LINE_NUMBER, POSITION_NUMBER);
                            Lexer.log(PROGRAM_NUMBER, false, "Lexer", whiteSpace.lexemeName, whiteSpace.symbol,
                                    whiteSpace.lineNum, whiteSpace.positionNum);
                        } else {
                            System.out.println("There is an error");
                        }
                    }
                }

                POSITION_NUMBER++;
                if ('\n' == code.charAt(i)) {
                    LINE_NUMBER++;
                    POSITION_NUMBER = 0;
                }
            }


        }
    }
    public static String readChars () {

        int lastPosition = 0;
        int currentPosition = 0;
        Scanner scan = new Scanner(System.in);

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
