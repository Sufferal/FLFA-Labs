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
                        new Production("K", "m"),
                },
                'S'
        );

        System.out.println("\n 1. Classify the grammar based on Chomsky hierarchy: ");
        System.out.println("===== Current grammar is of: " + grammar.classifyGrammar() + " =====");
    }
}
