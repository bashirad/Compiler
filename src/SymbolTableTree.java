import java.util.HashMap;
import java.util.Map;

public class SymbolTableTree {

    public static Map<Integer, SymbolTable> symbolTables = null;
    private final SymbolTable globalSymbolTable;

    public SymbolTableTree() {
        symbolTables = new HashMap<>();
        this.globalSymbolTable = new SymbolTable(0);
        symbolTables.put(0, globalSymbolTable);
    }

    public void addSymbolTable(int scopeId, SymbolTable symbolTable) {
        symbolTables.put(scopeId, symbolTable);
    }

    public static SymbolTable getSymbolTable(int scopeId) {
        return symbolTables.get(scopeId);
    }

    public void printSymbolTables() {
        System.out.println("Symbol table tree:");
        for (SymbolTable symbolTable : symbolTables.values()) {
            symbolTable.printSymbolTable();
        }
    }

    private int getParentScopeId(int scopeId) {
        // The parent of the global scope is itself
        if (scopeId == 0) {
            return 0;
        }
        // Find the parent of the current scope
        for (int i = scopeId - 1; i >= 0; i--) {
            if (symbolTables.containsKey(i)) {
                return i;
            }
        }
        return -1; // Parent not found
    }

    private String getSymbolsString(SymbolTable symbolTable) {
        StringBuilder symbols = new StringBuilder();
        for (Symbol symbol : symbolTable.getSymbols()) {
            symbols.append(symbol.name()).append(symbol.type()).append("\n");
            //symbols.append(symbol.toString()).append("; ");
        }
        return symbols.toString();
    }

    private String getSymbols(SymbolTable symbolTable) {
        StringBuilder symbols = new StringBuilder();
        for (Symbol symbol : symbolTable.getSymbols()) {
            symbols.append(symbol.name());
        }
        return symbols.toString();
    }

    private String getTypes(SymbolTable symbolTable) {
        StringBuilder symbols = new StringBuilder();
        for (Symbol symbol : symbolTable.getSymbols()) {
            symbols.append(symbol.type());
        }
        return symbols.toString();
    }

}
