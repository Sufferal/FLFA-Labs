import lexer.Lexer;
import parser.Parser;

public class Main
{
    public static void main(String[] args) {
        String input_1 = "print A;";
        Lexer lexer = new Lexer(input_1);
        System.out.println("===== 1. For input: " + input_1 + " the tokens are: =====");
        lexer.printTokens();
        Parser parser = new Parser(lexer);
        parser.parse();
        System.out.println("\nAbstract Syntax Tree:");
        parser.printAST();

        String input_2 = "B=10;";
        lexer = new Lexer(input_2);
        System.out.println("\n\n===== 2. For input: " + input_2 + " the tokens are: =====");
        lexer.printTokens();
        parser = new Parser(lexer);
        parser.parse();
        System.out.println("\nAbstract Syntax Tree:");
        parser.printAST();

        String input_3 = "if(a==b){return true;}else{return false;}";
        lexer = new Lexer(input_3);
        System.out.println("\n\n===== 3. For input: " + input_3 + " the tokens are: =====");
        lexer.printTokens();
        parser = new Parser(lexer);
        parser.parse();
        System.out.println("\nAbstract Syntax Tree:");
        parser.printAST();
        
        String input_4 = "while(i<10){print i;i++;}";
        lexer = new Lexer(input_4);
        System.out.println("\n\n===== 4. For input: " + input_4 + " the tokens are: =====");
        lexer.printTokens();
        parser = new Parser(lexer);
        parser.parse();
        System.out.println("\nAbstract Syntax Tree:");
        parser.printAST();
    }
}
