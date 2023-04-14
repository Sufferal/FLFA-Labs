import automaton.FiniteAutomaton;
import automaton.Transition;
import grammar.ChomskyType;
import grammar.Grammar;
import grammar.Production;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class GrammarTest {

  @Test
  void generateWord() {
    Grammar grammar = Grammar.createBaseGrammar();
    for (int i = 0; i < 10; i++) {
      String generatedWord = grammar.generateWord();
      boolean isWordValild = true;

      for (int j = 0; j < generatedWord.length(); j++) {
        String symbol = String.valueOf(generatedWord.charAt(j));
        if(!Arrays.asList(grammar.getTerminalVariables()).contains(symbol)) {
          isWordValild = false;
          break;
        }
      }

      // System.out.println(generatedWord + " " + isWordValild);
      Assertions.assertTrue(isWordValild, generatedWord);
    }
  }

  @Test
  void toFiniteAutomaton() {
    Grammar grammar = Grammar.createBaseGrammar();
    FiniteAutomaton FA = grammar.toFiniteAutomaton();

    // System.out.println("\n2. " + FA.toString());
    Assertions.assertArrayEquals(FA.getStates(), new String[]{"S", "I", "J", "K"});
    Assertions.assertArrayEquals(FA.getAlphabet(), new String[]{"a", "b", "c", "e", "n", "f", "m"});
    Assertions.assertEquals(FA.getInitialState(), "S");
    Assertions.assertArrayEquals(FA.getFinalStates(), new String[]{"X"});
    Assertions.assertEquals(Arrays.toString(FA.getTransitions()), Arrays.toString(new Transition[]{
            new Transition("S", "c", "I"),
            new Transition("I", "b", "J"),
            new Transition("I", "f", "I"),
            new Transition("J", "n", "J"),
            new Transition("J", "c", "S"),
            new Transition("I", "e", "K"),
            new Transition("K", "n", "K"),
            new Transition("K", "m", "X"),
            new Transition("K", "m", "X"),
    }));
  }

  @Test
  void classifyGrammar() {
    Grammar grammar = Grammar.createBaseGrammar();
    Assertions.assertEquals(grammar.classifyGrammar(), ChomskyType.TYPE_3);
  }

  @Test
  void isRegularGrammar() {
    Grammar grammar = Grammar.createBaseGrammar();
    Assertions.assertEquals(grammar.classifyGrammar(), ChomskyType.TYPE_3);
  }

  @Test
  void isContextFreeGrammar() {
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
    Assertions.assertEquals(grammar.classifyGrammar(), ChomskyType.TYPE_2);
  }

  @Test
  void isContextSensitiveGrammar() {
    Grammar grammar = new Grammar(
            new String[] {"S", "A", "B", "C", "E"},
            new String[] {"a", "b"},
            new Production[] {
                    new Production("S", "ABb"),
                    new Production("AB", "Ca"),
                    new Production("C", "bE"),
                    new Production("E", "b"),
            },
            "S"
    );
    Assertions.assertEquals(grammar.classifyGrammar(), ChomskyType.TYPE_1);
  }

  @Test
  void convertToChomskyNormalForm() {
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
    Assertions.assertArrayEquals(grammar.getNonTerminalVariables(), new String[]{"A", "B", "S", "C", "M", "N", "O"});
    Assertions.assertArrayEquals(grammar.getTerminalVariables(), new String[]{"a", "b"});
    Assertions.assertEquals(grammar.getStartingCharacter(), "S");
    Assertions.assertArrayEquals(Arrays.toString(grammar.getProductions()).toCharArray(), Arrays.toString(new Production[] {
            new Production("S", "MB"),
            new Production("S", "AC"),
            new Production("S", "a"),
            new Production("S", "NC"),
            new Production("S", "AS"),
            new Production("S", "BC"),
            new Production("S", "b"),
            new Production("S", "OS"),
            new Production("B", "b"),
            new Production("B", "OS"),
            new Production("C", "BA"),
            new Production("A", "a"),
            new Production("A", "NC"),
            new Production("A", "AS"),
            new Production("A", "BC"),
            new Production("A", "b"),
            new Production("A", "OS"),
            new Production("M", "a"),
            new Production("N", "AS"),
            new Production("O", "b")
            }).toCharArray());
  }

  @Test
  void testToString() {
    Grammar grammar = Grammar.createBaseGrammar();
    System.out.println(grammar);
    Assertions.assertEquals(grammar.toString(), """
    Grammar {
    \tVn (Non-terminal) = [S, I, J, K]
    \tVt (Terminal) = [a, b, c, e, n, f, m]
    \tP (Productions) = [S -> cI, I -> bJ, I -> fI, J -> nJ, J -> cS, I -> eK, K -> nK, I -> e, K -> m]
    \tS (Starting symbol) = S
    }""");
  }
}