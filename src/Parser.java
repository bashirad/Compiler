import java.util.*;

public class Parser extends Tree{
    private static int PROGRAM_NUMBER = 0;
    private static ArrayList<Tokens> tokens;
    private static int token_pointer;
    private static String current_token;
    private static Tokens token = null;
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

        System.out.println("\nINFO Parser - Parsing program " + PROGRAM_NUMBER + " ... ");

        Tree myTree = new Tree();

        token_pointer = 0;
        token = tokens.get(token_pointer);
        current_token = token.getLexemeName();

        System.out.println("Parser: init_Parser()");

        /**
         * start parsing the source code
         */
        System.out.println("Parser: parseProgram()");
        Parser myParser = new Parser();
        myParser.parseProgram();

        /**
         * if no errors, parsing is successful
         */
        if (error_count == 0) {
            System.out.println("DEBUG Parser - Parsing of program " + PROGRAM_NUMBER + " completed with no errors");
            /**
             * print the CST
             */

            PrintCST cstPrinter = new PrintCST(myTree);
            cstPrinter.print();
        }


    }

    /**
     * match method to check currentToken to the expectedToken
     */
    public void match(String cur_tok, String expected_token) {

        if (Objects.equals(current_token, expected_token)) {

            //System.out.printf("DEBUG Parser - CORRECT: expected    %-14s and found     %s \n", expected_token, current_token);

            addNode("leaf", token.getSymbol()); // leaf, expected_token

            token_pointer++;

            if (token_pointer < Lexer.tokens.size()) {
                token = Lexer.tokens.get(token_pointer);
                current_token = Lexer.tokens.get(token_pointer).getLexemeName();
            }
            // System.out.printf("DEBUG Parser - WRONG:   expected    %-6s    and instead   %s \n", expected_token, "RAN OUT OF TOKENS");

        } else {
            error_count++;
            //System.out.printf("DEBUG Parser - WRONG:   expected    %-14s and found     %s \n", expected_token, current_token);
        }
    }

    /**
     * Procedure to parse Program
     */
    public void parseProgram() {
        addNode("root", "program");

        System.out.println("Parser: parseBlock()");
        parseBlock();

        moveUp();
        /**
         * EOP is not part of the program unless it is separating two programs
         */
        //match(current_token, "EOP"); // end of program
    }

    /**
     * Procedure to parse Block
     */
    public void parseBlock() {
        addNode("branch", "block");

        this.match(current_token, "LEFT_BRACE");
        parseStatementList();
        match(current_token, "RIGHT_BRACE");

        moveUp();
    }

    /**
     * Procedure to parse StatementList
     */
    public void parseStatementList() {
        addNode("branch", "statementList");

        Set<String> validTokens =
                new HashSet<>(Arrays.asList("PRINT", "ASSIGN", "INT", "STRING", "BOOLEAN", "WHILE", "IF", "LEFT_BRACE"));

        if (validTokens.contains(current_token)) {
            System.out.println("Parser: parseStatement()");
            parseStatement();
            System.out.println("Parser: parseStatementList()");
            parseStatementList();
        }

        moveUp();
    }

    public void parseStatement() {
        addNode("branch", "statement");

        list_expected_strings.clear();
        list_expected_strings.addAll(Arrays.asList("PRINT", "ASSIGN", "ID", "WHILE", "IF", "LEFT_BRACE"));


        if (Objects.equals(current_token, "PRINT")) {
            System.out.println("Parser: parsePrintStatement()");
            parsePrintStatement();
        } else if (Objects.equals(current_token, "ASSIGN")) {
            System.out.println("Parser: parseAssignmentStatement()");
            parseAssignmentStatement();
        } else if (Objects.equals(current_token, "INT")
                || Objects.equals(current_token, "STRING")
                || Objects.equals(current_token, "BOOLEAN")) {
            System.out.println("Parser: parseVarDecl()");
            parseVarDecl();
        } else if (Objects.equals(current_token, "WHILE")) {
            System.out.println("Parser: parseWhileStatement()");
            parseWhileStatement();
        } else if (Objects.equals(current_token, "IF")) {
            System.out.println("Parser: parseIfStatement()");
            parseIfStatement();
        } else if (Objects.equals(current_token, "LEFT_BRACE")) {
            System.out.println("Parser: parseBlock()");
            parseBlock();
        } else {
            error(list_expected_strings);
        }

        moveUp();
    }

    public void parsePrintStatement() {
        addNode("branch", "printStatement");

        match(current_token, "PRINT");
        match(current_token, "LEFT_PAREN");
        System.out.println("Parser: parseExpr()");
        parseExpr();
        match(current_token, "RIGHT_PAREN");

        moveUp();
    }

    public void parseAssignmentStatement() {
        addNode("branch", "assignmentStatement");

        System.out.println("Parser: parseId()");
        parseId();
        match(current_token, "ASSIGN");
        System.out.println("Parser: parseExpr()");
        parseExpr();

        moveUp();
    }

    public void parseVarDecl() {
        addNode("branch", "varDecl");


        System.out.println("Parser: parseType()");
        parseType();
        System.out.println("Parser: parseId()");
        parseId();

        moveUp();
    }

    public void parseWhileStatement() {
        addNode("branch", "whileStatement");


        match(current_token, "WHILE");
        System.out.println("Parser: parseBooleanExpr()");
        parseBooleanExpr();
        System.out.println("Parser: parseBlock()");
        parseBlock();

        moveUp();
    }

    public void parseIfStatement() {
        addNode("branch", "ifStatement");


        match(current_token, "IF");
        System.out.println("Parser: parseBooleanExpr()");
        parseBooleanExpr();
        System.out.println("Parser: parseBlock()");
        parseBlock();

        moveUp();
    }

    public void parseExpr() {
        addNode("branch", "expr");

        list_expected_strings.clear();
        list_expected_strings.addAll(Arrays.asList("DIGIT", "STRING", "BOOLEAN", "ID"));

        if (Objects.equals(current_token, "DIGIT")) {
            System.out.println("Parser: parseIntExpr()");
            parseIntExpr();
        } else if (Objects.equals(current_token, "STRING")) {
            System.out.println("Parser: parseStringExpr()");
            parseStringExpr();
        } else if (Objects.equals(current_token, "LEFT_PAREN")
                || Objects.equals(current_token, "TRUE")
                || Objects.equals(current_token, "FALSE")) {
            System.out.println("Parser: parseBooleanExpr()");
            parseBooleanExpr();
        } else if (Objects.equals(current_token, "ID")) {
            System.out.println("Parser: parseId()");
            parseId();
        } else {
            System.out.println("Parser: error()");
            error(list_expected_strings);
        }

        moveUp();
    }

    public void parseIntExpr() {
        addNode("branch", "intExpr");

        match(current_token, "DIGIT");
        if (Objects.equals(current_token, "PLUS")) {
            System.out.println("Parser: parseIntOp()");
            parseIntOp();
            System.out.println("Parser: parseExpr()");
            parseExpr();
        }

        moveUp();
    }

    public void parseStringExpr() {
        addNode("branch", "stringExpr");

        System.out.println("Parser: parseStringExpr()");
        match(current_token, "STRING");

        moveUp();
    }

    public void parseBooleanExpr() {
        addNode("branch", "booleanExpr");

        if (Objects.equals(current_token, "FALSE")
                || Objects.equals(current_token, "TRUE")) {
            System.out.println("Parser: parseBoolVal()");
            parseBoolVal();
        } else {
            match(current_token, "LEFT_PAREN");
            System.out.println("Parser: parseExpr()");
            parseExpr();
            System.out.println("Parser: parseBoolOp()");
            parseBoolOp();
            System.out.println("Parser: parseExpr()");
            parseExpr();
            match(current_token, "RIGHT_PAREN");
        }

        moveUp();
    }

    public void parseId() {
        addNode("branch", "id");

        match(current_token, "ID");

        moveUp();
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

    public void parseType() {
        addNode("branch", "type");

        list_expected_strings.clear();
        list_expected_strings.addAll(Arrays.asList("INT", "STRING", "BOOLEAN"));

        if (Objects.equals(current_token, "INT")) {
            match(current_token, "INT");
        } else if (Objects.equals(current_token, "STRING")) {
            match(current_token, "STRING");
        } else if (Objects.equals(current_token, "BOOLEAN")) {
            match(current_token, "BOOLEAN");
        } else {
            System.out.println("Parser: error()");
            error(list_expected_strings);
        }

        moveUp();
    }

    public void parseBoolOp() {
        addNode("branch", "boolOp");

        if (Objects.equals(current_token, "EQUAL_TO_OP")) {
            match(current_token, "EQUAL_TO_OP");
        } else {
            match(current_token, "NOT_EQUAL_TO_OP");
        }

        moveUp();
    }

    public void parseBoolVal() {
        addNode("branch", "boolVal");

        list_expected_strings.clear();
        list_expected_strings.addAll(Arrays.asList("False", "TRUE"));

        if (Objects.equals(current_token, "FALSE")) {
            match(current_token, "FALSE");
        } else if (Objects.equals(current_token, "TRUE")) {
            match(current_token, "TRUE");
        } else {
            System.out.println("Parser: error()");
            error(list_expected_strings);
        }

        moveUp();
    }

    public void parseIntOp() {
        addNode("branch", "intOp");

        match(current_token, "PLUS");

        moveUp();
    }

    public void error(ArrayList<String> list_expected_tokens) {
        System.out.println("expected one of these " + list_expected_tokens.toString() + " but found " + current_token);
    }

    public static void getTokens(int program_number, ArrayList<Tokens> toks) {
        tokens = toks;
        PROGRAM_NUMBER = program_number;
    }
    Tree tree = new Tree();

}


