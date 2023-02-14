import automaton.FiniteAutomaton;
import grammar.Grammar;
import grammar.Production;

//    Variant 6:
//        Vn={S, I, J, K},
//        Vt={a, b, c, e, n, f, m},
//        P = {
//        S → cI
//        I → bJ
//        I → fI
//        J → nJ
//        J → cS
//        I → eK
//        K → nK
//        I → e
//        K → m
//    }

public class Main
{
    public static void main(String[] args)
    {
        Grammar grammar = new Grammar(
                new char[]{'S', 'I', 'J', 'K'},
                new char[]{'a', 'b', 'c', 'e', 'n', 'f', 'm'},
                new Production[]{
                        new Production("S", "cI"),
                        new Production("I", "bJ"),
                        new Production("I", "fI"),
                        new Production("J", "nJ"),
                        new Production("J", "cS"),
                        new Production("I", "eK"),
                        new Production("K", "nK"),
                        new Production("I", "e"),
                        new Production("K", "m")
                },
                'S'
        );

        System.out.println("\n1. Generate 5 random words: ");

        for (int i = 0; i < 5; i++) {
            System.out.println(grammar.generateWord());
        }

        FiniteAutomaton FA = grammar.toFiniteAutomaton();
        System.out.println("\n2. " + FA.toString());

        System.out.println("\n3. Test cases: ");
        System.out.println("3.0 ce: " + FA.isWordValid("ce"));
        System.out.println("3.1. cm: " + FA.isWordValid("cm"));
        System.out.println("3.2. cenm: " + FA.isWordValid("cenm"));
        System.out.println("3.3. cfbccbnccenm: " + FA.isWordValid("cfbccbnccenm"));
        System.out.println("3.4. cbccbccbnnccfem: " + FA.isWordValid("cbccbccbnnccfem"));
        System.out.println("3.5. cc: " + FA.isWordValid("cc"));
        System.out.println("3.6. acenm: " + FA.isWordValid("acenm"));
        System.out.println("3.7. cennnnnnnnnnn: " + FA.isWordValid("cennnnnnnnnnn"));
        System.out.println("3.8. cennnnnnnnnnnm: " + FA.isWordValid("cennnnnnnnnnnm"));
        System.out.println("3.9. hello: " + FA.isWordValid("hello"));
        System.out.println("3.10. cet: " + FA.isWordValid("cet"));
    }
}
