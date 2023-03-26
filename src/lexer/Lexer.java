package lexer;

import automaton.FiniteAutomaton;
import automaton.Transition;
import grammar.Grammar;
import lexer.Token;
import lexer.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
  private final FiniteAutomaton automaton;
  private final Grammar grammar;
  private final String input;
  private int position;

  public Lexer(FiniteAutomaton automaton, Grammar grammar, String input) {
    this.automaton = automaton;
    this.grammar = grammar;
    this.input = input;
    this.position = 0;
  }

  public Lexer(String input) {
    this.automaton = null;
    this.grammar = null;
    this.input = input;
    this.position = 0;
  }

//  public List<Token> tokenize() {
//    List<Token> tokens = new ArrayList<>();
//
//    while (position < input.length()) {
//      int maxMatchLength = 0;
//      Transition matchingTransition = null;
//
//      for (Transition transition : automaton.getTransitions()) {
//        if (transition.getSource().equals(automaton.getCurrentState())
//                && transition.matches(input.charAt(position))) {
//          if (transition.getLabel().length() > maxMatchLength) {
//            maxMatchLength = transition.getLabel().length();
//            matchingTransition = transition;
//          }
//        }
//      }
//
//      if (matchingTransition != null) {
//        automaton.setCurrentState(matchingTransition.getTarget());
//        position += maxMatchLength;
//      } else {
//        throw new IllegalArgumentException("Invalid input at position " + position);
//      }
//
//      if (automaton.isFinalState()) {
//        TokenType tokenType = getTokenType(automaton.getCurrentState());
//        String lexeme = input.substring(position - automaton.getCurrentState().length(), position);
//        tokens.add(new Token(tokenType, lexeme));
//        automaton.reset();
//      }
//    }
//
//    tokens.add(new Token(TokenType.END_OF_INPUT, ""));
//    return tokens;
//  }
//
//  private TokenType getTokenType(String state) {
//    for (int i = 0; i < grammar.getNonTerminalVariables().length; i++) {
//      if (state.equals(grammar.getNonTerminalVariables()[i])) {
//        return TokenType.values()[i];
//      }
//    }
//
//    for (int i = 0; i < grammar.getTerminalVariables().length; i++) {
//      if (state.equals(grammar.getTerminalVariables()[i])) {
//        return TokenType.values()[grammar.getNonTerminalVariables().length + i];
//      }
//    }
//
//    throw new IllegalStateException("Invalid state: " + state);
//  }

  public List<Token> tokenize() {
    List<Token> tokens = new ArrayList<>();
    int position = 0;

    while (position < input.length()) {
      int maxMatchLength = 0;
      TokenType tokenType = null;

      for (TokenType type : TokenType.values()) {
        String pattern = getTokenPattern(type);

        if (pattern != null) {
          Pattern regex = Pattern.compile(pattern);
          Matcher matcher = regex.matcher(input.substring(position));

          if (matcher.lookingAt()) {
            int matchLength = matcher.end();

            if (matchLength > maxMatchLength) {
              maxMatchLength = matchLength;
              tokenType = type;
            }
          }
        }
      }

      if (maxMatchLength == 0) {
        throw new IllegalArgumentException("Invalid input at position " + position);
      }

      String lexeme = input.substring(position, position + maxMatchLength);
      tokens.add(new Token(tokenType, lexeme));
      position += maxMatchLength;
    }

    tokens.add(new Token(TokenType.END_OF_INPUT, ""));
    return tokens;
  }


  private String getTokenPattern(TokenType type) {
    return switch (type) {
      case IF -> "if";
      case ELSE -> "else";
      case WHILE -> "while";
      case FOR -> "for";
      case RETURN -> "return";
      case BOOLEAN -> "true|false";
      case INT -> "\\d+";
      case CHAR -> "'.'";
      case STRING -> "\".*?\"";
      case FLOAT -> "\\d+\\.\\d+";
      case DOUBLE -> "\\d+\\.\\d+";
      case VOID -> "void";
      case PLUS -> "\\+";
      case MINUS -> "-";
      case MULTIPLY -> "\\*";
      case DIVIDE -> "/";
      case MODULO -> "%";
      case ASSIGNMENT -> "=";
      case EQUALS -> "==";
      case NOT_EQUALS -> "!=";
      case LESS_THAN -> "<";
      case LESS_THAN_OR_EQUAL_TO -> "<=";
      case GREATER_THAN -> ">";
      case GREATER_THAN_OR_EQUAL_TO -> ">=";
      case AND -> "&&";
      case OR -> "\\|\\|";
      case NOT -> "!";
      case LEFT_PAREN -> "\\(";
      case RIGHT_PAREN -> "\\)";
      case LEFT_BRACE -> "\\{";
      case RIGHT_BRACE -> "\\}";
      case SEMICOLON -> ";";
      case COMMA -> ",";
      case IDENTIFIER -> "[a-zA-Z_][a-zA-Z_0-9]*";
      case WHITESPACE -> "\\s+";
      default -> null;
    };
  }


}
