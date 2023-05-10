import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

public class processAST  {
private static final SymbolTable globalSymbolTable = new SymbolTable(0);
private static final Map<Integer, SymbolTable> symbolTables = new HashMap<>();

// Create the symbol table tree
private static final SymbolTableTree mySymbolTableTree = new SymbolTableTree();
    public static void traverseAST(Tree.Node root) {
        symbolTables.put(0, globalSymbolTable);
        // create an empty scope stack and start processing the root node
        Stack<Integer> scopeStack = new Stack<>();
        scopeStack.push(0); // start with the global scope
        StringBuilder traversalResult = new StringBuilder();
        // We can ignore the traversalResult for now

        processNode(root, 0, traversalResult, scopeStack, symbolTables);
        //System.out.println(traversalResult);
        mySymbolTableTree.printSymbolTables();
    }
    private static void processNode(Tree.Node node, int depth, StringBuilder traversalResult, Stack<Integer> scopeStack, Map<Integer, SymbolTable> symbolTables) {
        SymbolTable currentSymbolTable = null;

        String name = node.getName();
        Tree.Node left = null;
        Tree.Node right = null;

        // Space out based on the current depth so
        // this looks at least a little tree-like.
        traversalResult.append("-".repeat(Math.max(0, depth)));

        // If there are no children (i.e., leaf nodes)...
        if (Objects.equals(node.getKind(), "leaf")) {
            Tokens token = node.getTokens();
            if (token != null) {
                //System.out.println("\n\ntoken is " + token.getLexemeName() + " " + token.getSymbol() + " " + token.getLineNum() + " " + token.getPosNum());
            }
            // ... note the leaf node.
            traversalResult.append("[ ").append(node.getName()).append(" ]");
            traversalResult.append("\n");
        } else {

            if (Objects.equals(name, "Block")) {
                if (Objects.equals(node.getParent().getName(), "Program")) {
                    int currentScope = 0;
                    scopeStack.push(currentScope);
                    SymbolTable newSymbolTable = new SymbolTable(currentScope);
                    symbolTables.put(currentScope, newSymbolTable);
                } else {
                    int currentScope = scopeStack.peek() + 1;
                    scopeStack.push(currentScope);
                    SymbolTable newSymbolTable = new SymbolTable(currentScope);
                    symbolTables.put(currentScope, newSymbolTable);
                }
            } else if (Objects.equals(name, "Variable Declaration")) {
                left = node.getChildren().get(0);
                right = node.getChildren().get(1);

                String typeV = left.getName();
                String nameV = right.getName();

                // create symbol in case it needs to be added
                currentSymbolTable = symbolTables.get(scopeStack.peek());
                Symbol symbol = new Symbol(nameV, typeV, scopeStack.peek());

                if (scopeStack.peek() == 0) {
                    // add to the root symbol table if this first symbol is not there
                    if (globalSymbolTable.getSymbol(symbol.name()) == null) {
                        globalSymbolTable.addSymbol(symbol);
                        mySymbolTableTree.addSymbolTable(scopeStack.peek(), globalSymbolTable);
                    } else {
                        // call error() here and increment it
                        System.out.println("Variable Declaration => ERROR: " + nameV + " is declared again in the same scope [ " + scopeStack.peek() + " ]\n");
                    }
                }

                currentSymbolTable.addSymbol(symbol);
                mySymbolTableTree.addSymbolTable(scopeStack.peek(),currentSymbolTable);

                //System.out.println("Variable Declaration => " + nameV + " is added to SymbolTable " + scopeStack.peek() + "\n");
            } else if (Objects.equals(name, "Assignment Statement")) {
                processAssignStmt(node, scopeStack);
            } else if (Objects.equals(name, "Print Statement")) {
                processPrintStmt(node, scopeStack);
            } else if (Objects.equals(name, "EQUAL_TO_OP")
                    || Objects.equals(name, "NOT_EQUAL_TO_OP")) {
                processIsEqual(node, scopeStack);
            }


            // There are children, so note these interior/branch nodes and ...
            traversalResult.append("<").append(node.getName()).append("> \n");
            // ... recursively expand them.
            for (int i = 0; i < node.getChildren().size(); i++) {
                processNode(node.getChildren().get(i), depth + 1, traversalResult, scopeStack, symbolTables);
            }

            // As the recursion backtracks and scope closes,
            // remove the current scope from the stack.
            if (Objects.equals(name, "Block")) {
                scopeStack.pop();
            }
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

        // add to symbol table if not there

        if (currentSymbolTable.getSymbol(idA) == null) {
            System.out.println("\nAssignment Statement => ERROR: Variable " + idA + " is NOT declared in scope");
        } else {
            if (!typeCheck(node, scopeStack)) {
                System.out.println("Assignment Statement => ERROR: Type MISMATCH! for " + idA + " and " + exprA);
            }
        }
    }
    private static void processIsEqual(Tree.Node node, Stack<Integer> scopeStack) {
        Tree.Node left = node.getChildren().get(0);
        Tree.Node right = node.getChildren().get(1);

        String lexemeNameE1 = left.getTokens().lexemeName;
        String lexemeNameE2 = left.getTokens().lexemeName;

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
            if (!typeCheck(node, scopeStack)) {
                System.out.println("Assignment Statement => ERROR: Type MISMATCH! for " + exprE1 + " and " + exprE2);
            }
        }
    }
    private static void processPrintStmt(Tree.Node node, Stack<Integer> scopeStack) {
        Tree.Node child = node.getChildren().get(0);

        String exprA = child.getName();

        // add to symbol table if not there
        int currentScope = scopeStack.peek();
        SymbolTable currentSymbolTable = symbolTables.get(currentScope);

        // check if symbol is already declared

        if (currentSymbolTable.getSymbol(exprA) == null) {
            System.out.println("\nAssignment Statement => ERROR: Variable " + exprA + " is NOT declared in scope");
        }
    }


    /*private static void processIsEqual(Tree.Node node, Stack<Integer> scopeStack) {
        Tree.Node left = node.getChildren().get(0);
        Tree.Node right = node.getChildren().get(1);

        String idA = left.getName();
        String exprA = right.getName();
        String lexemeName = right.getTokens().lexemeName;

        // add to symbol table if not there
        int currentScope = scopeStack.peek();
        SymbolTable currentSymbolTable = symbolTables.get(currentScope);

        // add to symbol table if not there

        if (currentSymbolTable.getSymbol(idA) == null) {
            System.out.println("\nAssignment Statement => ERROR: Variable " + left.getName() + " is used before it is declared");
        } else {
            // Integer type checking
            if (Objects.equals(currentSymbolTable.getSymbol(idA).type(), "int")) {
                // integer is being assigned to another identifier which can hold a value of any type
                if (Objects.equals(lexemeName, "ID")) {
                    if (currentSymbolTable.getSymbol(exprA) != null) {
                        if (!Objects.equals(currentSymbolTable.getSymbol(idA).type(), currentSymbolTable.getSymbol(exprA).type())) {
                            // call error() here and increment it
                            System.out.println("Assignment Statement => ERROR: Type MISMATCH! for " + idA + " and " + exprA);
                        }
                    }
                } else {
                    try {
                        int number = Integer.parseInt(exprA);
                    } catch (NumberFormatException e) {
                        // call error() here and increment it
                        System.out.println("Assignment Statement => ERROR: Type MISMATCH! for " + idA + " and " + exprA);
                    }
                }
            }
            // Boolean type checking
            else if (Objects.equals(currentSymbolTable.getSymbol(idA).type(), "boolean")) {
                // Boolean is being assigned to another identifier which can hold a value of any type
                if (exprA.length() == 1) {
                    if (Objects.equals(lexemeName, "ID")) {
                        if (!Objects.equals(currentSymbolTable.getSymbol(idA), currentSymbolTable.getSymbol(idA))) {
                            // call error() here and increment it
                            System.out.println("Assignment Statement => ERROR: Type MISMATCH! for " + idA + " and " + exprA);
                        }
                    }
                } else {
                    // exprA has to be false or true
                    if (!Objects.equals(exprA, "true") || !Objects.equals(exprA, "false")) {
                        // call error() here and increment it
                        System.out.println("Assignment Statement => ERROR: Type MISMATCH! for " + idA + " and " + exprA);
                    }
                }
            }
            // String type checking
            else {
                // String is being assigned to another identifier which can hold a value of any type
                if (exprA.length() == 1) {
                    if (Objects.equals(lexemeName, "ID")) {
                        if (!Objects.equals(currentSymbolTable.getSymbol(idA), currentSymbolTable.getSymbol(idA))) {
                            // call error() here and increment it
                            System.out.println("Assignment Statement => ERROR: Type MISMATCH! for " + idA + " and " + exprA);
                        }
                    }
                } else {
                    if (!exprA.startsWith("\"") && !exprA.endsWith("\"")) {
                        // call error() here and increment it
                        System.out.println("Assignment Statement => ERROR: Type MISMATCH! for " + idA + " and " + exprA);
                    }
                }
            }
        }
    }*/

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

}
