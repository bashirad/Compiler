import imp.ICompiler;
import lexer.Lexer;

public class Compiler implements ICompiler {
    public static void main(String[] args) {

        System.out.println("Hello world!");

        /** Execute the Lexer                   */
        Lexer.init();

        /** Execute the Parser                  */


        /** Execute the Semantics ...           */


        /** Execute the 6502 Code Generator     */

    }

}
