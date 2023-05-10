import imp.ICompiler;

public class Compiler implements ICompiler {
    public static void main(String[] args) {

        if (args.length > 0) {
            String filePath = args[0];
            // Do something with the file

            /** Execute the Lexer                   */
            Lexer.init_Lexer(filePath);

        }

    }

}