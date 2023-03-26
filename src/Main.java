import lexer.Lexer;
import lexer.Token;

import java.util.List;

public class Main
{
    public static void main(String[] args) {
        String input = "5+(6-2)*3/4";
        Lexer lexer = new Lexer(input);

        List<Token> tokens = lexer.tokenize();
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
