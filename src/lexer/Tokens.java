package lexer;

public class Tokens {
    String lexemeName;
    String symbol;
    int lineNum;
    int positionNum;
    public Tokens (String lexemeName, String symbol, int lineNum, int positionNum ) {

        /** initialize variables    */
        this.lexemeName = lexemeName;
        this.symbol = symbol;
        this.lineNum = lineNum;
        this.positionNum = positionNum;
    }

}
