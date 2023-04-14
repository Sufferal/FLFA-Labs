import automaton.FiniteAutomaton;
import automaton.Transition;
import grammar.Grammar;
import grammar.Production;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FiniteAutomatonTest {

  @Test
  void isWordValid() {
    Grammar grammar = Grammar.createBaseGrammar();
    FiniteAutomaton FA = grammar.toFiniteAutomaton();

    Assertions.assertTrue(FA.isWordValid("ce"));
    Assertions.assertTrue(FA.isWordValid("cm"));
    Assertions.assertTrue(FA.isWordValid("cenm"));
    Assertions.assertTrue(FA.isWordValid("cfbccbnccenm"));
    Assertions.assertTrue(FA.isWordValid("cbccbccbnnccfem"));
    Assertions.assertFalse(FA.isWordValid("cc"));
    Assertions.assertFalse(FA.isWordValid("acenm"));
    Assertions.assertFalse(FA.isWordValid("cennnnnnnnnnn"));
    Assertions.assertTrue(FA.isWordValid("cennnnnnnnnnnm"));
    Assertions.assertFalse(FA.isWordValid("hello"));
    Assertions.assertFalse(FA.isWordValid("cet"));
  }

  @Test
  void convertToRegularGrammar() {
    FiniteAutomaton FA = FiniteAutomaton.createBaseFiniteAutomaton();

    Grammar FAtoGrammar = FA.convertToRegularGrammar();
    Assertions.assertArrayEquals(FAtoGrammar.getNonTerminalVariables(), new String[]{"q0", "q1", "q2", "q3", "q4"});
    Assertions.assertArrayEquals(FAtoGrammar.getTerminalVariables(), new String[]{"a", "b"});
    Assertions.assertEquals(FAtoGrammar.getStartingCharacter(), "q0");
    Assertions.assertArrayEquals(Arrays.toString(FAtoGrammar.getProductions()).toCharArray(), Arrays.toString(new Production[] {
            new Production("q0", "aq1"),
            new Production("q1", "bq1"),
            new Production("q1", "bq2"),
            new Production("q2", "bq3"),
            new Production("q2", "aq4"),
            new Production("q3", "aq1"),
            new Production("q4", "ε")
    }).toCharArray());
  }

  @Test
  void isDeterministic() {
    FiniteAutomaton FA = FiniteAutomaton.createBaseFiniteAutomaton();
    Assertions.assertFalse(FA.isDeterministic());
  }

  @Test
  void convertToDFA() {
    FiniteAutomaton FA = FiniteAutomaton.createBaseFiniteAutomaton();
    FiniteAutomaton DFA = FA.convertToDFA();
    Assertions.assertArrayEquals(DFA.getAlphabet(), new String[]{"a", "b"});
    Assertions.assertArrayEquals(DFA.getInitialState().toCharArray(), "q0".toCharArray());
  }

  @Test
  void testToString() {
    FiniteAutomaton FA = FiniteAutomaton.createBaseFiniteAutomaton();
    System.out.println(FA);
    Assertions.assertEquals(FA.toString(), """
      FiniteAutomaton{
      \tQ (states) = [q0, q1, q2, q3, q4]
      \tΣ (alphabet) = [a, b]
      \tΔ (transitions) = [q1, q1, q2, q3, q1, q4]
      \tq0 (initial state) = q0
      \tF (final States) = [q4]
      }""");
  }
}