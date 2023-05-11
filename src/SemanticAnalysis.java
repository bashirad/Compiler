import java.util.*;

public class SemanticAnalysis extends Tree{
    private static int PROGRAM_NUMBER = 0;
    private static ArrayList<Tokens> tokens;
    private static int token_pointer;
    private static String current_token;
    private static Tokens token = null;
    private static final Tree myAST = new Tree();
    private static int error_count = 0;
    private static int current_scope = 0;
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

        System.out.println("\nINFO Semantics - Analysing the semantics of program " + PROGRAM_NUMBER + " ... ");

        token_pointer = 0;
        token = tokens.get(token_pointer);
        current_token = token.getLexemeName();

        /**
         * start analysing the semantics of the source code
         */
        SemanticAnalysis mySemanticAnalysis = new SemanticAnalysis();
        mySemanticAnalysis.parseProgram();
        /**
         * if no errors, semantic analysis is successful
         */
        if (error_count == 0) {
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");
            System.out.println("DEBUG Semantic Analysis - Semantic Analysis of program " + PROGRAM_NUMBER + " completed with no errors");

            /**
             * print the AST
             */
            System.out.println("\n AST for program " + PROGRAM_NUMBER + " ...\n");

            Tree tree = new Tree();
            //tree.print(myAST);

            buildSymbolTable(myAST);

            myAST.clear();
            current_scope = 0;
        }


    }
    public static void buildSymbolTable(Tree myAST) {

        Node root = myAST.root;

        processAST.traverseAST(root);
    }
    /**
     * match method to check currentToken to the expectedToken
     */
    public void match(String cur_tok, String expected_token) {

        if (Objects.equals(current_token, expected_token)) {

            if (Objects.equals(current_token, "IF") || Objects.equals(current_token, "WHILE")) {

                // Re-ordering tokens that are out of order
                myAST.addNode(Lexer.tokens.get(token_pointer + 3).getLexemeName(), "branch", token);
            } else if (Objects.equals(current_token, "RIGHT_PAREN")) {
                if (token_pointer >= 5 && Lexer.tokens.get(token_pointer - 5) != null) {
                    Tokens token = Lexer.tokens.get(token_pointer - 5);
                    String lexemeName = token.getLexemeName();
                    if ("IF".equals(lexemeName) || "WHILE".equals(lexemeName)) {
                        // Checked! this is allowing block to be the second child of IF or WHILE Statement
                        myAST.moveUp();
                    }
                }
            } else {

                // Start adding leaf nodes after all rearrangements
                Set<String> exceptionTokens =
                        new HashSet<>(Arrays.asList("LEFT_BRACE", "RIGHT_BRACE", "LEFT_PAREN", "RIGHT_PAREN",
                                "ASSIGN", "PRINT", "EQUAL_TO_OP", "NOT_EQUAL_TO_OP"));

                if (!exceptionTokens.contains(current_token)) {
                    myAST.addNode(token.getSymbol() , "leaf", token);
                }
            }
            token_pointer++;

            if (token_pointer < Lexer.tokens.size()) {
                token = Lexer.tokens.get(token_pointer);
                current_token = Lexer.tokens.get(token_pointer).getLexemeName();
            }
        }
    }
    /**
     * Procedure to parse Program
     */
    public void parseProgram() {
        myAST.addNode("Program", "root", token );

        parseBlock();

        //myAST.moveUp();
        /**
         * EOP is not part of the program unless it is separating two programs
         */
        match(current_token, "EOP"); // end of program
    }
    /**
     * Procedure to parse Block
     */
    public void parseBlock() {
        myAST.addNode("Block", "branch", token);

        this.match(current_token, "LEFT_BRACE");
        parseStatementList();
        match(current_token, "RIGHT_BRACE");

        if (token_pointer < Lexer.tokens.size() - 1) {
            myAST.moveUp();
        }
    }
    /**
     * Procedure to parse StatementList
     */
    public void parseStatementList() {

        Set<String> validTokens =
                new HashSet<>(Arrays.asList("PRINT", "ID", "INT", "STRING", "BOOLEAN", "WHILE", "IF", "LEFT_BRACE"));

        if (validTokens.contains(current_token)) {
            parseStatement();
            parseStatementList();
        }

    }
    public void parseStatement() {

        if (Objects.equals(current_token, "PRINT")) {
            parsePrintStatement();
        } else if (Objects.equals(current_token, "ID")) {
            parseAssignmentStatement();
        } else if (Objects.equals(current_token, "INT")
                || Objects.equals(current_token, "STRING")
                || Objects.equals(current_token, "BOOLEAN")) {
            parseVarDecl();
        } else if (Objects.equals(current_token, "WHILE")) {
            parseWhileStatement();
        } else if (Objects.equals(current_token, "IF")) {
            parseIfStatement();
        } else if (Objects.equals(current_token, "LEFT_BRACE")) {
            parseBlock();
        }
    }
    public void parsePrintStatement() {
        myAST.addNode("Print Statement", "branch", token);

        match(current_token, "PRINT");
        match(current_token, "LEFT_PAREN");
         parseExpr();
        match(current_token, "RIGHT_PAREN");

        myAST.moveUp();
    }
    public void parseAssignmentStatement() {
        myAST.addNode("Assignment Statement", "branch", token);

        parseId();
        match(current_token, "ASSIGN");
        parseExpr();

        myAST.moveUp();
    }
    public void parseVarDecl() {
        myAST.addNode("Variable Declaration", "branch", token);

        parseType();
        parseId();

        myAST.moveUp();
    }
    public void parseWhileStatement() {
        myAST.addNode("While Statement", "branch", token);

        match(current_token, "WHILE");
        parseBooleanExpr();
        parseBlock();

        myAST.moveUp();
    }
    public void parseIfStatement() {
        myAST.addNode("If Statement", "branch", token);

        match(current_token, "IF");
        parseBooleanExpr();
        parseBlock();

        myAST.moveUp();
    }
    public void parseExpr() {

        if (Objects.equals(current_token, "DIGIT")) {
            parseIntExpr();
        } else if (Objects.equals(current_token, "CHAR_LIST")) {
            parseStringExpr();
        } else if (Objects.equals(current_token, "LEFT_PAREN")
                || Objects.equals(current_token, "TRUE")
                || Objects.equals(current_token, "FALSE")) {
            parseBooleanExpr();
        } else if (Objects.equals(current_token, "ID")) {
            parseId();
        }

    }
    public void parseIntExpr() {

        match(current_token, "DIGIT");
        if (Objects.equals(current_token, "PLUS")) {
            parseIntOp();
            parseExpr();
        }

    }
    public void parseStringExpr() {

        match(current_token, "CHAR_LIST");

    }
    public void parseBooleanExpr() {

        if (Objects.equals(current_token, "FALSE")
                || Objects.equals(current_token, "TRUE")) {
            parseBoolVal();
        } else {
            match(current_token, "LEFT_PAREN");
            parseExpr();
            parseBoolOp();
            parseExpr();
            match(current_token, "RIGHT_PAREN");
        }

    }
    public void parseId() {
        match(current_token, "ID");
    }
    public void parseType() {

        if (Objects.equals(current_token, "INT")) {
            match(current_token, "INT");
        } else if (Objects.equals(current_token, "STRING")) {
            match(current_token, "STRING");
        } else if (Objects.equals(current_token, "BOOLEAN")) {
            match(current_token, "BOOLEAN");
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

        if (Objects.equals(current_token, "FALSE")) {
            match(current_token, "FALSE");
        } else if (Objects.equals(current_token, "TRUE")) {
            match(current_token, "TRUE");
        }

    }
    public void parseIntOp() {
        // TODO does IntOp need to be on the AST
        //myAST.addNode("IntOp", "branch", token);

        match(current_token, "PLUS");

        //myAST.moveUp();
    }
    public void error(ArrayList<String> list_expected_tokens) {
        System.out.println("expected one of these " + list_expected_tokens.toString() + " but found " + current_token);
    }
    public static void getTokens(int program_number, ArrayList<Tokens> toks) {
        tokens = toks;
        PROGRAM_NUMBER = program_number;
    }
}


