import imp.ICompiler;

public class Compiler implements ICompiler {
    // TODO main must take an input of a file
    public static void main(String[] args) {

        if (args.length > 0) {
            String filePath = args[0];
            // Do something with the file

        /** Execute the Lexer                   */
        Lexer.init(filePath);
        /** Execute the Parser                  */


        /** Execute the Semantics ...           */


        /** Execute the 6502 Code Generator     */
    }

    }

}
