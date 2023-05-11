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
    public void clear() {
        symbolMap.clear();
    }
    public void replaceSymbol(Symbol newSymbol) {
        Symbol oldSymbol = symbolMap.get(newSymbol.name());
        if (oldSymbol != null) {
            symbolMap.remove(oldSymbol.name());
            symbolMap.put(newSymbol.name(), newSymbol);
        }
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
        System.out.println("+-----------------+-----------------+-----------------+-----------------+-----------------+");
        System.out.println("| Variable        | Type            | isInitialized   | IsUsed          | Scope           |");
        System.out.println("+-----------------+-----------------+-----------------+-----------------+-----------------+");
        for (Symbol symbol : symbolMap.values()) {
            System.out.printf("| %-15s | %-15s | %-15b | %-15b | %-15d |\n", symbol.name(), symbol.type(), symbol.isInitialized(), symbol.isUsed(), scopeId);
        }
        System.out.println("+-----------------+-----------------+-----------------+-----------------+-----------------+");
    }
    public int errorSymbolTable() {
        int warningCount = 0;
        for (Symbol symbol : symbolMap.values()) {
            if (symbol.isInitialized() && !symbol.isUsed()) {
                warningCount++;
                System.out.println("WARNING Semantic Analysis - Variable " + symbol.name() + " is declared and initialized but was never used.");
            } else if (!symbol.isInitialized() && symbol.isUsed()) {
                warningCount++;
                System.out.println("WARNING Semantic Analysis - Variable " + symbol.name() + " is used before it is initialized.");
            } else if (!symbol.isUsed()) {
                warningCount++;
                System.out.println("WARNING Semantic Analysis - Variable " + symbol.name() + " is declared but was never used.");
            }
        }
        return warningCount;
    }

}
