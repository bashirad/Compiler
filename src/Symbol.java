public record Symbol(String name, String type, int scopeId, boolean isInitialized, boolean isUsed) {
    public Symbol withInitialized(boolean initialized) {
        return new Symbol(name, type, scopeId, initialized, isUsed);
    }

    public Symbol withUsed(boolean used) {
        return new Symbol(name, type, scopeId, isInitialized, used);
    }
}
