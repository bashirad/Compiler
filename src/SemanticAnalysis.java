import java.util.*;

public class SemanticAnalysis extends Tree{
    private static int PROGRAM_NUMBER = 0;
    private static ArrayList<Tokens> tokens;
    private static int token_pointer;
    private static String current_token;
    private static Tokens token = null;
    private static Tree myCST = new Tree();
    private static int error_count = 0;
    private static final ArrayList<String> list_expected_strings = new ArrayList<>();

    public static void main(String[] args) {

        /**
         * Call init_Parser and execute the Parser in the Run environment
         */

        Lexer.init_Lexer("C:\\\\Users\\\\Bashir\\\\Documents\\\\Bashirs_Code_all\\\\Java\\\\cmpt432\\\\src\\\\code.txt");

        // TODO figure out a way to pass tokens to parser at the end of lexing each program
        //init_Parser("C:\\Users\\Bashir\\Documents\\Bashirs_Code_all\\Java\\cmpt432\\src\\code.txt");
        init_Semantic();
    }

    public static void init_Semantic() {

        /**
         * Call the Lexer here
         */
        //Lexer.init_Lexer("C:\\\\Users\\\\Bashir\\\\Documents\\\\Bashirs_Code_all\\\\Java\\\\cmpt432\\\\src\\\\code.txt");

        System.out.println("\nINFO Parser - Parsing program " + PROGRAM_NUMBER + " ... ");

        token_pointer = 0;
        token = tokens.get(token_pointer);
        current_token = token.getLexemeName();

        System.out.println("Parser: init_Parser()");

        /**
         * start analysing the semantics of the source code
         */
        System.out.println("Semantics: parseProgram()");
        SemanticAnalysis mySemanticAnalysis = new SemanticAnalysis();
        mySemanticAnalysis.parseProgram();

        /**
         * if no errors, semantic analysis is successful
         */
        if (error_count == 0) {
            System.out.println("DEBUG Semantic Analysis - Semantic Analysis of program " + PROGRAM_NUMBER + " completed with no errors");

            /**
             * print the CST
             */
            System.out.println("\n AST for program " + PROGRAM_NUMBER + " ...\n");

            Tree tree = new Tree();
            tree.print(myCST);

            /**
             * pass the CST to the Semantics Analysis
             */

        }


    }

    /**
     * match method to check currentToken to the expectedToken
     */
    public void match(String cur_tok, String expected_token) {

        if (Objects.equals(current_token, expected_token)) {

            //System.out.printf("DEBUG Parser - CORRECT: expected    %-14s and found     %s \n", expected_token, current_token);

            if (!Objects.equals(current_token, "LEFT_BRACE")
            || !Objects.equals(current_token, "RIGHT_BRACE")
            || !Objects.equals(current_token, "LEFT_PAREN")
            || !Objects.equals(current_token, "RIGHT_PAREN")
            || !Objects.equals(current_token, "ASSIGN")) {
                // do nothing
                myCST.addNode(token.getSymbol(), "leaf"); // leaf, expected_token
            }
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
        myCST.addNode("program", "root" );

        System.out.println("Parser: parseBlock()");
        parseBlock();

        //myCST.moveUp();
        /**
         * EOP is not part of the program unless it is separating two programs
         */
        match(current_token, "EOP"); // end of program
    }

    /**
     * Procedure to parse Block
     */
    public void parseBlock() {
        myCST.addNode("Block", "branch");

        this.match(current_token, "LEFT_BRACE");
        parseStatementList();
        match(current_token, "RIGHT_BRACE");

        myCST.moveUp();
    }

    /**
     * Procedure to parse StatementList
     */
    public void parseStatementList() {

        Set<String> validTokens =
                new HashSet<>(Arrays.asList("PRINT", "ID", "INT", "STRING", "BOOLEAN", "WHILE", "IF", "LEFT_BRACE"));

        if (validTokens.contains(current_token)) {
            System.out.println("Parser: parseStatement()");
            parseStatement();
            System.out.println("Parser: parseStatementList()");
            parseStatementList();
        }

    }

    public void parseStatement() {

        list_expected_strings.clear();
        list_expected_strings.addAll(Arrays.asList("PRINT", "ID", "WHILE", "IF", "LEFT_BRACE"));


        if (Objects.equals(current_token, "PRINT")) {
            System.out.println("Parser: parsePrintStatement()");
            parsePrintStatement();
        } else if (Objects.equals(current_token, "ID")) {
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

    }

    public void parsePrintStatement() {
        myCST.addNode("Print Statement", "branch");

        match(current_token, "PRINT");
        match(current_token, "LEFT_PAREN");
        System.out.println("Parser: parseExpr()");
        parseExpr();
        match(current_token, "RIGHT_PAREN");

        myCST.moveUp();
    }

    public void parseAssignmentStatement() {
        myCST.addNode("Assignment Statement", "branch");

        System.out.println("Parser: parseId()");
        parseId();
        match(current_token, "ASSIGN");
        System.out.println("Parser: parseExpr()");
        parseExpr();

        myCST.moveUp();
    }

    public void parseVarDecl() {
        myCST.addNode("Variable Declaration", "branch");


        System.out.println("Parser: parseType()");
        parseType();
        System.out.println("Parser: parseId()");
        parseId();

        myCST.moveUp();
    }

    public void parseWhileStatement() {
        myCST.addNode("While Statement", "branch");


        match(current_token, "WHILE");
        System.out.println("Parser: parseBooleanExpr()");
        parseBooleanExpr();
        System.out.println("Parser: parseBlock()");
        parseBlock();

        myCST.moveUp();
    }

    public void parseIfStatement() {
        myCST.addNode("If Statement", "branch");


        match(current_token, "IF");
        System.out.println("Parser: parseBooleanExpr()");
        parseBooleanExpr();
        System.out.println("Parser: parseBlock()");
        parseBlock();

        myCST.moveUp();
    }

    public void parseExpr() {

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

    }

    public void parseIntExpr() {

        match(current_token, "DIGIT");
        if (Objects.equals(current_token, "PLUS")) {
            System.out.println("Parser: parseIntOp()");
            parseIntOp();
            System.out.println("Parser: parseExpr()");
            parseExpr();
        }

    }

    public void parseStringExpr() {

        System.out.println("Parser: parseStringExpr()");
        match(current_token, "STRING");

    }

    public void parseBooleanExpr() {

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

    }

    public void parseId() {

        match(current_token, "ID");

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

    }

    public void parseBoolOp() {

        if (Objects.equals(current_token, "EQUAL_TO_OP")) {
            match(current_token, "EQUAL_TO_OP");
        } else {
            match(current_token, "NOT_EQUAL_TO_OP");
        }

    }

    public void parseBoolVal() {

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

    }

    public void parseIntOp() {
        //myCST.addNode("IntOp", "branch");

        match(current_token, "PLUS");

        //myCST.moveUp();
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


