import java.util.*;

public class CodeGenerator extends SymbolTableTree{
    private static int PROGRAM_NUMBER = 0;

    private static final SymbolTable globalSymbolTable = new SymbolTable(0);
    private static SymbolTableTree mySymbolTableTree = new SymbolTableTree();
    private static Tree myAST = new Tree();
    private static final Memory myMemory = new Memory();
    private static final StaticTable myStaticTable = new StaticTable();
    private static final Jumps myJumps = new Jumps();
    private static int currentTempNum = 0;
    private static int currentTempAdd = 0;

    private static int startJump = 0;
    private static int endJump = 0;
    private static int jumpSize = 0;
    private static int jumpNum = 0;
    public static void init_code_generator (int prog_num, Tree ast, SymbolTableTree symbolTableTree) {

        PROGRAM_NUMBER = prog_num;
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");
        System.out.println("INFO Code Generator - Generating Code for program " + PROGRAM_NUMBER + " ... ");

        myMemory.initializeMem("00");

        traverseAST(ast.root, symbolTableTree);

        // add break to denote end of program
        myMemory.addToStack("00");

        //System.out.println(Integer.toHexString(myMemory.getCurrentStackLocation()).toUpperCase());

        myStaticTable.updateAddress(myMemory.getCurrentStackLocation());

        updateTempVariables();

        updateJumps();

        myStaticTable.printTable();

        myJumps.printTable();

        myMemory.printMemory();
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
                processVarDeclStmt(node, scopeStack);
            } else if (Objects.equals(name, "Assignment Statement")) {
                processAssignStmt(node, scopeStack);
            } else if (Objects.equals(name, "Print Statement")) {
                processPrintStmt(node, scopeStack);
            } else if (Objects.equals(name, "EQUAL_TO_OP")) {
                processIsEqual(node, scopeStack);
            } else if (Objects.equals(name, "NOT_EQUAL_TO_OP")) {
                processNotEqual(node, scopeStack);
            }


            // ... recursively expand them.
            for (int i = 0; i < node.getChildren().size(); i++) {
                processNode(node.getChildren().get(i), depth + 1, scopeStack, mySymbolTableTree);
            }

            // As the recursion backtracks and scope closes,
            // remove the current scope from the stack.
            if (Objects.equals(name, "Block")) {
                scopeStack.pop();

                if (Objects.equals(node.getParent().getName(), "If Statement")) {
                    endJump = myMemory.getCurrentStackLocation();
                    jumpSize = endJump - startJump;

                    myJumps.setJumpDistance("J"+jumpNum, jumpSize);
                    jumpNum++;
                }
            }
        }
    }
    private static void processVarDeclStmt(Tree.Node node, Stack<Integer> scopeStack) {
        Tree.Node left = node.getChildren().get(0);
        Tree.Node right = node.getChildren().get(1);

        String typeV = left.getName();
        String varV= right.getName();

        // Initialize Integers to 0
       if (Objects.equals(typeV, "int")) {
            myMemory.addToStack("A9");
            myMemory.addToStack("00");

            myStaticTable.addEntry("T"+currentTempNum + "XX", varV, currentTempAdd);

            myMemory.addToStack("8D");
            if (myStaticTable.containsVariable(varV)) {
                myMemory.addToStack(myStaticTable.getVariableTempAddress(varV)[0]);
                myMemory.addToStack(myStaticTable.getVariableTempAddress(varV)[1]);
            } else {
                myMemory.addToStack("T" + currentTempNum);
                myMemory.addToStack("XX");
            }

            currentTempAdd++;
            currentTempNum++;
        }
    }

    private static void processAssignStmt(Tree.Node node, Stack<Integer> scopeStack) {
        Tree.Node left = node.getChildren().get(0);
        Tree.Node right = node.getChildren().get(1);

        String idA = left.getName();
        String exprA = right.getName();

        // add to symbol table if not there
        int currentScope = scopeStack.peek();
        SymbolTable currentSymbolTable = symbolTables.get(currentScope);

        if (Objects.equals(currentSymbolTable.getSymbol(idA).type(), "int")) {
            // Generate code for integer assignment
            generateIntAssignmentCode(node);
        } else if (Objects.equals(currentSymbolTable.getSymbol(idA).type(), "string")) {
            // Generate code for string assignment
            generateStringAssignmentCode(node);
        } else if (Objects.equals(currentSymbolTable.getSymbol(idA).type(), "boolean")) {
            // Generate code for boolean assignment
            generateBooleanAssignmentCode(node);
        }

    }

    private static void generateIntAssignmentCode(Tree.Node node) {
        Tree.Node left = node.getChildren().get(0);
        Tree.Node right = node.getChildren().get(1);

        String idA = left.getName();
        String exprA = right.getName();

        // Check if the expression is a digit
        try {
            int value = Integer.parseInt(exprA);
            String hexValue = Integer.toHexString(value);

            if (hexValue.length() == 1) {
                hexValue = "0" + hexValue;
            }

            myMemory.addToStack("A9");
            myMemory.addToStack(hexValue.toUpperCase());
        } catch (NumberFormatException e) {
            // If it's not a digit, assume it's an ID and load its value into the accumulator
            myMemory.addToStack("AD");
            myMemory.addToStack(myStaticTable.getVariableTempAddress(exprA)[0]);
            myMemory.addToStack(myStaticTable.getVariableTempAddress(exprA)[1]);
        }

        myMemory.addToStack("8D");
        if (myStaticTable.containsVariable(idA)) {
            myMemory.addToStack(myStaticTable.getVariableTempAddress(idA)[0]);
            myMemory.addToStack(myStaticTable.getVariableTempAddress(idA)[1]);
        } else {
            myMemory.addToStack("T" + currentTempNum);
            myMemory.addToStack("XX");
            currentTempAdd++;
            currentTempNum++;
        }
    }

    private static void generateStringAssignmentCode(Tree.Node node) {
        Tree.Node left = node.getChildren().get(0);
        Tree.Node right = node.getChildren().get(1);

        String idA = left.getName();
        String strA = right.getName();

        String beginRowStr = myMemory.addToHeap(strA)[0];
        String beginColStr = myMemory.addToHeap(strA)[1];

        int beginRowInt = Integer.parseInt(beginRowStr);
        int beginColInt = Integer.parseInt(beginColStr);

        // Copy the string into memory
        myMemory.copyStringToMemory(strA, beginRowStr, beginColStr);

        // Load the address of the string into the X register
        myMemory.addToStack("A9");
        myMemory.addToStack(myMemory.getHexStringFromMemoryAddress(beginRowInt,beginColInt));


        myStaticTable.addEntry("T" + currentTempNum + "XX", idA, currentTempAdd);
        // add a static pointer ot the start of the string in the heap
        myMemory.addToStack("8D");
        myMemory.addToStack("T" + currentTempNum);
        myMemory.addToStack("XX");

        currentTempNum++;
        currentTempAdd++;

    }

    private static void generateBooleanAssignmentCode(Tree.Node node) {
        String expr = node.getName();

        if (Objects.equals(expr, "true")) {
            // Load 01 into the accumulator
            myMemory.addToStack("A9");
            myMemory.addToStack("01");
        } else {
            // Load 00 into the accumulator
            myMemory.addToStack("A9");
            myMemory.addToStack("00");
        }

        myMemory.addToStack("8D");
        myMemory.addToStack("T" + currentTempNum);
        myMemory.addToStack("XX");
    }

    private static void processPrintStmt(Tree.Node node, Stack<Integer> scopeStack) {
        Tree.Node child = node.getChildren().get(0);

        String lexemeName = child.getTokens().getLexemeName();
        String exprP = child.getName();

        // add to symbol table if not there
        int currentScope = scopeStack.peek();
        SymbolTable currentSymbolTable = symbolTables.get(currentScope);

        // if integer

        // if we are printing an int variable, find it temp address, update x registrar with 2 and call system
        if (Objects.equals(lexemeName, "ID")) {
            myMemory.addToStack("AC");
            if (Objects.equals(currentSymbolTable.getSymbol(exprP).type(), "int")) {
                myMemory.addToStack(myStaticTable.getVariableTempAddress(exprP)[0]);
                myMemory.addToStack(myStaticTable.getVariableTempAddress(exprP)[1]);

                myMemory.addToStack("A2");
                myMemory.addToStack("01");
                myMemory.addToStack("FF");

                // if we are printing a string variable, find its temp address, update x registrar with 2 and call system
            } else if (Objects.equals(currentSymbolTable.getSymbol(exprP).type(), "string")) {
                myMemory.addToStack(myStaticTable.getVariableTempAddress(exprP)[0]);
                myMemory.addToStack(myStaticTable.getVariableTempAddress(exprP)[1]);

                myMemory.addToStack("A2");
                myMemory.addToStack("02");
                myMemory.addToStack("FF");
            }
        } else if (Objects.equals(lexemeName, "DIGIT")) {
            myMemory.addToStack("A2");
            myMemory.addToStack("01");
            myMemory.addToStack("FF");
        } else if (Objects.equals(lexemeName, "CHAR_LIST")) {
            myMemory.addToStack(myStaticTable.getVariableTempAddress(exprP)[0]);
            myMemory.addToStack(myStaticTable.getVariableTempAddress(exprP)[1]);

            myMemory.addToStack("A2");
            myMemory.addToStack("02");
            myMemory.addToStack("FF");
        }


    }

    private static void processIsEqual(Tree.Node node, Stack<Integer> scopeStack) {
        Tree.Node left = node.getChildren().get(0);
        Tree.Node right = node.getChildren().get(1);

        String exprE1 = left.getName();
        String exprE2 = right.getName();

        // variable == or !=  to an int
        myMemory.addToStack("A2");
        myMemory.addToStack("0"+exprE2);

        myMemory.addToStack("EC");
        myMemory.addToStack(myStaticTable.getVariableTempAddress(exprE1)[0]);
        myMemory.addToStack(myStaticTable.getVariableTempAddress(exprE1)[1]);

        myMemory.addToStack("D0");
        myJumps.addJump("J"+jumpNum, 0);
        myMemory.addToStack("J"+jumpNum);

        startJump = myMemory.getCurrentStackLocation();

    }
    private static void processNotEqual(Tree.Node node, Stack<Integer> scopeStack) {
        Tree.Node left = node.getChildren().get(0);
        Tree.Node right = node.getChildren().get(1);

        String exprE1 = left.getName();
        String exprE2 = right.getName();

        // while (a != 5) { ... }
        // Load Accumulator from memory
        myMemory.addToStack("AD");
        myMemory.addToStack(myStaticTable.getVariableTempAddress(exprE1)[0]);
        myMemory.addToStack(myStaticTable.getVariableTempAddress(exprE1)[1]);

        myMemory.addToStack("8D");
        myMemory.addToStack("94");
        myMemory.addToStack("00");

        myMemory.addToStack("A9");
        myMemory.addToStack(exprE2);

        myMemory.addToStack("8D");
        myMemory.addToStack("93");
        myMemory.addToStack("00");

        myMemory.addToStack("AE");
        myMemory.addToStack("94");
        myMemory.addToStack("00");

        myMemory.addToStack("EC");
        myMemory.addToStack("93");
        myMemory.addToStack("00");

        myMemory.addToStack("A9");
        myMemory.addToStack("00");

        myMemory.addToStack("D0");
        myMemory.addToStack("02");

        myMemory.addToStack("A9");
        myMemory.addToStack("01");

        myMemory.addToStack("A2");
        myMemory.addToStack("00");

        myMemory.addToStack("8D");
        myMemory.addToStack("93");
        myMemory.addToStack("00");

        myMemory.addToStack("EC");
        myMemory.addToStack("93");
        myMemory.addToStack("00");

        myMemory.addToStack("D0");
        myJumps.addJump("J"+jumpNum, 0);
        myMemory.addToStack("J"+jumpNum);

        startJump = myMemory.getCurrentStackLocation();
    }

    public static void updateTempVariables() {
        int currentAddress = myMemory.getCurrentStackLocation();
        int numTempVars = myStaticTable.getNumTempVars();

        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 8; j++) {
                String memoryValue = myMemory.memoryArray[i][j];
                if (memoryValue.startsWith("T")) {
                    String temp = memoryValue.concat("XX");

                    String address = Integer.toHexString(myStaticTable.getAddress(temp)).toUpperCase();

                    myMemory.memoryArray[i][j] = address;
                } else if (Objects.equals(memoryValue, "XX")) {
                    myMemory.memoryArray[i][j] = "00";
                }
            }
        }
    }
    public static void updateJumps() {
        int currentAddress = myMemory.getCurrentStackLocation();
        int numTempVars = myStaticTable.getNumTempVars();

        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 8; j++) {
                String memoryValue = myMemory.memoryArray[i][j];
                if (memoryValue.startsWith("J")) {

                    String address = Integer.toHexString(myJumps.getJumpDistance(memoryValue)).toUpperCase();

                    if (address.length() == 1) {
                        address = "0" + address;
                    }

                    myMemory.memoryArray[i][j] = address;
                }
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

    public static void getAST(int program_number, Tree ast) {
        PROGRAM_NUMBER = program_number;
        myAST = ast;
    }
    public static void getSymbolTableTree(SymbolTableTree symbolTableTree) {
        mySymbolTableTree = symbolTableTree;
    }


}
