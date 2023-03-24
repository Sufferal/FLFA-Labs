package lexer;

import automaton.FiniteAutomaton;
import grammar.Grammar;

public class Lexer {
  private final FiniteAutomaton automaton;
  private final Grammar grammar;
  private String input;
  private int index;

  public Lexer(FiniteAutomaton automaton, Grammar grammar, String input) {
    this.automaton = automaton;
    this.grammar = grammar;
    this.input = input;
    this.index = 0;
  }

  public Token nextToken() {
    // Use the finite automaton to determine the next token
    String tokenText = automaton.nextToken(input, index);
    TokenType tokenType = grammar.getTokenType(tokenText);

    // Create and return the token
    Token token = new Token(tokenType, tokenText);
    index += tokenText.length();
    return token;
  }
}
