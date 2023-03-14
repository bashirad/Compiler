import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// TODO 1. develop the code the will run the lexer here
// TODO 2. make use of RegEx
public class Workstation2 {

    private static String OPEN_BLOCK;
    private static String CLOSE_BLOCK;
    private static String ID;
    private static String I_TYPE;
    private static String EOP;
    private static String ASSIGN_OP;
    private static String OPEN_PARENTHESIS;
    private static String CLOSE_PARENTHESIS;
    private static String OPEN_QUOTES;
    private static String CLOSE_QUOTES;
    private static String KEYWORDS;
    private static String DIGIT;
    private static String WHITE_SPACE;
    private static String SYMBOLS;
    private static int LINE_NUMBER = 1;
    private static int POSITION_NUMBER = 0;
    private static int PROGRAM_NUMBER = 1;
    private static int lastPosition = -1;
    private static int currentPosition = 0;
    private static String KEYWORD_CANDIDATE = " ";

    public static void main(String[] args ) {
        init();
    }

    public static void init() {
        scan();
        //readChars();
        //readAllWords();

    }

    public static void scan() {

        System.out.println("index of char ( is " + readChars().indexOf('('));

    }
    public static String readChars () {

        int lastPosition = 0;
        int currentPosition = 0;
        Scanner scan = new Scanner(System.in);

        File file =new File("C:\\Users\\Bashir\\Documents\\Bashirs_Code_all\\Java\\cmpt432\\src\\code.txt");     //Creation of File Descriptor for input file
        FileReader file_read = null;   //Creation of File Reader object
        try {
            file_read = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br=new BufferedReader(file_read);  //Creation of BufferedReader object
        int c = 0;
        String code = "";
        while(true)         //Read char by Char
        {
            try {
                if (!((c = br.read()) != -1)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            char character = (char) c;          //converting integer to char
            //System.out.print(character);        //Display the Character
            code += (char)(character);
            lastPosition = currentPosition;         // update the last position
            currentPosition++;                      // increment the current position

        }
        return code;
    }
}
