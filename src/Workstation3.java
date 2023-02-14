import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Workstation3 {

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            Pattern keywordPattern = Pattern.compile("\\b(print|string|int)\\b");
            Pattern idPattern = Pattern.compile("\\b[a-zA-Z]\\d*\\b");
            Pattern digitPattern = Pattern.compile("\\b\\d+\\b");
            Pattern symbolPattern = Pattern.compile("[{}();]");

            while (line != null) {
                StringBuilder currentToken = new StringBuilder();
                for (char c : line.toCharArray()) {
                    String s = Character.toString(c);
                    Matcher keywordMatcher = keywordPattern.matcher(currentToken.toString());
                    Matcher idMatcher = idPattern.matcher(currentToken.toString());
                    Matcher digitMatcher = digitPattern.matcher(currentToken.toString());
                    Matcher symbolMatcher = symbolPattern.matcher(currentToken.toString());

                    if (symbolMatcher.matches()) {
                        System.out.println("Symbol found: " + currentToken.toString());
                        currentToken.setLength(0);
                    } else if (Character.isWhitespace(c) || symbolMatcher.find()) {
                        if (keywordMatcher.matches()) {
                            System.out.println("Keyword found: " + currentToken.toString());
                        } else if (idMatcher.matches()) {
                            System.out.println("Identifier found: " + currentToken.toString());
                        } else if (digitMatcher.matches()) {
                            System.out.println("Digit found: " + currentToken.toString());
                        }
                        currentToken.setLength(0);
                    } else {
                        currentToken.append(c);
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
