import java.util.*;

public class SemanticAnalysis extends Tree{
    private static int PROGRAM_NUMBER = 0;
    private static ArrayList<Tokens> tokens;
    private static int token_pointer;
    private static String current_token;
    private static Tokens token = null;
    private static Tree myCST = new Tree();
    private static int error_count = 0;
    private static int scope_num = -1;
    private static final ArrayList<String> list_expected_strings = new ArrayList<>();
    private static final ArrayList<String> symbolTableContent = new ArrayList<>();

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
        scope_num = 0;
        /**
         * if no errors, semantic analysis is successful
         */
        if (error_count == 0) {
            System.out.println("DEBUG Semantic Analysis - Semantic Analysis of program " + PROGRAM_NUMBER + " completed with no errors");

            /**
             * print the AST
             */
            System.out.println("\n AST for program " + PROGRAM_NUMBER + " ...\n");

            Tree tree = new Tree();
            tree.print(myCST);

            /**
             * Program Symbol Table
             */
            System.out.println("Program " + PROGRAM_NUMBER + " Symbol Table");
            System.out.println(" _____________________________");
            System.out.println("| Name, Type,    Scope, Line  |");
            System.out.println(" _____________________________");

            // Calculate the number of rows needed to store all elements
            int numRows = (int) Math.ceil((double) symbolTableContent.size() / 4);

            // Initialize the 2D array
            String[][] symbolTableArray = new String[numRows][4];

            // Fill the array with the contents of the ArrayList
            int index = 0;
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < 4; j++) {
                    if (index < symbolTableContent.size()) {
                        symbolTableArray[i][j] = symbolTableContent.get(index++);
                    } else {
                        // If there are no more elements in the ArrayList,
                        // fill the remaining cells with an empty string
                        symbolTableArray[i][j] = "";
                    }
                }
            }

            for (String[] strings : symbolTableArray) {
                System.out.print("| ");
                for (String string : strings) {
                    if (string.contains("string")) {
                        System.out.print(string + "  ");
                    } else {
                        System.out.print(string + "  ");
                    }

                }
                System.out.print("|");
                System.out.println();
            }
            System.out.println(" _____________________________ ");


        }


    }

    /**
     * match method to check currentToken to the expectedToken
     */
    public void match(String cur_tok, String expected_token) {

        if (Objects.equals(current_token, expected_token)) {

            if (Objects.equals(current_token, "LEFT_BRACE")) {
                scope_num++;
            }

            if (Objects.equals(current_token, "INT")) {
                symbolTableContent.addAll(Arrays.asList(Lexer.tokens.get(token_pointer+1).getSymbol(), "   " + token.getSymbol(),
                        "    " + String.valueOf(scope_num), "    " + String.valueOf(token.getLineNum())+ "   "));
            } else if (Objects.equals(current_token, "BOOLEAN")) {
                symbolTableContent.addAll(Arrays.asList(Lexer.tokens.get(token_pointer+1).getSymbol(), "   " +token.getSymbol(),
                        String.valueOf(scope_num), "    " + String.valueOf(token.getLineNum())+ "   "));

            } else if (Objects.equals(current_token, "STRING")) {
                symbolTableContent.addAll(Arrays.asList(Lexer.tokens.get(token_pointer+1).getSymbol(), "   " +token.getSymbol(),
                        " " + String.valueOf(scope_num) , "    " + String.valueOf(token.getLineNum()) + "   "));

            }
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
            parseStatement();
            parseStatementList();
        }

    }

    public void parseStatement() {

        list_expected_strings.clear();
        list_expected_strings.addAll(Arrays.asList("PRINT", "ID", "WHILE", "IF", "LEFT_BRACE"));


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
        } else {
            error(list_expected_strings);
        }

    }

    public void parsePrintStatement() {
        myCST.addNode("Print Statement", "branch");

        match(current_token, "PRINT");
        match(current_token, "LEFT_PAREN");
         parseExpr();
        match(current_token, "RIGHT_PAREN");

        myCST.moveUp();
    }

    public void parseAssignmentStatement() {
        myCST.addNode("Assignment Statement", "branch");

        parseId();
        match(current_token, "ASSIGN");
        parseExpr();

        myCST.moveUp();
    }

    public void parseVarDecl() {
        myCST.addNode("Variable Declaration", "branch");

        parseType();
        parseId();

        myCST.moveUp();
    }

    public void parseWhileStatement() {
        myCST.addNode("While Statement", "branch");

        match(current_token, "WHILE");
        parseBooleanExpr();
        parseBlock();

        myCST.moveUp();
    }

    public void parseIfStatement() {
        myCST.addNode("If Statement", "branch");

        match(current_token, "IF");
        parseBooleanExpr();
        parseBlock();

        myCST.moveUp();
    }

    public void parseExpr() {

        list_expected_strings.clear();
        list_expected_strings.addAll(Arrays.asList("DIGIT", "STRING", "BOOLEAN", "ID"));

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
        } else {
            error(list_expected_strings);
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
        Tokens tok = Lexer.tokens.get(token_pointer-1);
        String symbol = "";
        String name = "";
        symbol = tok.getSymbol();
        name = tok.getLexemeName();
        if (Objects.equals(name, "ID")) {
            if (!Objects.equals(Lexer.tokens.get(token_pointer-2).getLexemeName(), "INT")
                 || !Objects.equals(Lexer.tokens.get(token_pointer-2).getLexemeName(), "STRING")
                 || !Objects.equals(Lexer.tokens.get(token_pointer-2).getLexemeName(), "BOOLEAN")) {
                if (symbolTableContent.contains(symbol)) {
                    if (Objects.equals(String.valueOf(scope_num), (symbolTableContent.get(symbolTableContent.indexOf(symbol) + 2)).trim())) {
                        System.out.println("variable [ " + symbol + " ] is declared");
                    } else {
                        System.out.println("Variable  [ " + symbol + " ] is NOT declared in scope " + scope_num);
                    }
                }
            }
        }
    }

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
            error(list_expected_strings);
        }

    }

    public void parseIntOp() {
        // TODO does IntOp need to be on the AST
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


