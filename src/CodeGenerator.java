import java.util.*;

public class CodeGenerator extends SymbolTableTree{
    private static int PROGRAM_NUMBER = 0;

    private static SymbolTable globalSymbolTable = new SymbolTable(0);
    private static SymbolTableTree mySymbolTableTree = new SymbolTableTree();
    private static Tree myAST = new Tree();
    private static Memory myMemory = new Memory();
    private static StaticTable myStaticTable = new StaticTable();
    private static int currentTempNum = 0;
    private static int currentTempAdd = 0;
    private static int stackPointer = 0;
    private static int heapPointer = 0;

    public static void init_code_generator (int prog_num, Tree ast, SymbolTableTree symbolTableTree) {

        PROGRAM_NUMBER = prog_num;
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");
        System.out.println("INFO Code Generator - Generating Code for program " + PROGRAM_NUMBER + " ... ");

        traverseAST(ast.root, symbolTableTree);

        mySymbolTableTree.printSymbolTables();

        // Get all symbol tables
        Map<Integer, SymbolTable> symbolTables = symbolTableTree.symbolTables;

   }
    public static void traverseAST(Tree.Node root, SymbolTableTree symbolTableTree) {

        Stack<Integer> scopeStack = new Stack<>();
        scopeStack.push(0); // start with the global scope

        processNode(root, 0, scopeStack, symbolTableTree);
    }
    private static void processNode(Tree.Node node, int depth, Stack<Integer> scopeStack, SymbolTableTree symbolTableTree) {
        SymbolTable currentSymbolTable = null;

        String name = node.getName();
        Tree.Node left = null;
        Tree.Node right = null;


        // If there are no children (i.e., leaf nodes)...
        if (Objects.equals(node.getKind(), "leaf")) {
            Tokens token = node.getTokens();

        } else {

            if (Objects.equals(name, "Block")) {
                if (Objects.equals(node.getParent().getName(), "Program")) {
                    int currentScope = 0;
                    scopeStack.push(currentScope);

                } else {
                    int currentScope = scopeStack.peek() + 1;
                }
            } else if (Objects.equals(name, "Variable Declaration")) {
                // get the current symbol table in case it needs to be added
                currentSymbolTable = symbolTables.get(scopeStack.peek());

                processVarDeclStmt(node, scopeStack);
            } else if (Objects.equals(name, "Assignment Statement")) {
                //processAssignStmt(node, scopeStack);
            } else if (Objects.equals(name, "Print Statement")) {
                //processPrintStmt(node, scopeStack);
            } else if (Objects.equals(name, "EQUAL_TO_OP")
                    || Objects.equals(name, "NOT_EQUAL_TO_OP")) {
                //processIsEqual(node, scopeStack);
            }


            // ... recursively expand them.
            for (int i = 0; i < node.getChildren().size(); i++) {
                processNode(node.getChildren().get(i), depth + 1, scopeStack, mySymbolTableTree);
            }

            // As the recursion backtracks and scope closes,
            // remove the current scope from the stack.
            if (Objects.equals(name, "Block")) {
                scopeStack.pop();
            }
        }
    }
    private static void processVarDeclStmt(Tree.Node node, Stack<Integer> scopeStack) {
        Tree.Node left = node.getChildren().get(0);
        Tree.Node right = node.getChildren().get(1);

        String idA = left.getName();
        String exprA = right.getName();

        // Initialize Integers to 0
//        if (Objects.equals(symbolTables.get(scopeStack.peek() - 1).getSymbol(idA).type(), "int")) {
            myMemory.addToStack("A9");
            myMemory.addToStack("00");

            currentTempAdd = currentTempAdd + currentTempNum;
            myStaticTable.addEntry("T"+currentTempNum + "XX", idA, currentTempAdd);


            myMemory.addToStack("8D");
            myMemory.addToStack("T"+currentTempNum);
            myMemory.addToStack("XX");
//        }
    }
    private static void processAssignStmt(Tree.Node node, Stack<Integer> scopeStack) {
        Tree.Node left = node.getChildren().get(0);
        Tree.Node right = node.getChildren().get(1);

        String idA = left.getName();
        String exprA = right.getName();

        // add to symbol table if not there
        int currentScope = scopeStack.peek();
        SymbolTable currentSymbolTable = symbolTables.get(currentScope);

        // add to symbol table if not there

        if (currentSymbolTable.getSymbol(idA) == null) {
            System.out.println("\nAssignment Statement => ERROR: Variable " + idA + " is NOT declared in scope");
        } else {
            Symbol symbol = currentSymbolTable.getSymbol(idA);
            int symbolScope = currentSymbolTable.getSymbol(idA).scopeId();
            Symbol newSymbol = symbol.withInitialized(true);
            SymbolTable symbolTable = symbolTables.get(symbolScope);
            if (symbolTable != null) {
                symbolTable.replaceSymbol(newSymbol);
            }

            if (!typeCheck(node, scopeStack)) {
                System.out.println("Assignment Statement => ERROR: Type MISMATCH! for " + idA + " and " + exprA);
            }
        }
    }
    private static void processIsEqual(Tree.Node node, Stack<Integer> scopeStack) {
        Tree.Node left = node.getChildren().get(0);
        Tree.Node right = node.getChildren().get(1);

        String lexemeNameE1 = left.getTokens().lexemeName;
        String lexemeNameE2 = right.getTokens().lexemeName;

        String exprE1 = left.getName();
        String exprE2 = right.getName();

        // add to symbol table if not there
        int currentScope = scopeStack.peek();
        SymbolTable currentSymbolTable = symbolTables.get(currentScope);

        // add to symbol table if not there

        if (Objects.equals(lexemeNameE1, "ID") && currentSymbolTable.getSymbol(exprE1) == null) {
            System.out.println("\nAssignment Statement => ERROR: Variable " + exprE1 + " is NOT declared in scope");
        } else if (Objects.equals(lexemeNameE2, "ID") && currentSymbolTable.getSymbol(exprE2) == null) {
            System.out.println("\nAssignment Statement => ERROR: Variable " + exprE2 + " is NOT declared in scope");
        } else {
            if (Objects.equals(lexemeNameE1, "ID")) {
                Symbol symbol = currentSymbolTable.getSymbol(exprE1);
                Symbol newSymbol = symbol.withUsed(true);
                int symbolScope = currentSymbolTable.getSymbol(exprE1).scopeId();

                SymbolTable symbolTable = symbolTables.get(symbolScope);
                if (symbolTable != null) {
                    symbolTable.replaceSymbol(newSymbol);
                }

            } else if (Objects.equals(lexemeNameE2, "ID")) {
                Symbol symbol = currentSymbolTable.getSymbol(exprE2);
                Symbol newSymbol = symbol.withUsed(true);
                int symbolScope = currentSymbolTable.getSymbol(exprE2).scopeId();

                SymbolTable symbolTable = symbolTables.get(symbolScope);
                if (symbolTable != null) {
                    symbolTable.replaceSymbol(newSymbol);
                }

            }
            if (!typeCheck(node, scopeStack)) {
                System.out.println("Assignment Statement => ERROR: Type MISMATCH! for " + exprE1 + " and " + exprE2);
            }
        }
    }
    private static void processPrintStmt(Tree.Node node, Stack<Integer> scopeStack) {
        Tree.Node child = node.getChildren().get(0);

        String lexemeName = child.getTokens().getLexemeName();
        String exprP = child.getName();

        // add to symbol table if not there
        int currentScope = scopeStack.peek();
        SymbolTable currentSymbolTable = symbolTables.get(currentScope);

        // check if symbol is already declared

        if (currentSymbolTable.getSymbol(exprP) == null && Objects.equals(lexemeName, "ID")) {
            System.out.println("\nAssignment Statement => ERROR: Variable " + exprP + " is NOT declared in scope");
        } else if (Objects.equals(lexemeName, "ID")) {
            Symbol symbol = currentSymbolTable.getSymbol(exprP);
            Symbol newSymbol = symbol.withUsed(true);
            int symbolScope = currentSymbolTable.getSymbol(exprP).scopeId();

            SymbolTable symbolTable = symbolTables.get(symbolScope);
            if (symbolTable != null) {
                symbolTable.replaceSymbol(newSymbol);
            }
        }
    }

    // NO TYPE CHECKING HERE
    private static Boolean typeCheck(Tree.Node node, Stack<Integer> scopeStack) {
        Tree.Node left = node.getChildren().get(0);
        Tree.Node right = node.getChildren().get(1);

        String idA = left.getName();
        String exprA = right.getName();
        String lexemeName = right.getTokens().lexemeName;

        int currentScope = scopeStack.peek();
        SymbolTable currentSymbolTable = symbolTables.get(currentScope);

        // Integer type checking
        if (Objects.equals(currentSymbolTable.getSymbol(idA).type(), "int")) {

            // integer is being assigned to another identifier which can hold a value of any type
            if (Objects.equals(lexemeName, "ID")) {
                if (currentSymbolTable.getSymbol(exprA) != null) {
                    // call error() here and increment it
                    return (Objects.equals(currentSymbolTable.getSymbol(idA).type(), currentSymbolTable.getSymbol(exprA).type()));
                }
            } else {
                try {
                    int number = Integer.parseInt(exprA);
                } catch (NumberFormatException e) {
                    // call error() here and increment it
                    return false;
                }
            }
        }
        // Boolean type checking
        else if (Objects.equals(currentSymbolTable.getSymbol(idA).type(), "boolean")) {
            // Boolean is being assigned to another identifier which can hold a value of any type
            if (Objects.equals(lexemeName, "ID")) {
                // call error() here and increment it
                return (Objects.equals(currentSymbolTable.getSymbol(idA).type(), currentSymbolTable.getSymbol(idA).type()));
            } else {
                // exprA has to be false or true
                // call error() here and increment it
                return (Objects.equals(exprA, "true") || Objects.equals(exprA, "false"));
            }
        }
        // String type checking
        else {
            // String is being assigned to another identifier which can hold a value of any type
            if (Objects.equals(lexemeName, "ID")) {
                // call error() here and increment it
                return (Objects.equals(currentSymbolTable.getSymbol(idA).type(), currentSymbolTable.getSymbol(idA).type()));
            } else {
                // call error() here and increment it
                return exprA.startsWith("\"") || exprA.endsWith("\"");
            }
        }
        return true;
    }


    private void setZFlag(int value) {
        Memory memory = new Memory();
        if (value == 0) {
            memory.setZFlag(1);
        }
    }

    public static void getAST(int program_number, Tree ast) {
        PROGRAM_NUMBER = program_number;
        myAST = ast;
    }
    public static void getSymbolTableTree(SymbolTableTree symbolTableTree) {
        mySymbolTableTree = symbolTableTree;
    }


}
