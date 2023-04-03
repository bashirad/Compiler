import java.util.*;

public class Parser {
    static String TOKEN = "";
    private static int token_pointer;
    private static String current_token;

    public static void main(String[] args) {

        /**
         * Call init_Parser and execute the Parser in the Run environment
         */
        init_Parser("C:\\Users\\Bashir\\Documents\\Bashirs_Code_all\\Java\\cmpt432\\src\\code.txt");

    }

    public static void init_Parser( String path) {

        /**
         * Call getStreamTokens here and then pass the stream of tokens
         */
        List<Tokens> tokens = Lexer.passTokens(path);

        System.out.println("\nINFO Parser - Parsing program " + 1 + " ... ");

        for (int i = token_pointer; i < tokens.size(); i++) {
            Tokens token = tokens.get(i);
            current_token = token.getLexemeName();

            /**
             * Perform any other operations with currentToken here
             */

            // TODO write code to start parsing
            // System.out.println(current_token);

            /**
             * start parsing the source code
             */
            parseProgram(current_token);


        }

    }

    /**
     match method to check currentToken to the expectedToken
     */
    public static void match(String cur_tok, String expected_token) {
        if (Objects.equals(current_token, expected_token)) {
            System.out.println("CORRECT: expected the " + expected_token + " and found the " + current_token);
            token_pointer++;
            if (token_pointer < Lexer.tokens.size()) {
                current_token = Lexer.tokens.get(token_pointer).getLexemeName();
            }
        }
        else {
            System.out.println("WRONG: expected the " + expected_token + " but found the " + current_token);
        }
    }

    /**
     Procedure to parse Program
     */
    public static void parseProgram(String current_token) {
        parseBlock(current_token);
        /**
         * EOP is not part of the program unless it is separating two programs
         */
        //match(current_token, "EOP"); // end of program
    }

    /**
     Procedure to parse Block
    */
    public static void parseBlock(String current_token) {
        match(current_token,"LEFT_BRACE");
        parseStatementList(current_token);
        match(current_token, "RIGHT_BRACE");
    }

    /**
    Procedure to parse StatementList
    */
    public static void parseStatementList(String current_token) {

        Set<String> validTokens = new HashSet<>(Arrays.asList("PRINT", "ASSIGN", "ID", "WHILE", "IF", "RIGHT_BRACE"));

        if (validTokens.contains(current_token)) {
            parseStatement(current_token);
        }
    }
    public static void parseStatement(String current_token) {

        if (Objects.equals(current_token, "PRINT")) {
            parsePrintStatement(current_token);
        } else if (Objects.equals(current_token, "ASSIGN")) {
            parseAssignmentStatement(current_token);
        } else if (Objects.equals(current_token, "ID")) {
            parseVarDecl(current_token);
        } else if (Objects.equals(current_token, "WHILE")) {
            parseWhileStatement(current_token);
        } else if (Objects.equals(current_token, "IF")) {
            parseIfStatement(current_token);
        } else if (Objects.equals(current_token, "RIGHT_BRACE")) {
            /**
             * Parse the right brace to denote the end of the block
             */
            match(current_token, "RIGHT_BRACE");
        } else {
            error(current_token);
        }
    }

    public static void parsePrintStatement(String current_token) {
        match(current_token, "PRINT");
        match(current_token, "LEFT_PARENTHESIS");
        parseExpr(current_token);
        match(current_token, "RIGHT_PARENTHESIS");
    }

    public static void parseAssignmentStatement(String current_token) {
        parseId(current_token);
        match(current_token,"ASSIGN");
        parseExpr(current_token);
    }

    public static void parseVarDecl(String current_token) {
        parseType(current_token);
        parseId(current_token);
    }

    public static void parseWhileStatement(String current_token) {
        match(current_token, "WHILE");
        parseBooleanExpr(current_token);
        parseBlock(current_token);
    }

    public static void parseIfStatement(String current_token) {
        match(current_token,"IF");
        parseBooleanExpr(current_token);
        parseBlock(current_token);
    }

    public static void parseExpr(String current_token) {
        if (Objects.equals(current_token, "INT_I-TYPE")) {
            parseIntExpr(current_token);
        } else if (Objects.equals(current_token, "STRING_I_TYPE")) {
            parseStringExpr(current_token);
        } else if (Objects.equals(current_token, "BOOL_I_TYPE")) {
            parseBooleanExpr(current_token);
        } else if (Objects.equals(current_token, "ID")) {
            parseId(current_token);
        } else {
            error(current_token);
        }
    }

    public static void parseIntExpr(String current_token) {
        if (Objects.equals(current_token, "DIGIT")) {
            match(current_token,"DIGIT");
        } else {
            match(current_token, "DIGIT");
            parseIntOp(current_token);
            parseExpr(current_token);
        }
    }

    public static void parseStringExpr(String current_token) {
        match(current_token,"STRING");
    }

    public static void parseBooleanExpr(String current_token) {
        if (current_token == "LEFT_PARENTHESIS") {
            match(current_token,"LEFT_PARENTHESIS");
            parseExpr(current_token);
            parseBoolOp(current_token);
            parseExpr(current_token);
            match(current_token,"RIGHT_PARENTHESIS");
        } else {
            parseBoolVal(current_token);
        }
    }

    public static void parseId(String current_token) {
        match(current_token,"ID");
    }

    // TODO delete this method when done as you are not separating the string into char list
    /* public static void parseCharList(String current_token) {
        if (current_token == "CHAR") {
            match(current_token,"char");
            parseCharList(current_token);
        } else if (current_token == "WHITESPACE") {
            match(current_token," ");
            parseCharList(current_token);
        }
    }*/

    public static void parseType(String current_token) {
        if (current_token == "INT_I_TYPE") {
            match(current_token,"INT_I_TYPE");
        } else if (current_token == "STRING_I_TYPE") {
            match(current_token,"STRING_I_TYPE");
        } else if (current_token == "BOOL_I_TYPE") {
            match(current_token,"BOOL_I_TYPE");
        } else {
            error(current_token);
        }
    }

    public static void parseBoolOp(String current_token) {
        if (Objects.equals(current_token, "EQUAL_TO_OP")) {
            match(current_token,"EQUAL_TO_OP");
        } else {
            match(current_token,"NOT_EQUAL_TO_OP");
        }
    }

    public static void parseBoolVal(String current_token) {
        if (Objects.equals(current_token, "FALSE_BOOL_VAL")) {
            match(current_token,"FALSE_BOOL_VAL");
        } else if (Objects.equals(current_token, "TRUE_BOOL_VAL")){
            match(current_token,"TRUE_BOOL_VAL");
            error( current_token);
        }
    }

    public static void parseIntOp(String current_token) {
        match(current_token,"PLUS");
    }

    public static void error(String current_token) {
        System.out.println("there is an error. You can check later!");
    }

}
