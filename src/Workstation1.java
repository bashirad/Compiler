import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// TODO 1. develop the code the will run the lexer here
// TODO 2. make use of RegEx
public class Workstation1 {

    private static int LINE_NUMBER = 1;
    private static int POSITION_NUMBER = 0;
    private static Tokens currentTokenID;
    private static Tokens currentTokenKeyword;


    public static void main(String[] args ) {
        init();
    }

    public static void init() {
        scan();
    }

    public static void scan() {

        int curPos;
        int beginPos = 0;
        int endPos = 0;
        int keywordNum = 0;

        // start wth keywords and create empty string and append the char
        String code = readChars();

        String TOKEN_CANDIDATE = "";
        StringBuilder strBuilder = new StringBuilder(TOKEN_CANDIDATE);

        // stream the characters in the string
        for (int currentPosition = 0; currentPosition < code.length(); currentPosition++) {
            char currentCharacter = code.charAt(currentPosition);
            //System.out.println(ch);


            int PROGRAM_NUMBER = 1;

            if (isID(currentCharacter)) {
                String tokenSymbol = "";
                tokenSymbol += currentCharacter;
                currentTokenID = new Tokens("ID", tokenSymbol, LINE_NUMBER, POSITION_NUMBER);

                curPos = code.indexOf(currentCharacter);

                //TODO Look ahead happens here;
                for(int i = code.indexOf(currentCharacter); i < code.length(); i++) {
                    char curChar = code.charAt(i);
                    if (!isWHITESPACE(curChar) && !isSYMBOL(curChar) && !isDIGIT(curChar)){

                        strBuilder.append(curChar);
                        beginPos = code.indexOf(currentCharacter);
                        endPos = 0;

                        // code for recognizing keywords using RegEx
                        String KEYWORDS = "\\b(print|while|true|false|int|string|boolean)\\b";
                        Pattern patternKeyword = Pattern.compile("\\b" + KEYWORDS + "\\b", Pattern.CASE_INSENSITIVE);
                        Matcher matchKeyword = patternKeyword.matcher(String.valueOf(strBuilder));

                        if (matchKeyword.matches()) {
                            currentTokenKeyword = new Tokens("KEYWORD", matchKeyword.group(), LINE_NUMBER, POSITION_NUMBER);
                            endPos = i;
                            keywordNum++;
                            curPos = code.indexOf(curChar);
                        }
                    } else {
                        strBuilder.delete(0, strBuilder.length());
                        break;
                    }


                }
                if (keywordNum != 0) {
                    Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentTokenKeyword.lexemeName,
                            currentTokenKeyword.symbol, LINE_NUMBER, POSITION_NUMBER);
                }
                else {
                    Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentTokenID.lexemeName,
                            currentTokenID.symbol, LINE_NUMBER, POSITION_NUMBER);
                    curPos++;
                }
                currentPosition = curPos;
            } else {
                if (isSYMBOL(currentCharacter)) {

                    if (strBuilder.isEmpty()) {
                        String tokenSymbol = "";
                        tokenSymbol += currentCharacter;

                        Tokens currentToken = new Tokens("SYMBOLS", tokenSymbol, LINE_NUMBER, POSITION_NUMBER);
                        // TODO might not need to log here
                        Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentToken.lexemeName, currentToken.symbol,
                                currentToken.lineNum, currentToken.positionNum);
                    }
                } else {
                    if (isDIGIT(currentCharacter)) {
                        if (strBuilder.isEmpty()) {
                            String tokenSymbol = "";
                            tokenSymbol += currentCharacter;

                            Tokens currentToken = new Tokens("DIGIT", tokenSymbol, LINE_NUMBER, POSITION_NUMBER);
                            // TODO might not need to log here
                            Lexer.log(PROGRAM_NUMBER, true, "Lexer", currentToken.lexemeName, currentToken.symbol,
                                    currentToken.lineNum, currentToken.positionNum);
                        }
                    } else {
                        if (isWHITESPACE(currentCharacter)) {
                        String tokenSymbol = "";
                        tokenSymbol += currentCharacter;

                        Tokens currentToken = new Tokens("WHITE_SPACE", "' '", LINE_NUMBER, POSITION_NUMBER);
                        Lexer.log(PROGRAM_NUMBER, false, "Lexer", currentToken.lexemeName, currentToken.symbol,
                                currentToken.lineNum, currentToken.positionNum);
                    } else {
                        System.out.println("There is an error");
                    }
                }

                // this logs the ID or Keyword that were last found
                /*Lexer.log(PROGRAM_NUMBER, false, "Lexer", currentToken.lexemeName, currentToken.symbol,
                        currentToken.lineNum, currentToken.positionNum);*/

                POSITION_NUMBER++;
                if ('\n' == code.charAt(currentPosition)) {
                    LINE_NUMBER++;
                    POSITION_NUMBER = 0;
                }

            }
        }
    }

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
    // This method reads the string file char by char
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
