public class Workstation2 {

    //public static void main(String[] args) {
        // Create a new root symbol table
        /*SymbolTables symbolTable = new SymbolTables();

// Open a new scope
        symbolTable.openScope(0);

// Add some variables to the symbol table
        symbolTable.addSymbol("x", "int");
        symbolTable.addSymbol("y", "int");

// Open a new nested scope
        symbolTable.openScope(1);

// Add a variable to the nested scope
        symbolTable.addSymbol("x", "double");

// Print the current symbol table
        symbolTable.printSymbolTable(); // Output: x : double, y : int

// Close the nested scope
        symbolTable.closeScope();

// Print the updated symbol table
        symbolTable.printSymbolTable(); // Output: x : int, y : int

    }*/

    /*
        private static void printSymbolTable(SymbolTable symbolTable) {
        System.out.println("+---------------+---------------+---------------+");
        System.out.println("| Variable Name |  Variable Type|     Scope     |");
        System.out.println("+---------------+---------------+---------------+");

        for (Symbol symbol : symbolTable.getAllSymbols()) {
            String name = symbol.getName();
            String type = symbol.getType();
            int scope = symbol.getScope();
            System.out.printf("| %13s | %13s | %13d |\n", name, type, scope);
        }

        System.out.println("+---------------+---------------+---------------+");
    }

     */


}