import java.util.HashMap;
import java.util.Map;

public class SymbolTables {

    // A map to store symbols and their corresponding values
    private final Map<String, String> symbolTable;

    // A map to store child symbol tables
    private final Map<Integer, SymbolTables> children;

    // The parent symbol table (if any)
    private final SymbolTables parent;

    // Constructor for creating the root symbol table
    public SymbolTables() {
        symbolTable = new HashMap<>();
        children = new HashMap<>();
        parent = null;
    }

    // Constructor for creating a child symbol table
    public SymbolTables(SymbolTables parent) {
        symbolTable = new HashMap<>();
        children = new HashMap<>();
        this.parent = parent;
    }

    // Method to add a symbol to the symbol table
    public void addSymbol(String identifier, String value) {
        symbolTable.put(identifier, value);
    }

    // Method to retrieve the value of a symbol from the symbol table
    public Object getSymbol(String identifier) {
        Object value = symbolTable.get(identifier);
        if (value == null && parent != null) {
            // If the symbol is not found in the current symbol table,
            // look for it in the parent symbol table (if any)
            return parent.getSymbol(identifier);
        }
        return value;
    }

    // Method to add a child symbol table
    public void addChild(Integer scope_number) {
        children.put(scope_number, new SymbolTables(this));
    }

    // Method to retrieve a child symbol table
    public SymbolTables getChild(Integer scope_number) {
        SymbolTables child = children.get(scope_number);
        if (child == null) {
            // If the child symbol table does not exist yet, create it
            // and add it to the children map
            child = new SymbolTables(this);
            children.put(scope_number, child);
        }
        return child;
    }
}
