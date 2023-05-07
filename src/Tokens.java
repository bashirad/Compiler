public class Tokens extends Tree {
    String lexemeName;
    String symbol;
    int lineNum;
    int positionNum;
    public Tokens (String lexemeName, String symbol, int lineNum, int positionNum ) {
        super();

        /** initialize variables    */
        this.lexemeName = lexemeName;
        this.symbol = symbol;
        this.lineNum = lineNum;
        this.positionNum = positionNum;
    }
    public String getLexemeName() {
        return this.lexemeName;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public int getLineNum() {
        return this.lineNum;
    }

    public int getPosNum() {
        return this.positionNum;
    }
}