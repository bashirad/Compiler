public class Parser {
    static String TOKEN = "";
    public static void main(String[] args) {
        init("C:\\Users\\Bashir\\Documents\\Bashirs_Code_all\\Java\\cmpt432\\src\\code.txt");
    }
    public static void init( String path) {

        System.out.println("\nINFO Parser - Parsing program " + 1 + " ... ");
        /** start parsing the source code */
        parseProgram();
    }

    // Procedure to parse Program
    public static void parseProgram() {
        parseBlock();
        match("EOP"); // end of program
    }

    // Procedure to parse Block
    public static void parseBlock() {
        match("LEFT_BRACE");
        parseStatementList();
        match("RIGHT_BRACE");
    }

    // Procedure to parse StatementList
    public static void parseStatementList() {
        while (TOKEN == "STATEMENT") {
            parseStatement();
        }
    }
    public static void parseStatement() {
        if (TOKEN == "PRINT_STATEMENT") {
            parsePrintStatement();
        } else if (TOKEN == "ASSIGN_STATEMENT") {
            parseAssignmentStatement();
        } else if (TOKEN == "VAR_DECL") {
            parseVarDecl();
        } else if (TOKEN == "WHILE_STATEMENT") {
            parseWhileStatement();
        } else if (TOKEN == "IF_STATEMENT") {
            parseIfStatement();
        } else if (TOKEN == "BLOCK") {
            parseBlock();
        } else {
            error();
        }
    }

    public static void parsePrintStatement() {
        match("PRINT");
        match("LEFT_PARENTHESIS");
        parseExpr();
        match("RIGHT_PARENTHESIS");
    }

    public static void parseAssignmentStatement() {
        parseId();
        match("ASSIGN");
        parseExpr();
    }

    public static void parseVarDecl() {
        parseType();
        parseId();
    }

    public static void parseWhileStatement() {
        match("WHILE");
        parseBooleanExpr();
        parseBlock();
    }

    public static void parseIfStatement() {
        match("IF");
        parseBooleanExpr();
        parseBlock();
    }

    public static void parseExpr() {
        if (TOKEN == "INT_EXPR") {
            parseIntExpr();
        } else if (TOKEN == "STRING_EXPR") {
            parseStringExpr();
        } else if (TOKEN == "BOOL_EXPR") {
            parseBooleanExpr();
        } else if (TOKEN == "ID") {
            parseId();
        } else {
            error();
        }
    }

    public static void parseIntExpr() {
        match("DIGIT");
        if (TOKEN == "PLUS") {
            parseIntOp();
            parseExpr();
        }
    }

    public static void parseStringExpr() {
        match("QUOTE");
        parseCharList();
        match("QUOTE");
    }

    public static void parseBooleanExpr() {
        if (TOKEN == "BOOL_VAL") {
            parseBoolVal();
        } else {
            match("LEFT_PARENTHESIS");
            parseExpr();
            parseBoolOp();
            parseExpr();
            match("RIGHT_PARENTHESIS");
        }
    }

    public static void parseId() {
        match("CHAR");
    }

    public static void parseCharList() {
        if (TOKEN == "CHARACTER") {
            match("CHAR");
            parseCharList();
        } else if (TOKEN == "WHITESPACE") {
            match("SPACE");
            parseCharList();
        }
    }

    public static void parseType() {
        if (TOKEN == "INT_I_TYPE") {
            match("INT");
        } else if (TOKEN == "STRING_I_TYPE") {
            match("STRING");
        } else if (TOKEN == "BOOL_I_TYPE") {
            match("BOOLEAN");
        } else {
            error();
        }
    }

    public static void parseBoolOp() {
        if (TOKEN == "EQUAL_TO_OP") {
            match("==");
        } else if (TOKEN == "NOT_EQUAL_TO") {
            match("!=");
        }
    }

    public static void parseBoolVal() {
        if (TOKEN == "FALSE_BOOL_VAL") {
            match("FALSE");
        } else if (TOKEN == "TRUE_BOOL_VAL"){
            match("TRUE");
            error();
        }
    }

    public static void parseIntOp() {
        match("PLUS");
    }

    private static char lookahead;

    public static void match(char c) {
        if (lookahead == c) {
            lookahead = getNextToken();
        } else {
            error();
        }
    }

    public static void match(String s) {
        for (char c : s.toCharArray()) {
            match(c);
        }
    }

    public static void error() {
        throw new RuntimeException("Syntax error");
    }

    public static char getNextToken() {
        // example string
        String input = "this is an example for the string";
        int pos = 0;
        char c = input.charAt(pos);
        pos++;
        return c;
    }

}
