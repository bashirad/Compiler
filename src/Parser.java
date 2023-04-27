import java.util.*;

public class Parser extends Tree{
    private static int PROGRAM_NUMBER = 0;
    private static ArrayList<Tokens> tokens;
    private static int token_pointer;
    private static String current_token;
    private static Tokens token = null;
    private static final Tree myCST = new Tree();
    private static int error_count = 0;
    private static final ArrayList<String> list_expected_strings = new ArrayList<>();

    public static void main(String[] args) {

        /**
         * Call init_Parser and execute the Parser in the Run environment
         */

        // TODO figure out a way to pass tokens to parser at the end of lexing each program
        //init_Parser("C:\\Users\\Bashir\\Documents\\Bashirs_Code_all\\Java\\cmpt432\\src\\code.txt");
        Parser.init_Parser();
    }

    public static void init_Parser() {

        /**
         * Call the Lexer here
         */
        //Lexer.init_Lexer("C:\\\\Users\\\\Bashir\\\\Documents\\\\Bashirs_Code_all\\\\Java\\\\cmpt432\\\\src\\\\code.txt");

        System.out.println("\nINFO Parser  -  Parsing program " + PROGRAM_NUMBER + " ... ");

        token_pointer = 0;
        token = tokens.get(token_pointer);
        current_token = token.getLexemeName();

        System.out.println("INFO Parser  -  init_Parser()");

        /**
         * start parsing the source code
         */
        System.out.println("INFO Parser  -  parseProgram()");
        Parser myParser = new Parser();
        myParser.parseProgram();

        /**
         * if no errors, parsing is successful
         */
        if (error_count == 0) {
            System.out.println("INFO Parser  -  Parsing of program " + PROGRAM_NUMBER + " completed with " + error_count + " error(s)");

            /**
             * print the CST
             */
            System.out.println("\n CST for program " + PROGRAM_NUMBER + " ...\n");

            Tree tree = new Tree();
            tree.print(myCST);
        } else {
            System.out.println("INFO Parser  -  Parsing of program " + PROGRAM_NUMBER + " completed with " + error_count + " error(s)");

            System.out.println("\nCST for program " + PROGRAM_NUMBER + " skipped due to Parser ERROR");
        }


    }

    /**
     * match method to check currentToken to the expectedToken
     */
    public void match(String cur_tok, String expected_token) {

        if (Objects.equals(current_token, expected_token)) {

            //System.out.printf("DEBUG Parser - CORRECT: expected    %-14s and found     %s \n", expected_token, current_token + " at line " + token.getLineNum() + " position " + token.getPosNum());

            myCST.addNode(token.getSymbol(), "leaf"); // leaf, expected_token

            token_pointer++;

            if (token_pointer < Lexer.tokens.size()) {
                token = Lexer.tokens.get(token_pointer);
                current_token = Lexer.tokens.get(token_pointer).getLexemeName();
            }
            // System.out.printf("DEBUG Parser - WRONG:   expected    %-6s    and instead   %s \n", expected_token, "RAN OUT OF TOKENS");

        } else {
            error_count++;
            System.out.printf("ERROR Parser -  expected %s and found  %s [ %-2s] at (%d : %d)\n", expected_token, current_token, token.getSymbol(), token.getLineNum(), token.getPosNum());
        }
    }

    /**
     * Procedures to parse Program
     */
    public void parseProgram() {
        myCST.addNode("program", "root" );

        System.out.println("INFO Parser  -  parseBlock()");
        parseBlock();

        //myCST.moveUp();
        /**
         * EOP is not part of the program unless it is separating two programs
         */
        match(current_token, "EOP"); // end of program
    }

    public void parseBlock() {
        myCST.addNode("Block", "branch");

        match(current_token, "LEFT_BRACE");

        System.out.println("INFO Parser  -  parseBlock()");
        parseStatementList();

        match(current_token, "RIGHT_BRACE");

        myCST.moveUp();
    }

    public void parseStatementList() {

        Set<String> validTokens =
                new HashSet<>(Arrays.asList("PRINT", "ID", "INT", "STRING", "BOOLEAN", "WHILE", "IF", "LEFT_BRACE"));

        if (validTokens.contains(current_token)) {
            // I printed this here because the current StatementList could be empty
            System.out.println("INFO Parser  -  parseStatementList()");

            myCST.addNode("StatementList", "branch");

            System.out.println("INFO Parser  -  parseStatement()");
            parseStatement();

            parseStatementList();

            myCST.moveUp();
        }

    }

    public void parseStatement() {
        myCST.addNode("Statement", "branch");

        list_expected_strings.clear();
        list_expected_strings.addAll(Arrays.asList("PRINT", "ID", "WHILE", "IF", "LEFT_BRACE"));


        // TODO order the following like our grammar paper orders them
        if (Objects.equals(current_token, "PRINT")) {
            System.out.println("INFO Parser  -  parsePrintStatement()");
            parsePrintStatement();
        } else if (Objects.equals(current_token, "ID")) {
            System.out.println("INFO Parser  -  parseAssignmentStatement()");
            parseAssignmentStatement();
        } else if (Objects.equals(current_token, "INT")
                || Objects.equals(current_token, "STRING")
                || Objects.equals(current_token, "BOOLEAN")) {
            System.out.println("INFO Parser  -  parseVarDecl()");
            parseVarDecl();
        } else if (Objects.equals(current_token, "WHILE")) {
            System.out.println("INFO Parser  -  parseWhileStatement()");
            parseWhileStatement();
        } else if (Objects.equals(current_token, "IF")) {
            System.out.println("INFO Parser  -  parseIfStatement()");
            parseIfStatement();
        } else if (Objects.equals(current_token, "LEFT_BRACE")) {
            System.out.println("INFO Parser  -  parseBlock()");
            parseBlock();
        } else {
            error(list_expected_strings);
        }

        myCST.moveUp();
    }

    public void parsePrintStatement() {
        myCST.addNode("PrintStatement", "branch");

        match(current_token, "PRINT");
        match(current_token, "LEFT_PAREN");
        System.out.println("INFO Parser  -  parseExpr()");
        parseExpr();
        match(current_token, "RIGHT_PAREN");

        myCST.moveUp();
    }

    public void parseAssignmentStatement() {
        myCST.addNode("AssignmentStatement", "branch");

        System.out.println("INFO Parser  -  parseId()");
        parseId();

        match(current_token, "ASSIGN");

        System.out.println("INFO Parser  -  parseExpr()");
        parseExpr();

        myCST.moveUp();
    }

    public void parseVarDecl() {
        myCST.addNode("VarDecl", "branch");

        System.out.println("INFO Parser  -  parseType()");
        parseType();

        System.out.println("INFO Parser  -  parseId()");
        parseId();

        myCST.moveUp();
    }

    public void parseWhileStatement() {
        myCST.addNode("WhileStatement", "branch");


        match(current_token, "WHILE");

        System.out.println("INFO Parser  -  parseBooleanExpr()");
        parseBooleanExpr();

        System.out.println("INFO Parser  -  parseBlock()");
        parseBlock();

        myCST.moveUp();
    }

    public void parseIfStatement() {
        myCST.addNode("IfStatement", "branch");

        match(current_token, "IF");

        System.out.println("INFO Parser  -  parseBooleanExpr()");
        parseBooleanExpr();

        System.out.println("INFO Parser  -  parseBlock()");
        parseBlock();

        myCST.moveUp();
    }

    public void parseExpr() {
        myCST.addNode("Expr", "branch");

        list_expected_strings.clear();
        list_expected_strings.addAll(Arrays.asList("DIGIT", "STRING", "BOOLEAN", "ID"));

        if (Objects.equals(current_token, "DIGIT")) {
            System.out.println("INFO Parser  -  parseIntExpr()");
            parseIntExpr();
        } else if (Objects.equals(current_token, "STRING")) {
            System.out.println("INFO Parser  -  parseStringExpr()");
            parseStringExpr();
        } else if (Objects.equals(current_token, "LEFT_PAREN")
                || Objects.equals(current_token, "TRUE")
                || Objects.equals(current_token, "FALSE")) {
            System.out.println("INFO Parser  -  parseBooleanExpr()");
            parseBooleanExpr();
        } else if (Objects.equals(current_token, "ID")) {
            System.out.println("INFO Parser  -  parseId()");
            parseId();
        } else {
            error(list_expected_strings);
        }

        myCST.moveUp();
    }

    public void parseIntExpr() {
        myCST.addNode("IntExpr", "branch");

        match(current_token, "DIGIT");
        if (Objects.equals(current_token, "PLUS")) {
            System.out.println("INFO Parser  -  parseIntOp()");
            parseIntOp();

            System.out.println("INFO Parser  -  parseExpr()");
            parseExpr();
        }

        myCST.moveUp();
    }

    public void parseStringExpr() {
        myCST.addNode("StringExpr", "branch");

        match(current_token, "STRING");

        myCST.moveUp();
    }

    public void parseBooleanExpr() {
        myCST.addNode("BooleanExpr", "branch");

        if (Objects.equals(current_token, "FALSE")
                || Objects.equals(current_token, "TRUE")) {
            System.out.println("INFO Parser  -  parseBoolVal()");
            parseBoolVal();
        } else {
            match(current_token, "LEFT_PAREN");
            System.out.println("INFO Parser  -  parseExpr()");
            parseExpr();
            System.out.println("INFO Parser  -  parseBoolOp()");
            parseBoolOp();
            System.out.println("INFO Parser  -  parseExpr()");
            parseExpr();
            match(current_token, "RIGHT_PAREN");
        }

        myCST.moveUp();
    }

    public void parseId() {
        myCST.addNode("Id", "branch");

        match(current_token, "ID");

        myCST.moveUp();
    }

    public void parseType() {
        myCST.addNode("Type", "branch");

        list_expected_strings.clear();
        list_expected_strings.addAll(Arrays.asList("INT", "STRING", "BOOLEAN"));

        if (Objects.equals(current_token, "INT")) {
            match(current_token, "INT");
        } else if (Objects.equals(current_token, "STRING")) {
            match(current_token, "STRING");
        } else if (Objects.equals(current_token, "BOOLEAN")) {
            match(current_token, "BOOLEAN");
        } else {
            error(list_expected_strings);
        }

        myCST.moveUp();
    }

    public void parseBoolOp() {
        myCST.addNode("BoolOp", "branch");

        if (Objects.equals(current_token, "EQUAL_TO_OP")) {
            match(current_token, "EQUAL_TO_OP");
        } else {
            match(current_token, "NOT_EQUAL_TO_OP");
        }

        myCST.moveUp();
    }

    public void parseBoolVal() {
        myCST.addNode("BoolVal", "branch");

        list_expected_strings.clear();
        list_expected_strings.addAll(Arrays.asList("False", "TRUE"));

        if (Objects.equals(current_token, "FALSE")) {
            match(current_token, "FALSE");
        } else if (Objects.equals(current_token, "TRUE")) {
            match(current_token, "TRUE");
        } else {
            System.out.println("INFO Parser  -  error()");
            error(list_expected_strings);
        }

        myCST.moveUp();
    }

    public void parseIntOp() {
        myCST.addNode("IntOp", "branch");

        match(current_token, "PLUS");

        myCST.moveUp();
    }

    public void error(ArrayList<String> list_expected_tokens) {
        System.out.println("expected one of these " + list_expected_tokens.toString() + " but found " + current_token + " at line " + token.getLineNum() + " position " + token.getPosNum());

        // TODO : think about keeping or removing the following
        error_count++;
        token_pointer = Lexer.tokens.size();
    }

    public static void getTokens(int program_number, ArrayList<Tokens> toks) {

        tokens = toks;
        PROGRAM_NUMBER = program_number;
    }

    // get the symbol from the token name using this method
    public static String getSymbol(String c) {
        return switch (c) {
            case "EOP" -> "$";
            case "LEFT_PAREN" -> "(";
            case "RIGHT_PAREN" -> ")";
            case "LEFT_BRACE" -> "{";
            case "RIGHT_BRACE" -> "}";
            case "ASSIGN" -> "=";
            case "EQUAL_TO_OP" -> "==";
            case "NOT_EQUAL_TO_OP" -> "!=";
            case "PLUS" -> "+";
            case "SLASH" -> "/";
            case "ASTERISK" -> "*";
            case "QUOTE" -> "\"";
            case "EXCLAMATION_MARK" -> "!";
            default -> "UNKNOWN";
        };
    }


}


