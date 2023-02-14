import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class store {
    public static String readChars () {

        int lastPosition = 0;
        int currentPosition = 0;
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
            System.out.print(character);        //Display the Character
            code += (char)(character);
            lastPosition = currentPosition;         // update the last position
            currentPosition++;                      // increment the current position

            /*if (Character.isWhitespace(code.charAt(2))) {
                System.out.print(code);
            }*/

        }
        Pattern pattern = Pattern.compile("6", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(code);
        boolean matchFound = matcher.find();
        if(matchFound) {
            System.out.println("Match Found! " + pattern.pattern());
            System.out.println("Matcher group! " + matcher.group());
        }
        else {
            System.out.println("Match Not Found!");
        }
        if ('\n' == code.charAt(33)) {
            //System.out.print(code);
        }
        return code;
    }

    public static void readAllWords () {

        try {
            String data = "";
            File myObj = new File("C:\\Users\\Bashir\\Documents\\Bashirs_Code_all\\Java\\cmpt432\\src\\code.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNext()) {

                data = myReader.next();
                System.out.print(data);

                Pattern pattern = Pattern.compile("9", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(data);
                boolean matchFound = matcher.find();
                if(matchFound) {
                    System.out.println("    Match Found! " + pattern.pattern());
                }
                else {
                    System.out.println("    Match Not Found!");
                }
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        int edges [][] = {  /*  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z  $  {  }  (  )  =  "  "  /  !  +  * */
                /* state 0 */         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 1 */         {23,23,23,23,23,23,23,23,23,23,22,22,22,22,22,25,22,22,18,22,22,22,22,22,22, 5,22,22,22,30,22,22,13,22,22,22, 2, 3, 4,10,11,12,20,21,38,35,37,40 },
                /* state 2 */         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 3 */         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 4 */         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 5 */         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 6 */         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 7 */         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 8 */         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 9 */         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 10 */         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 11 */         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 12 */         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 13 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 14 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 15 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 16 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 17 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 18 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 19 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 20 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 21 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 22 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 23 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 24 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 25 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 26 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,26, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 27 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,27, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 28 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,28, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 29 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 30 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 31 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,31, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 32 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 33 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,33, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 34 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 35 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 36 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,36, 0, 0, 0, 0, 0, 0 },
                /* state 37 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 38 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 39 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                /* state 40 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,40 },
                /* state 41 */     {0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,41, 0, 0, 0 },
        };
        System.out.println("number of rows are: " + edges.length);
        System.out.println("number of columns are: " + edges[0].length);
    }

    public static void patternMatch (){
        /** code for recognizing each of the tokens using RegEx
        if (String.valueOf(ch).matches(EOP)) {
            code2 += code.charAt(i);
            Matcher matcherEOP = patternEOP.matcher(code2);
            Tokens endOfProgram = new Tokens("EOP", matcherEOP.toString(), LINE_NUMBER, POSITION_NUMBER);
            Lexer.log(PROGRAM_NUMBER, true, "Lexer", endOfProgram.lexemeName,
                    endOfProgram.symbol, endOfProgram.lineNum, endOfProgram.positionNum);
        } else if (String.valueOf(ch).matches(OPEN_BLOCK)) {
            Tokens openBlock = new Tokens("OPEN_BLOCK", OPEN_BLOCK, LINE_NUMBER, POSITION_NUMBER);
            Lexer.log(PROGRAM_NUMBER, true, "Lexer", openBlock.lexemeName, openBlock.symbol,
                    openBlock.lineNum, openBlock.positionNum);
        } else if (String.valueOf(ch).matches(CLOSE_BLOCK)) {
            Tokens closeBlock = new Tokens("CLOSE_BLOCK", CLOSE_BLOCK, LINE_NUMBER, POSITION_NUMBER);
            Lexer.log(PROGRAM_NUMBER, true, "Lexer", closeBlock.lexemeName, closeBlock.symbol,
                    closeBlock.lineNum, closeBlock.positionNum);
        } else if (String.valueOf(ch).matches(ID)) {
            Tokens id = new Tokens("ID", ID, LINE_NUMBER, POSITION_NUMBER);
            Lexer.log(PROGRAM_NUMBER, true, "Lexer", id.lexemeName, id.symbol,
                    id.lineNum, id.positionNum);
        }else if (String.valueOf(ch).matches(I_TYPE)) {
            Tokens iType = new Tokens("I_Type", I_TYPE, LINE_NUMBER, POSITION_NUMBER);
            Lexer.log(PROGRAM_NUMBER, true, "Lexer", iType.lexemeName, iType.symbol,
                    iType.lineNum, iType.positionNum);
        } else if (String.valueOf(ch).matches(ASSIGN_OP)) {
            Tokens assignOp = new Tokens("ASSIGN_OP", ASSIGN_OP, LINE_NUMBER, POSITION_NUMBER);
            Lexer.log(PROGRAM_NUMBER, true, "Lexer", assignOp.lexemeName, assignOp.symbol,
                    assignOp.lineNum, assignOp.positionNum);
        } else if (String.valueOf(ch).matches(OPEN_PARENTHESIS)) {
            Tokens openParenthesis = new Tokens("OPEN_PARENTHESIS", OPEN_PARENTHESIS, LINE_NUMBER, POSITION_NUMBER);
            Lexer.log(PROGRAM_NUMBER, true, "Lexer", openParenthesis.lexemeName, openParenthesis.symbol,
                    openParenthesis.lineNum, openParenthesis.positionNum);
        } else if (String.valueOf(ch).matches(KEYWORDS)) {
            Tokens openParenthesis = new Tokens("OPEN_PARENTHESIS", OPEN_PARENTHESIS, LINE_NUMBER, POSITION_NUMBER);
            Lexer.log(PROGRAM_NUMBER, true, "Lexer", openParenthesis.lexemeName, openParenthesis.symbol,
                    openParenthesis.lineNum, openParenthesis.positionNum);
        } else if (String.valueOf(ch).matches(OPEN_QUOTES)) {
            Tokens openQuotes = new Tokens("OPEN_QUOTES", OPEN_QUOTES, LINE_NUMBER, POSITION_NUMBER);
            Lexer.log(PROGRAM_NUMBER, true, "Lexer", openQuotes.lexemeName, openQuotes.symbol,
                    openQuotes.lineNum, openQuotes.positionNum);
        } else if (String.valueOf(ch).matches(CLOSE_QUOTES)) {
            Tokens closeQuotes = new Tokens("CLOSE_QUOTES", CLOSE_QUOTES, LINE_NUMBER, POSITION_NUMBER);
            Lexer.log(PROGRAM_NUMBER, true, "Lexer", closeQuotes.lexemeName, closeQuotes.symbol,
                    closeQuotes.lineNum, closeQuotes.positionNum);
        } else if (String.valueOf(ch).matches(DIGIT)) {
            Tokens digit = new Tokens("DIGIT", DIGIT, LINE_NUMBER, POSITION_NUMBER);
            Lexer.log(PROGRAM_NUMBER, true, "Lexer", digit.lexemeName, digit.symbol,
                    digit.lineNum, digit.positionNum);
        } else if (String.valueOf(ch).matches(WHITE_SPACE)) {
            Tokens whiteSpace = new Tokens("WHITE_SPACE", WHITE_SPACE, LINE_NUMBER, POSITION_NUMBER);
            Lexer.log(PROGRAM_NUMBER, true, "Lexer", whiteSpace.lexemeName, whiteSpace.symbol,
                    whiteSpace.lineNum, whiteSpace.positionNum);
        } else if (String.valueOf(ch).matches(SYMBOLS)) {
            code2 += ch;
            Matcher matchSymbols = patternSymbols.matcher(code2);
            if (matchSymbols.find()) System.out.println("Found symbol match, Great work");
            Tokens symbols = new Tokens("SYMBOLS", SYMBOLS, LINE_NUMBER, POSITION_NUMBER);
            Lexer.log(PROGRAM_NUMBER, true, "Lexer", symbols.lexemeName, symbols.symbol,
                    symbols.lineNum, symbols.positionNum);
        } else {
            System.out.println("There is an error");
        }




         THIS IS WHERE I PUT THE 4 CASES FOR PATTER MATCHING ON MONDAY 2/13/2023
         String code2 = "";
         code2 += ch;

         Matcher matchDigits = patternDigit.matcher(String.valueOf(code2));
         // code for recognizing digit using RegEx
        if (matchDigits.matches()) {
            Tokens symbols = new Tokens("DIGIT", matchDigits.group(), LINE_NUMBER, POSITION_NUMBER);
            Lexer.log(PROGRAM_NUMBER, true, "Lexer", symbols.lexemeName, symbols.symbol,
                    symbols.lineNum, symbols.positionNum);
        } else {
            Matcher matchID = patternID.matcher(String.valueOf(code2));
            // code for recognizing id using RegEx
            if (matchID.matches()) {

                // create empty string and append the char
                +
                        Tokens id = new Tokens("ID", matchID.group(), LINE_NUMBER, POSITION_NUMBER);
                Lexer.log(PROGRAM_NUMBER, true, "Lexer", id.lexemeName, id.symbol,
                        id.lineNum, id.positionNum);
            } else {
                Matcher matchSymbol = patternSymbols.matcher(String.valueOf(code2));
                if (matchSymbol.matches()) {
                    Tokens symbols = new Tokens("SYMBOLS", matchSymbol.group(), LINE_NUMBER, POSITION_NUMBER);
                    Lexer.log(PROGRAM_NUMBER, true, "Lexer", symbols.lexemeName, symbols.symbol,
                            symbols.lineNum, symbols.positionNum);
                } else {
                    Matcher matchWhiteSpace = patternWhiteSpace.matcher(String.valueOf(code2));
                    // code for recognizing whitespace using RegEx
                    if (matchWhiteSpace.matches()) {
                        Tokens whiteSpace = new Tokens("WHITE_SPACE", "\' \'", LINE_NUMBER, POSITION_NUMBER);
                        //Lexer.log(PROGRAM_NUMBER, true, "Lexer", whiteSpace.lexemeName, whiteSpace.symbol,
                        //whiteSpace.lineNum, whiteSpace.positionNum);
                    } else {
                        System.out.println("There is an error");
                    }
                }
            }

            POSITION_NUMBER++;
            if ('\n' == code.charAt(i)) {
                LINE_NUMBER++;
                POSITION_NUMBER = 0;
            }
        }

         Matcher matchKeywords = patternKeyword.matcher(code);
         // code for recognizing digit using RegEx
         while (matchKeywords.find()) {
         Tokens keyword = new Tokens("KEYWORD", matchKeywords.group(), LINE_NUMBER, POSITION_NUMBER);
         Lexer.log(PROGRAM_NUMBER, true, "Lexer", keyword.lexemeName, keyword.symbol,
         keyword.lineNum, keyword.positionNum);
         }
         */
    }
}
