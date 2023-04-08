// Variant 6
// Vn = {S, A, B, C, E}
// Vt = {a, b}
// P = {
//    S -> aB
//    S -> AC
//    A -> a
//    A -> ASC
//    A -> BC
//    B -> b
//    B -> bS
//    C -> ε
//    C -> BA
//    E -> bB
// }

import grammar.Grammar;
import grammar.Production;

public class Main
{
    public static void main(String[] args) {
        Grammar grammar = new Grammar(
            new String[] {"S", "A", "B", "C", "E"},
            new String[] {"a", "b"},
            new Production[] {
                new Production("S", "aB"),
                new Production("S", "AC"),
                new Production("A", "a"),
                new Production("A", "ASC"),
                new Production("A", "BC"),
                new Production("B", "b"),
                new Production("B", "bS"),
                new Production("C", "ε"),
                new Production("C", "BA"),
                new Production("E", "bB")
            },
            "S"
        );

        grammar.convertToChomskyNormalForm();
    }
}
