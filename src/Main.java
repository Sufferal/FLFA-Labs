import automaton.FiniteAutomaton;
import automaton.Transition;
import grammar.Grammar;
import grammar.Production;

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
    public static void main(String[] args) {
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
                        new Transition("q0", "a", "q1"),
                        new Transition("q1", "b", "q1"),
                        new Transition("q1", "b", "q2"),
                        new Transition("q2", "b", "q3"),
                        new Transition("q3", "a", "q1"),
                        new Transition("q2", "a", "q4"),
                },
                "q0",
                new String[]{"q4"}
        );

        System.out.println("\n 1. Classify the grammar based on Chomsky hierarchy: ");
        System.out.println(grammar);
        System.out.println("===== Current grammar is of: " + grammar.classifyGrammar() + " =====");

        System.out.println("\n 2. Convert the finite automaton to regular grammar: ");
        Grammar FAtoGrammar = FA.convertToRegularGrammar();
        System.out.println(FA);
        System.out.println(FAtoGrammar);

        System.out.println("\n 2.1. Generate random words from converted grammar: ");
        for (int i = 0; i < 10; i++) {
            System.out.println(FAtoGrammar.generateWord());
        }

        System.out.println("\n 3. Check if Finite Automaton is deterministic: ");
        System.out.println("===== FA is deterministic: " + FA.isDeterministic() + " =====");

        System.out.println("\n 4. NFA to DFA: ");
        System.out.println("===== DFA: " + FA.convertToDFA() + " =====");

        System.out.println("\n 5. Display Finite Automaton graph: ");
        System.out.println(FA);
        FA.showGraph();
    }
}
