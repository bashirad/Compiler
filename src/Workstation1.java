import java.util.ArrayList;
import java.util.List;

public class Workstation1 {
   /*
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

    */

        /*public  void depthFirstInOrder(Node node, int scopeLevel, HashMap<Integer, HashMap<String, String>> symbolTables) {

        // If the node is a variable declaration, add it to the symbol table for the current scope level.
        if (node.getKind().equals("branch") && node.getName().equals("Variable Declaration")) {
        HashMap<String, String> symbolTable = symbolTables.get(scopeLevel);
        String name = node.getChildren().get(0).getName();
        String type = node.getChildren().get(1).getName();
        symbolTable.put(name, type);
        } else if (node.getKind().equals("branch") && node.getName().equals("Assignment Statement")) {
            // If the node is an assignment statement, check if the variable can be assigned the given value.
            Node left = node.getChildren().get(0);
            Node right = node.getChildren().get(1);

            String name = left.getParent().getName();
            HashMap<String, String> symbolTable = symbolTables.get(scopeLevel);

            if (symbolTable.containsKey(name)) {
               String type = symbolTable.get(name);
               if (type.equals("int") && right.getName().equals("NUMBER")) {
               // OK
               } else if (type.equals("string") && right.getName().equals("STRING")) {
               // OK
               } else if (type.equals("boolean") && (right.getName().equals("TRUE") || right.getName().equals("FALSE"))) {
               // OK
               } else {
               System.out.println("ERROR: Invalid assignment for variable " + name + " in scope " + scopeLevel);
               }
            } else {
               System.out.println("ERROR: Variable " + name + " not declared in scope " + scopeLevel);
            }
        }
        if (node.getChildren() != null) {
            // Recursively call the method on the node's children.
            for (Node child : node.getChildren()) {
               depthFirstInOrder(child, scopeLevel, symbolTables);
               if (child.getName().equals("BLOCK")) {
                       scopeLevel++;
                       symbolTables.put(scopeLevel, new HashMap<String, String>());
               } else if (child.getName().equals("}")) {
                   symbolTables.remove(scopeLevel);
                   scopeLevel--;
               }
            }
        }
    }*/
/*
// If we are an interior/branch node, then...
            if (node.getName().equals("Block")) {
                // ... create a new symbol table for this block and make it the current scope.
                SymbolTable blockScope = new SymbolTable(node.getSymbolTable());
                node.setSymbolTable(blockScope);
            } else {
                // ... check if the variable is declared in the current or any parent scope.
                String varType = null;
                Node lookupNode = node;
                while (lookupNode != null) {
                    if (lookupNode.getSymbolTable() != null) {
                        varType = lookupNode.getSymbolTable().get(node.getName());
                        if (varType != null) {
                            break;
                        }
                        lookupNode = lookupNode.getParent();
                    }
                }
                if (varType == null) {
                    // Variable not found in any parent scope, so it's a new declaration.
                    // Add it to the current scope.
                    String type = null;
                    if (node.getName().equals("Variable Declaration")) {
                        // If we're a variable declaration, get the type from the node's parent.
                        Node parentNode = node.getParent();
                        type = parentNode.getKind();
                    } else if (node.getName().equals("Assignment Statement")) {
                        // If we're an assignment statement, infer the type from the expression.
                        type = node.getKind();
                    }
                    node.getSymbolTable().put(node.getName(), type);

                } else {
                    // Variable already declared in the current or a parent scope.
                    // This is a redeclaration error.
                    System.out.println("Error: variable " + node.getName() + " already declared in the current or a parent scope");
                }
                // ... update the current node pointer to ourselves.
                node = node;
                // ... set the type of the variable in the node.
                if (node.getName().equals("Variable Declaration")) {
                    Node parentNode = node.getParent();
                    node.setType(parentNode.getKind());
                } else if (node.getName().equals("Assignment Statement")) {
                    node.setType(varType);
                } else {
                    // There are children, so note these interior/branch nodes and ...
                    traversalResult.append("<").append(node.getName()).append("> \n");
                    // ... recursively expand them.
                    for (int i = 0; i < node.getChildren().size(); i++) {
                        expand(node.getChildren().get(i), depth + 1, traversalResult);
                    }
                }
            }
 */

    /* Program Symbol Table => OLD SYMBOL TABLE => WILL BE DELETED

    System.out.println("Symbol Table for program " + PROGRAM_NUMBER + " ...");
    System.out.println(" ----------------------------------------------------------------------");
            for (String[] row : symbolTable) {
        System.out.printf("| %-20s %-20s %-20s %-5s |%n",row[0], row[1], row[2],row[3]);
        System.out.println(" ----------------------------------------------------------------------");
    }*/

    /*
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

     */

    /*
    THIS CODE IS FROM match() in SemanticAnalysis.java ...

    if (Objects.equals(current_token, "INT")
                            || Objects.equals(current_token, "STRING")
                            || Objects.equals(current_token, "BOOLEAN")) {

                        // Build the symbolTable here
                        symbolTable.add(new String[]{
                                Lexer.tokens.get(token_pointer+1).getSymbol(),
                                current_token,
                                String.valueOf(scope_num),
                                String.valueOf(token.getLineNum())});
                    }
     */
    /*
    else if (Objects.equals(name, "Variable Declaration")) {
                left = node.getChildren().get(0);
                right = node.getChildren().get(1);

                String typeV = left.getName();
                String nameV = right.getName();

                System.out.println("Variable Declaration => left is typeV " + typeV);
                System.out.println("Variable Declaration => right is nameV " + nameV);

                // add to symbol table if not there

                if (symbolTable.getSymbol(nameV) == null) {
                    symbolTable.addSymbol(nameV, typeV);
                    System.out.println("Variable Declaration => " + nameV + " is added to symbolTable\n");


                } else {
                    System.out.println("Variable " + right.getName() + " is declared more than once");
                }
            }
    else if (Objects.equals(node.getName(), "Assignment Statement")) {
                left = node.getChildren().get(0);
                right = node.getChildren().get(1);

                String idA = left.getName();
                String exprA = right.getName();

                System.out.println("\nAssignment Statement => left idA is " + idA);
                System.out.println("Assignment Statement => right exprA is " + exprA);

                // add to symbol table if not there

                if (symbolTable.getSymbol(idA) == null) {
                    System.out.println("\nAssignment Statement => Variable " + left.getName() + " is used before it is declared");
                } else {
                    System.out.println("Type checking happens here");
                    System.out.println("****************** " + symbolTable.getSymbol(idA));

                    // Integer type checking
                    if (Objects.equals(symbolTable.getSymbol(idA), "int")) {
                        try {
                            // integer is being assigned to another identifier which can hold a value of any type
                            if (exprA.length() == 1) {
                                if (symbolTable.getSymbol(exprA) != null) {
                                    if (Objects.equals(symbolTable.getSymbol(idA), symbolTable.getSymbol(exprA))) {
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
                    else if (Objects.equals(symbolTable.getSymbol(idA), "boolean")) {
                        // Boolean is being assigned to another identifier which can hold a value of any type
                        if (exprA.length() == 1) {
                            if (symbolTable.getSymbol(exprA) != null) {
                                if (Objects.equals(symbolTable.getSymbol(idA), symbolTable.getSymbol(exprA))) {
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
                            if (symbolTable.getSymbol(exprA) != null) {
                                if (Objects.equals(symbolTable.getSymbol(idA), symbolTable.getSymbol(exprA))) {
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


     */
}
