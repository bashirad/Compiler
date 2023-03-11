import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Workstation4 {
    public static void main(String[] args) {
        String input = "Hello, world!";
        Pattern pattern = Pattern.compile("[a-z]");

        for (int i = 0; i < input.length(); i++) {
            String character = input.substring(i, i+1);
            Matcher matcher = pattern.matcher(character);
            if (matcher.matches()) {
                System.out.print(character);
            }
        }

    }
}