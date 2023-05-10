import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

public class Workstation3 {

        private static final SymbolTable globalSymbolTable = new SymbolTable(0);
        private static final Map<Integer, SymbolTable> symbolTables = new HashMap<>();

    /*public static void main(String[] args) {
        // traverseAST();

        // System.out.println("Symbol Tables:");

        /**
         * Program Symbol Table
         */
        //

        //}
        public static void traverseAST(Tree.Node root) {
            symbolTables.put(0, globalSymbolTable);
            // create an empty scope stack and start processing the root node
            Stack<Integer> scopeStack = new Stack<>();
            scopeStack.push(0); // start with the global scope
            StringBuilder traversalResult = new StringBuilder();
            // We can ignore the traversalResult for now

            processNode(root, 0, traversalResult, scopeStack, symbolTables);
            System.out.println("This is from processAST Class ****************************************************");
            System.out.println(traversalResult);
        }
        private static void processNode(Tree.Node node, int depth, StringBuilder traversalResult, Stack<Integer> scopeStack, Map<Integer, SymbolTable> symbolTables) {
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
                    int currentScope = scopeStack.peek() + 1;
                    scopeStack.push(currentScope);
                    SymbolTable newSymbolTable = new SymbolTable(currentScope);
                    symbolTables.put(currentScope, newSymbolTable);
                } else if (Objects.equals(name, "Variable Declaration")) {
                    left = node.getChildren().get(0);
                    right = node.getChildren().get(1);

                    String typeV = left.getName();
                    String nameV = right.getName();

                    System.out.println("Variable Declaration => left is typeV " + typeV);
                    System.out.println("Variable Declaration => right is nameV " + nameV);

                    // add to symbol table if not there
                    int currentScope = scopeStack.peek();
                    SymbolTable currentSymbolTable = symbolTables.get(currentScope);

                    if (currentSymbolTable.getSymbol(nameV) == null) {
                        Symbol mysymbol = new Symbol(nameV, typeV, currentScope);
                        currentSymbolTable.addSymbol(mysymbol);
                        System.out.println("Variable Declaration => " + nameV + " is added to SymbolTable " + currentScope + "\n");
                    } else {
                        System.out.println("Variable " + nameV + " is declared more than once");
                    }
                } else if (Objects.equals(node.getName(), "Assignment Statement")) {
                    left = node.getChildren().get(0);
                    right = node.getChildren().get(1);

                    String idA = left.getName();
                    String exprA = right.getName();

                    System.out.println("\nAssignment Statement => left idA is " + idA);
                    System.out.println("Assignment Statement => right exprA is " + exprA);

                    // add to symbol table if not there
                    int currentScope = scopeStack.peek();
                    SymbolTable currentSymbolTable = symbolTables.get(currentScope);

                    // add to symbol table if not there

                    if (currentSymbolTable.getSymbol(idA) == null) {
                        System.out.println("\nAssignment Statement => Variable " + left.getName() + " is used before it is declared");
                    } else {
                        System.out.println("Type checking happens here");
                        System.out.println("****************** " + currentSymbolTable.getSymbol(idA));

                        // Integer type checking
                        if (Objects.equals(currentSymbolTable.getSymbol(idA), "int")) {
                            try {
                                // integer is being assigned to another identifier which can hold a value of any type
                                if (exprA.length() == 1) {
                                    if (currentSymbolTable.getSymbol(exprA)!= null) {
                                        if (Objects.equals(currentSymbolTable.getSymbol(idA), currentSymbolTable.getSymbol(exprA))) {
                                            System.out.println(exprA + " is an integer.");
                                            System.out.println("Types match! for " + idA + " and " + exprA);
                                        } else {
                                            System.out.println(exprA + " is not an integer.");
                                            System.out.println("Type MISMATCH! for " + idA + " and " + exprA);
                                        }
                                    }
                                } else {
                                    int number = Integer.parseInt(exprA);
                                    System.out.println(exprA + " is an integer.");
                                    System.out.println("Types match! for " + idA + " and " + exprA);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(exprA + " is not an integer.");
                                System.out.println("Type MISMATCH! for " + left.getName() + " and " + right.getName());
                            }
                        }
                        // Boolean type checking
                        else if (Objects.equals(currentSymbolTable.getSymbol(idA), "boolean")) {
                            // Boolean is being assigned to another identifier which can hold a value of any type
                            if (exprA.length() == 1) {
                                if (currentSymbolTable.getSymbol(idA) != null) {
                                    if (Objects.equals(currentSymbolTable.getSymbol(idA), currentSymbolTable.getSymbol(idA))) {
                                        System.out.println(exprA + " is a boolean.");
                                        System.out.println("Types match! for " + idA + " and " + exprA);
                                    } else {
                                        System.out.println(exprA + " is not a boolean.");
                                        System.out.println("Type MISMATCH! for " + idA + " and " + exprA);
                                    }
                                }
                            } else {
                                // exprA has to be false or true
                                if (Objects.equals(exprA, "true") || Objects.equals(exprA, "false")) {
                                    System.out.println(exprA + " is a boolean.");
                                    System.out.println("Types match! for " + idA + " and " + exprA);
                                } else {
                                    System.out.println(exprA + " is not a boolean.");
                                    System.out.println("Type MISMATCH! for " + left.getName() + " and " + right.getName());
                                }
                            }
                        }
                        // String type checking
                        else {
                            // String is being assigned to another identifier which can hold a value of any type
                            if (exprA.length() == 1) {
                                if (currentSymbolTable.getSymbol(idA) != null) {
                                    if (Objects.equals(currentSymbolTable.getSymbol(idA), currentSymbolTable.getSymbol(idA))) {
                                        System.out.println(exprA + " is a string.");
                                        System.out.println("Types match! for " + idA + " and " + exprA);
                                    } else {
                                        System.out.println(exprA + " is not a string.");
                                        System.out.println("Type MISMATCH! for " + idA + " and " + exprA);
                                    }
                                }
                            } else {
                                if (exprA.startsWith("\"") && exprA.endsWith("\"")) {
                                    System.out.println(exprA + " is a string.");
                                    System.out.println("Types match! for " + idA + " and " + exprA);
                                } else {
                                    System.out.println(exprA + " is not a string.");
                                    System.out.println("Type MISMATCH! for " + left.getName() + " and " + right.getName());
                                }
                            }
                        }
                    }
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
 }

