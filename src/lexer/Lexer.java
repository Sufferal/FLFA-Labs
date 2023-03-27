package lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
  private final String input;

  public Lexer(String input) {
    this.input = input;
  }

  public List<Token> tokenize() {
    List<Token> tokens = new ArrayList<>();
    int position = 0;

    while (position < this.input.length()) {
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
      case PRINT -> "print";
      case PRINTLN -> "println";
      case IF -> "if";
      case ELSE -> "else";
      case WHILE -> "while";
      case FOR -> "for";
      case RETURN -> "return";
      case INT -> "\\d+";
      case CHAR -> "'.'";
      case STRING -> "\".*?\"";
      case FLOAT -> "\\d+\\.\\d+";
      case DOUBLE -> "\\d+\\.\\d+";
      case VOID -> "void";
      case BOOLEAN -> "true|false";
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

  public void printTokens() {
    List<Token> tokens = this.tokenize();
    for (Token token : tokens) {
      System.out.print(token + ", ");
    }
  }
}
