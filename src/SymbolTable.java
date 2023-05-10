import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private final int scopeId;
    private final Map<String, Symbol> symbolMap;

    public SymbolTable(int scopeId) {
        this.scopeId = scopeId;
        this.symbolMap = new HashMap<>();
    }
    public void addSymbol(Symbol symbol) {
        symbolMap.put(symbol.name(), symbol);
    }

    public Symbol getSymbol(String name) {
        Symbol symbol = symbolMap.get(name);
        if (symbol != null) {
            return symbol;
        }
        int parentScopeId = getParentScopeId();
        while (parentScopeId != -1) {
            SymbolTable parentSymbolTable = SymbolTableTree.symbolTables.get(parentScopeId);
            symbol = parentSymbolTable.symbolMap.get(name);
            if (symbol != null) {
                return symbol;
            }
            parentScopeId = parentSymbolTable.getParentScopeId();
        }
        return null;
    }

    public int getParentScopeId() {
        int parentScopeId = -1;
        for (int i = scopeId - 1; i >= 0; i--) {
            if (SymbolTableTree.getSymbolTable(i) != null) {
                parentScopeId = i;
                break;
            }
        }
        return parentScopeId;
    }

    public int getScopeId() {
        return scopeId;
    }
    public List<Symbol> getSymbols() {
        return new ArrayList<>(symbolMap.values());
    }
    public void printSymbolTable() {
        System.out.println("Symbol table for scope " + scopeId + ":");
        System.out.println("+-----------------+-----------------+-----------------+");
        System.out.println("| Variable        | Type            | Scope           |");
        System.out.println("+-----------------+-----------------+-----------------+");
        for (Symbol symbol : symbolMap.values()) {
            System.out.printf("| %-15s | %-15s | %-15d |\n", symbol.name(), symbol.type(), scopeId);
        }
        System.out.println("+-----------------+-----------------+-----------------+");
    }

}
