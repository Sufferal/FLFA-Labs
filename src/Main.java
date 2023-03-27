import lexer.Lexer;

public class Main
{
    public static void main(String[] args) {
        String input_1 = "5+(6-2)*3/4";
        Lexer lexer = new Lexer(input_1);
        System.out.println("===== 1. For input: " + input_1 + " the tokens are: =====");
        lexer.printTokens();

        String input_2 = "if (a == b) { return true; }";
        lexer = new Lexer(input_2);
        System.out.println("\n\n===== 2. For input: " + input_2 + " the tokens are: =====");
        lexer.printTokens();

        String input_3 = "for (int i = 0; i < 10; i++) { print(i); }";
        lexer = new Lexer(input_3);
        System.out.println("\n\n===== 3. For input: " + input_3 + " the tokens are: =====");
        lexer.printTokens();

        String input_4 = "while (i < 10) { println(i); i++; }";
        lexer = new Lexer(input_4);
        System.out.println("\n\n===== 4. For input: " + input_4 + " the tokens are: =====");
        lexer.printTokens();

        String input_5 = "char c = 'a';";
        lexer = new Lexer(input_5);
        System.out.println("\n\n===== 5. For input: " + input_5 + " the tokens are: =====");
        lexer.printTokens();
    }
}
