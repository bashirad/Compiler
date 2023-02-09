package lexer;

public class Tokens {
    String lexmineName;
    String symbol;
    int lineNum;
    int positionNum;
    public Tokens (String lexmineName, String symbol, int lineNum, int positionNum ) {
        // TODO search for how to access these tokens and print them
        this.lexmineName = lexmineName;
        this.symbol = symbol;
        this.lineNum = lineNum;
        this.positionNum = positionNum;
    }
    public void getLexmineName() {

    }
}
