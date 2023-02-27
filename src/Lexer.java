import java.io.*;
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


    public static void main(String[] args) {
        init("C:\\Users\\Bashir\\Documents\\Bashirs_Code_all\\Java\\cmpt432\\src\\code.txt");
    }
    public static void init( String path) {

        System.out.println("\nINFO Lexer - Lexing program " + 1 + " ... ");
        /** start lexing the source code */
        scan(path);

    }

    /**
     * This method scans the source code and finds the lexemes or the valid words
     */
    public static void scan(String path) {

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
                        String KEYWORDS = "\\b(print|while|true|false|int|string|boolean)\\b";
                        Pattern patternKeyword = Pattern.compile("\\b" + KEYWORDS + "\\b", Pattern.CASE_INSENSITIVE);
                        Matcher matchKeyword = patternKeyword.matcher(String.valueOf(strBuilder));

                        if (matchKeyword.matches()) {
                            currentTokenKeyword = new Tokens("KEYWORD", matchKeyword.group(), LINE_NUMBER, POSITION_NUMBER);
                            keywordNum++;
                            curPos = j;
                        }
                    } else {
                        break;
                    }
                }

                if (keywordNum != 0) {
                    POSITION_NUMBER = POSITION_NUMBER + currentTokenKeyword.lexemeName.length();
                    Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentTokenKeyword.lexemeName,
                            currentTokenKeyword.symbol, LINE_NUMBER, POSITION_NUMBER);
                    keywordNum = 0;
                } else {
                    POSITION_NUMBER++;
                    Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentTokenID.lexemeName,
                            currentTokenID.symbol, LINE_NUMBER, POSITION_NUMBER);
                }
                strBuilder.delete(0, strBuilder.length());
                i = curPos;

            } else {
                if (isSYMBOL(currentCharacter)) {

                    // TODO if quote then read ahead until another quote appears, then consider the entire thing to be a string
                    if (strBuilder.isEmpty()) {
                        String tokenSymbol = "";
                        tokenSymbol += currentCharacter;
                        if (Objects.equals((getTokenName(tokenSymbol)), "ASSIGNMENT_OP")) {
                            if (code.charAt(i+1) == '=') {
                                tokenSymbol += '=';
                                i++;
                            }
                        } else if (Objects.equals(getTokenName(tokenSymbol), "NOT_EQUAL_TO")) {
                            if (code.charAt(i+1) == '=') {
                                tokenSymbol += '=';
                                i++;
                            }
                        } else if (Objects.equals(getTokenName(tokenSymbol), "QUOTE")) {
                            String str = "";
                            str += '\u0022';
                            for (int s = i+1; s < code.length(); s++) {
                                char curChar = code.charAt(s);

                                if (curChar == '"') {
                                    str += String.valueOf(strBuilder);
                                    i = s-2;
                                    POSITION_NUMBER = POSITION_NUMBER + str.length();
                                    break;
                                } else {
                                    strBuilder.append(curChar);
                                }
                            }
                            str += '"';
                            tokenSymbol = str;
                        } else if (Objects.equals(getTokenName(tokenSymbol), "SLASH")) {
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
                                        Tokens currentToken = new Tokens("COMMENT", comment, LINE_NUMBER, POSITION_NUMBER);
                                        Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentToken.lexemeName, currentToken.symbol,
                                                currentToken.lineNum, currentToken.positionNum);
                                        break;
                                    }
                                }
                                strBuilder.append(curChar);
                                POSITION_NUMBER++;
                            }
                        } else if (Objects.equals(getTokenName(tokenSymbol), "EOP") ){

                            if( i == code.length() - 1 ) {
                                // do nothing
                                // Now this is the last $ sign
                                break;
                            } else {
                                PROGRAM_NUMBER++;
                                System.out.println("\n\nINFO Lexer - Lexing program " + PROGRAM_NUMBER + " ... ");
                            }
                        }
                        else {
                            POSITION_NUMBER++;
                            Tokens currentToken = new Tokens(getTokenName(tokenSymbol), tokenSymbol, LINE_NUMBER, POSITION_NUMBER);
                            Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentToken.lexemeName, currentToken.symbol,
                                    currentToken.lineNum, currentToken.positionNum);
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
                        }
                    }  else {
                        if (isWHITESPACE(currentCharacter)) {
                            // ignore and do nothing because it is a white space
                        } else {
                            System.out.println("ERROR Lexer - Unrecognized Token  found at ("
                                    + LINE_NUMBER + ":" + POSITION_NUMBER + ")");
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
        return code;
    }

    // This method identifies if a char is an ID
    public static boolean isID (char idCandidate) {
        String ID = "[a-z][a-z0-9]*";
        Pattern patternID = Pattern.compile(ID, Pattern.CASE_INSENSITIVE);
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
        Pattern patternSymbols = Pattern.compile(SYMBOLS, Pattern.CASE_INSENSITIVE);
        Matcher matchSymbol = patternSymbols.matcher(String.valueOf(symbolCandidate));
        if (matchSymbol.matches()) {
            return true;
        }
        return false;
    }

    // This method identifies if a char is an DIGIT
    public static boolean isDIGIT (char digitCandidate) {
        String DIGIT = "0|([1-9][0-9]*)";
        Pattern patternDigit = Pattern.compile(DIGIT, Pattern.CASE_INSENSITIVE);
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
        Pattern patternWhiteSpace = Pattern.compile(WHITE_SPACE, Pattern.CASE_INSENSITIVE);
        Matcher matchWhiteSpace = patternWhiteSpace.matcher(String.valueOf(whitespaceCandidate));
        // code for recognizing whitespace using RegEx
        if (matchWhiteSpace.matches()) {
            return true;
        }
        return false;
    }

    // get the symbol token name from here
    public static String getTokenName(String c) {
        String tokenName;
        switch (c) {
            case "$":
                tokenName = "EOP";
                break;
            case "(":
                tokenName = "LEFT_PARENTHESIS";
                break;
            case ")":
                tokenName = "RIGHT_PARENTHESIS";
                break;
            case "{":
                tokenName = "LEFT_BRACE";
                break;
            case "}":
                tokenName = "RIGHT_BRACE";
                break;
            case "=":
                tokenName = "ASSIGNMENT_OP";
                break;
            case "==":
                tokenName = "EQUAL_TO_OP";
                break;
            case "!=":
                tokenName = "NOT_EQUAL_TO";
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
            default:
                tokenName = "string";
        }
        return tokenName;
    }

    /** Concrete implementation for the log method    */
    public static void log(int progNum, Boolean debug, String compilerStage, String tokenName, String tokenSymbol,
                    int tokenLineNum, int tokenPosNum) {
        if (debug) {
            System.out.println("DEBUG " + compilerStage + " - " + tokenName + " [ " + tokenSymbol + " ] found at ("
                                + tokenLineNum + ":" + tokenPosNum + ")");
        }
    }

}
