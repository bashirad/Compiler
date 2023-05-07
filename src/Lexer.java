import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private static int LINE_NUMBER = 1;
    private static int POSITION_NUMBER = 0;
    private static Tokens currentTokenID;
    private static Tokens currentTokenKeyword;
    private static StringBuilder strBuilder;
    private static int PROGRAM_NUMBER = 1;
    private static int error_count = 0;

    // Create a list of tokens
    static ArrayList<Tokens> tokens = new ArrayList<>();

    public static void main(String[] args) {

        /**
         * Call init_Lexer and execute the Lexer in the Run environment
         */
        init_Lexer("C:\\Users\\Bashir\\Documents\\Bashirs_Code_all\\Java\\cmpt432\\src\\code.txt");
    }
    public static void init_Lexer( String path) {

        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");
        System.out.println("INFO Lexer   -  Lexing program " + 1 + " ... ");

        /**
         * Start lexing the source code
         */
        scan(path);

    }

    /**
     * This method passes the stream of tokens to the Parser
     */
    public static List<Tokens> passTokens (String path) {

        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");
        System.out.println("INFO Lexer   -  Lexing program " + 1 + " ... ");

        scan(path);

        return tokens;
    }

    /**
     * This method scans the source code and finds the lexemes or the valid words
     */
    public static void scan(String path) {

        POSITION_NUMBER = 1;
        String TOKEN_CANDIDATE = "";
        strBuilder = new StringBuilder(TOKEN_CANDIDATE);

        // start wth keywords and create empty string and append the char
        String code = readChars(path);

        int curPos = 0;
        int beginPos = 0;
        int endPos = 0;
        int keywordNum = 0;

        // stream the characters in the string
        for (int i = 0; i < code.length(); i++) {
            char currentCharacter = code.charAt(i);
            //System.out.println(ch);


            if (isID(currentCharacter)) {
                String tokenSymbol = "";
                tokenSymbol += currentCharacter;
                currentTokenID = new Tokens("ID", tokenSymbol, LINE_NUMBER, POSITION_NUMBER);
                curPos = i;

                //TODO Correct the position and line numbers
                for(int j = i; j < code.length(); j++) {
                    char curChar = code.charAt(j);
                    if (!isWHITESPACE(curChar) && !isSYMBOL(curChar) && !isDIGIT(curChar)){

                        strBuilder.append(curChar);

                        // code for recognizing keywords using RegEx
                        String KEYWORDS = "\\b(print|while|true|false|int|string|boolean|if)\\b";
                        Pattern patternKeyword = Pattern.compile("\\b" + KEYWORDS + "\\b", Pattern.CASE_INSENSITIVE);
                        Matcher matchKeyword = patternKeyword.matcher(String.valueOf(strBuilder));

                        if (matchKeyword.matches()) {
                            currentTokenKeyword = new Tokens(matchKeyword.group().toUpperCase(), matchKeyword.group(), LINE_NUMBER, POSITION_NUMBER);
                            keywordNum++;
                            curPos = j;
                        }
                    } else {
                        break;
                    }
                }

                if (keywordNum == 1) {

                    Lexer.log(PROGRAM_NUMBER, true, "Lexer", getKeywordType(currentTokenKeyword.symbol),
                            currentTokenKeyword.symbol, LINE_NUMBER, POSITION_NUMBER);
                    tokens.add(currentTokenKeyword);

                    POSITION_NUMBER = POSITION_NUMBER + currentTokenKeyword.symbol.length();

                    keywordNum = 0;
                } else {
                    Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentTokenID.lexemeName,
                            currentTokenID.symbol, LINE_NUMBER, POSITION_NUMBER);
                    tokens.add(currentTokenID);
                }
                strBuilder.delete(0, strBuilder.length());
                i = curPos;

            } else {
                if (isSYMBOL(currentCharacter)) {

                    if (strBuilder.isEmpty()) {
                        String tokenSymbol = "";
                        tokenSymbol += currentCharacter;
                        if (Objects.equals((getSymbolName(tokenSymbol)), "ASSIGN")) {
                            if (code.charAt(i+1) == '=') {
                                tokenSymbol += '=';
                                i++;
                                Tokens currentToken = new Tokens(getSymbolName(tokenSymbol), tokenSymbol, LINE_NUMBER, POSITION_NUMBER);
                                Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentToken.lexemeName, currentToken.symbol,
                                        currentToken.lineNum, currentToken.positionNum);

                                tokens.add(currentToken);
                            } else {
                                POSITION_NUMBER++;
                                Tokens currentToken = new Tokens(getSymbolName(tokenSymbol), tokenSymbol, LINE_NUMBER, POSITION_NUMBER);
                                Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentToken.lexemeName, currentToken.symbol,
                                        currentToken.lineNum, currentToken.positionNum);

                                tokens.add(currentToken);
                            }
                        } else if (Objects.equals((getSymbolName(tokenSymbol)), "EXCLAMATION_MARK")) {
                            if (code.charAt(i+1) == '=') {
                                tokenSymbol += '=';
                                i++;
                                Tokens currentToken = new Tokens(getSymbolName(tokenSymbol), tokenSymbol, LINE_NUMBER, POSITION_NUMBER);
                                Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentToken.lexemeName, currentToken.symbol,
                                        currentToken.lineNum, currentToken.positionNum);

                                tokens.add(currentToken);
                            }
                        } else if (Objects.equals(getSymbolName(tokenSymbol), "QUOTE")) {
                            String str = "";
                              for (int s = i+1; s < code.length(); s++) {
                                char curChar = code.charAt(s);

                                if (curChar == '"') {
                                    str += '\u0022';
                                    strBuilder.append(curChar);
                                    str += String.valueOf(strBuilder);
                                    i = s;
                                    POSITION_NUMBER = POSITION_NUMBER + str.length();

                                    break;
                                } else {
                                    strBuilder.append(curChar);
                                    if (code.indexOf(curChar) == code.length()-1) {
                                        System.out.println("DEBUG Lexer - WARNING: Cannot find the second/closing quotation mark!");
                                        str += '\u0022';
                                        str += String.valueOf(strBuilder);
                                        i = s;
                                    }
                                }
                            }
                            if (str == "") {
                                System.out.println("WARNING Lexer - Cannot find the second/closing quotation mark!");
                                str += '\u0022';
                            }

                            tokenSymbol = str;
                            Tokens currentToken = new Tokens("CHAR_LIST", tokenSymbol, LINE_NUMBER, POSITION_NUMBER);
                            Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentToken.lexemeName.toUpperCase(), currentToken.symbol,
                                    currentToken.lineNum, currentToken.positionNum);

                            tokens.add(currentToken);

                            strBuilder.delete(0,strBuilder.length());


                        } else if (Objects.equals(getSymbolName(tokenSymbol), "SLASH")) {
                            String comment = "";
                            strBuilder.append(currentCharacter);
                            for (int s = i+1; s < code.length(); s++) {
                                char curChar = code.charAt(s);

                                if (curChar == '*') {
                                    if (code.charAt(s+1) == '/') {

                                        strBuilder.append('*');
                                        strBuilder.append('/');

                                        comment = String.valueOf(strBuilder);
                                        strBuilder.delete(0, strBuilder.length());
                                        POSITION_NUMBER = POSITION_NUMBER + 2;
                                        i = s + 1;

                                        break;
                                    }
                                    else if (s == code.length() - 1) {
                                        error_count++;
                                        System.out.println("Unclosed comments");
                                    }
                                } else if (s == code.length() - 1) {
                                    error_count++;
                                    System.out.println("Error Lexer -  Unclosed comments");
                                    System.out.println("INFO Lexer   -  Lexing of program " + PROGRAM_NUMBER + " completed with " + error_count + " error(s)");
                                } else {
                                    strBuilder.append(curChar);

                                    POSITION_NUMBER++;
                                }
                            }
                        } else if (Objects.equals(getSymbolName(tokenSymbol), "EOP") ){
                            Tokens currentToken = new Tokens(getSymbolName(tokenSymbol), tokenSymbol, LINE_NUMBER, POSITION_NUMBER);
                            Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentToken.lexemeName, currentToken.symbol,
                                    currentToken.lineNum, currentToken.positionNum);

                            tokens.add(currentToken);



                            if (error_count == 0) {
                                System.out.println("INFO Lexer   -  Lexing of program " + PROGRAM_NUMBER + " completed with no errors");
                                // pass the tokens to the Parser
                                Parser.getTokens(PROGRAM_NUMBER, tokens);

                                Parser.init_Parser();
                            } else {
                                System.out.println("INFO Parser  - Lexing of program " + PROGRAM_NUMBER + " completed with " + error_count + " error(s)");

                                System.out.println("\nParser for program " + PROGRAM_NUMBER + " skipped due to Lexer ERROR");


                            }

                            if ( i == code.length() - 1) {
                                // do nothing
                                // Now this is the last $ sign
                                break;
                            } else {
                                PROGRAM_NUMBER++;

                                System.out.println("************************************************************" +
                                        "***************************************************************************" +
                                        "************************************\n ");
                                System.out.println("INFO Lexer   -   Lexing program " + PROGRAM_NUMBER + " ... ");

                                tokens.clear();
                            }
                        } else if (Objects.equals(getSymbolName(tokenSymbol), "ASTERISK")) {
                            error_count++;
                            System.out.println("ERROR Lexer -  Unrecognized Token [ " + currentCharacter + " ]  found at ("
                                    + LINE_NUMBER + " : " + POSITION_NUMBER + ")");

                        } else {
                            Tokens currentToken = new Tokens(getSymbolName(tokenSymbol), tokenSymbol, LINE_NUMBER, POSITION_NUMBER);
                            Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentToken.lexemeName, currentToken.symbol,
                                    currentToken.lineNum, currentToken.positionNum);

                            tokens.add(currentToken);

                            POSITION_NUMBER++;
                        }
                    }
                } else {
                    if (isDIGIT(currentCharacter)) {
                        if (strBuilder.isEmpty()) {
                            String tokenSymbol = "";
                            tokenSymbol += currentCharacter;

                            Tokens currentToken = new Tokens("DIGIT", tokenSymbol, LINE_NUMBER, POSITION_NUMBER);
                            Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentToken.lexemeName, currentToken.symbol,
                                    currentToken.lineNum, currentToken.positionNum);

                            tokens.add(currentToken);
                        }
                    }  else {
                        if (isWHITESPACE(currentCharacter)) {
                            // ignore and do nothing because it is a white space
                        } else {
                            error_count++;
                            System.out.println("ERROR Lexer -  Unrecognized Token [ " + currentCharacter + " ]  found at ("
                                    + LINE_NUMBER + " : " + POSITION_NUMBER + ")");
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

    }

    /**
     * This method reads the string file char by char
    */
    public static String readChars (String path) {

        int lastPosition = 0;
        int currentPosition = 0;
        Scanner scan = new Scanner(System.in);

        File file =new File(path);     //Creation of File Descriptor for input file
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
        code = code.trim();
        return code;
    }

    // This method identifies if a char is an ID
    public static boolean isID (char idCandidate) {
        String ID = "[a-z]";
        Pattern patternID = Pattern.compile(ID);
        Matcher matchID = patternID.matcher(String.valueOf(idCandidate));
        // code for recognizing id using RegEx
        if (matchID.matches()) {
            return true;
        }
        return false;
    }

    // This method identifies if a char is an SYMBOL
    public static boolean isSYMBOL (char symbolCandidate) {
        String SYMBOLS = "[$(){}=!+/*\".]";
        Pattern patternSymbols = Pattern.compile(SYMBOLS);
        Matcher matchSymbol = patternSymbols.matcher(String.valueOf(symbolCandidate));
        if (matchSymbol.matches()) {
            return true;
        }
        return false;
    }

    // This method identifies if a char is an DIGIT
    public static boolean isDIGIT (char digitCandidate) {
        String DIGIT = "0|([1-9][0-9]*)";
        Pattern patternDigit = Pattern.compile(DIGIT);
        Matcher matchDigits = patternDigit.matcher(String.valueOf(digitCandidate));
        // code for recognizing digit using RegEx
        if (matchDigits.matches()) {
            return true;
        }
        return false;
    }

    // This method identifies if a char is an DIGIT
    public static boolean isWHITESPACE (char whitespaceCandidate) {
        String WHITE_SPACE = "[\\n\\t\\s\\r ]+";
        Pattern patternWhiteSpace = Pattern.compile(WHITE_SPACE);
        Matcher matchWhiteSpace = patternWhiteSpace.matcher(String.valueOf(whitespaceCandidate));
        // code for recognizing whitespace using RegEx
        if (matchWhiteSpace.matches()) {
            return true;
        }
        return false;
    }

    // get the symbol token name from here
    public static String getSymbolName(String c) {
        String tokenName;
        switch (c) {
            case "$":
                tokenName = "EOP";
                break;
            case "(":
                tokenName = "LEFT_PAREN";
                break;
            case ")":
                tokenName = "RIGHT_PAREN";
                break;
            case "{":
                tokenName = "LEFT_BRACE";
                break;
            case "}":
                tokenName = "RIGHT_BRACE";
                break;
            case "=":
                tokenName = "ASSIGN";
                break;
            case "==":
                tokenName = "EQUAL_TO_OP";
                break;
            case "!=":
                tokenName = "NOT_EQUAL_TO_OP";
                break;
            case "+":
                tokenName = "PLUS";
                break;
            case "/":
                tokenName = "SLASH";
                break;
            case "*":
                tokenName = "ASTERISK";
                break;
            case "\"":
                tokenName = "QUOTE";
                break;
            case "!":
                tokenName = "EXCLAMATION_MARK";
                break;

            default:
                tokenName = "UNKNOWN";
        }
        return tokenName;
    }

    /** Concrete implementation for the log method    */
    public static void log(int progNum, Boolean debug, String compilerStage, String tokenName, String tokenSymbol,
                    int tokenLineNum, int tokenPosNum) {
        if (debug) {
            System.out.printf("INFO %-4s   -  %-16s [ %-16s ] found at (%-2d:%2d)\n", compilerStage, tokenName,
                    String.format("%" + 1 + "s%s%" + 1 + "s", "", tokenSymbol, ""), tokenLineNum, tokenPosNum);
        }
    }

    public static String getKeywordType(String c) {
        String tokenName;
        switch (c) {
            case "int":
                tokenName = "INT";
                break;
            case "string":
                tokenName = "STRING";
                break;
            case "boolean":
                tokenName = "BOOLEAN";
                break;
            case "false":
                tokenName = "FALSE";
                break;
            case "true":
                tokenName = "TRUE";
                break;
            case "while":
                tokenName = "WHILE";
                break;
            case "if":
                tokenName = "IF";
                break;
            case "print":
                tokenName = "PRINT";
                break;
            default:
                tokenName = "KEYWORD";
        }
        return tokenName;
    }

}