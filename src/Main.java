import automaton.FiniteAutomaton;
import automaton.Transition;
import grammar.Grammar;
import grammar.Production;

import java.util.Arrays;

//    Variant 6: First laboratory work
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

//      Variant 6: Second laboratory work
//        Q = {q0,q1,q2,q3,q4},
//        ∑ = {a,b},
//        F = {q4},
//        δ(q0,a) = q1,
//        δ(q1,b) = q1,
//        δ(q1,b) = q2,
//        δ(q2,b) = q3,
//        δ(q3,a) = q1,
//        δ(q2,a) = q4.

public class Main
{
    public static void main(String[] args)
    {
        Grammar grammar = new Grammar(
                new String[]{"S", "I", "J", "K"},
                new String[]{"a", "b", "c", "e", "n", "f", "m"},
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
                "S"
        );

        FiniteAutomaton FA = new FiniteAutomaton(
                new String[]{"q0", "q1", "q2", "q3", "q4"},
                new String[]{"a", "b"},
                new Transition[]{
                        new Transition("q0", 'a', "q1"),
                        new Transition("q1", 'b', "q1"),
                        new Transition("q1", 'b', "q2"),
                        new Transition("q2", 'b', "q3"),
                        new Transition("q3", 'a', "q1"),
                        new Transition("q2", 'a', "q4"),
                },
                "q0",
                new String[]{"q4"}
        );

        System.out.println("\n 1. Classify the grammar based on Chomsky hierarchy: ");
        System.out.println("===== Current grammar is of: " + grammar.classifyGrammar() + " =====");

        System.out.println("\n 2. Convert the finite automaton regular grammar: ");
        System.out.println("===== FA -> Grammar: \n" + FA.convertToRegularGrammar()+ " \n=====");

//        System.out.println("\n 3. Generate words : ");
//        for (int i = 0; i < 5; i++) {
//            System.out.println(FA.convertToRegularGrammar().generateWord());
//        }
//        System.out.println("===== Word: \n" + Arrays.toString(FA.convertToRegularGrammar().getProductions()) + " \n=====");


//        Grammar grammar = new Grammar(
//                new char[]{'S', 'I', 'J', 'K'},
//                new char[]{'a', 'b', 'c', 'e', 'n', 'f', 'm'},
//                new Production[]{
//                        new Production("S", "cI"),
//                        new Production("I", "bJ"),
//                        new Production("I", "fI"),
//                        new Production("J", "nJ"),
//                        new Production("J", "cS"),
//                        new Production("I", "eK"),
//                        new Production("K", "nK"),
//                        new Production("I", "e"),
//                        new Production("K", "m")
//                },
//                'S'
//        );
//
//        System.out.println("\n1. Generate 5 random words: ");
//
//        for (int i = 0; i < 5; i++) {
//            System.out.println(grammar.generateWord());
//        }
//
//        FiniteAutomaton FA = grammar.toFiniteAutomaton();
//        System.out.println("\n2. " + FA.toString());
//
//        System.out.println("\n3. Test cases: ");
//        System.out.println("3.0 ce: " + FA.isWordValid("ce"));
//        System.out.println("3.1. cm: " + FA.isWordValid("cm"));
//        System.out.println("3.2. cenm: " + FA.isWordValid("cenm"));
//        System.out.println("3.3. cfbccbnccenm: " + FA.isWordValid("cfbccbnccenm"));
//        System.out.println("3.4. cbccbccbnnccfem: " + FA.isWordValid("cbccbccbnnccfem"));
//        System.out.println("3.5. cc: " + FA.isWordValid("cc"));
//        System.out.println("3.6. acenm: " + FA.isWordValid("acenm"));
//        System.out.println("3.7. cennnnnnnnnnn: " + FA.isWordValid("cennnnnnnnnnn"));
//        System.out.println("3.8. cennnnnnnnnnnm: " + FA.isWordValid("cennnnnnnnnnnm"));
//        System.out.println("3.9. hello: " + FA.isWordValid("hello"));
//        System.out.println("3.10. cet: " + FA.isWordValid("cet"));

    }
}
