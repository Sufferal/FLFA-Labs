package parser;

import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;

import java.util.List;

public class Parser {
  private final Lexer lexer;
  private List<Token> tokens;
  private int currentTokenIndex;
  private final StringBuilder AST = new StringBuilder();

  public Parser(Lexer lexer) {
    this.lexer = lexer;
  }

  public void printAST() {
    System.out.println(this.AST);
  }

  public void parse() {
    tokens = lexer.tokenize();
    currentTokenIndex = 0;

    // Start the parsing process
    parseProgram();

    // Check if there are any remaining tokens
    if (currentTokenIndex < tokens.size() - 1) {
      throw new IllegalArgumentException("Unexpected token at the end of the input");
    }
  }

  // Parser methods for each production rule
  private void parseProgram() {
    // program -> statement*
    while (currentTokenIndex < tokens.size()) {
      parseStatement();
    }
  }

  private void parseStatement() {
    // statement -> printStatement | ifStatement | whileStatement | assignmentStatement
    Token currentToken = getCurrentToken();

    switch (currentToken.getType()) {
      case PRINT -> parsePrintStatement();
      case IF -> parseIfStatement();
      case WHILE -> parseWhileStatement();
      case IDENTIFIER -> parseAssignmentStatement();
      default -> throw new IllegalArgumentException("Unexpected token: " + currentToken);
    }
  }

  private void parsePrintStatement() {
    // printStatement -> "print" expression ";"
    consumeToken(TokenType.PRINT);
    AST.append("PrintStatement { \n");
    AST.append("\tExpression { \n");
    parseExpression();
    AST.append("\t}\n");
    consumeToken(TokenType.SEMICOLON);
    consumeToken(TokenType.END_OF_INPUT);
    AST.append("}");
  }

  private void parseIfStatement() {
    // ifStatement -> "if" "(" expression ")" "{" program "}" ["else" "{" program "}"]
    AST.append("IfStatement { \n");
    consumeToken(TokenType.IF);
    consumeToken(TokenType.LEFT_PAREN);

    AST.append("\tExpression { \n");
    parseExpression();
    parseExpression();
    parseExpression();
    consumeToken(TokenType.RIGHT_PAREN);
    consumeToken(TokenType.LEFT_BRACE);
    AST.append("\t}\n");

    AST.append("\tProgram { \n");
    parseExpression();
    consumeToken(TokenType.WHITESPACE);
    parseExpression();
    consumeToken(TokenType.SEMICOLON);
    consumeToken(TokenType.RIGHT_BRACE);
    AST.append("\t}\n");
    AST.append("}\n");

    if (checkToken(TokenType.ELSE)) {
      AST.append("ElseStatement { \n");
      consumeToken(TokenType.ELSE);
      consumeToken(TokenType.LEFT_BRACE);
      AST.append("\tProgram { \n");
      parseExpression();
      consumeToken(TokenType.WHITESPACE);
      parseExpression();
      AST.append("\t}\n");
      consumeToken(TokenType.SEMICOLON);
      consumeToken(TokenType.RIGHT_BRACE);
      AST.append("}\n");
    }

    consumeToken(TokenType.END_OF_INPUT);
  }

  private void parseWhileStatement() {
    // whileStatement -> "while" "(" expression ")" "{" program "}"
    AST.append("WhileStatement { \n");

    consumeToken(TokenType.WHILE);
    consumeToken(TokenType.LEFT_PAREN);

    AST.append("\tExpression { \n");

    parseExpression();
    parseExpression();
    parseExpression();
    consumeToken(TokenType.RIGHT_PAREN);
    consumeToken(TokenType.LEFT_BRACE);

    AST.append("\t}\n");

    AST.append("\tProgram { \n");
    consumeToken(TokenType.PRINT);
    AST.append("\t\tPrintStatement { \n");
    AST.append("\t\t\tExpression { \n\t");
    parseExpression();
    AST.append("\t\t}\n");
    consumeToken(TokenType.SEMICOLON);

    parseExpression();
    parseExpression();
    consumeToken(TokenType.RIGHT_BRACE);

    AST.append("\t}\n");
    AST.append("}\n");

    consumeToken(TokenType.END_OF_INPUT);
  }

  private void parseAssignmentStatement() {
    // assignmentStatement -> identifier "=" expression ";"
    AST.append("AssignmentStatement { \n");
    AST.append("\tExpression { \n");

    // identifier
    parseExpression();
    consumeToken(TokenType.ASSIGNMENT);

    // expression
    parseExpression();

    consumeToken(TokenType.SEMICOLON);
    consumeToken(TokenType.END_OF_INPUT);

    AST.append("\t}\n");
    AST.append("}");
  }

  private void parseExpression() {
    // Skip any remaining whitespace tokens
    while (checkToken(TokenType.WHITESPACE)) {
      consumeToken();
    }

    // expression -> additiveExpression
    parseAdditiveExpression();
  }

  private void parseAdditiveExpression() {
    // additiveExpression -> multiplicativeExpression { ("+" | "-") multiplicativeExpression }
    parseMultiplicativeExpression();

    while (checkToken(TokenType.PLUS) || checkToken(TokenType.MINUS)) {
      if(getCurrentToken().getType() == TokenType.PLUS &&
         getNextToken().getType() == TokenType.PLUS) {
        AST.append("\t\tOperator { \n");
        AST.append("\t\t\tValue: ++").append("\n");
        AST.append("\t\t} \n");
        consumeToken(TokenType.PLUS);
        consumeToken(TokenType.PLUS);
        break;
      }
      parseMultiplicativeExpression();
    }
  }

  private void parseMultiplicativeExpression() {
    // multiplicativeExpression -> primaryExpression { ("*" | "/") primaryExpression }
    parsePrimaryExpression();

    while (checkToken(TokenType.MULTIPLY) || checkToken(TokenType.DIVIDE)) {
      consumeToken();
      parsePrimaryExpression();
    }
  }

  private void parsePrimaryExpression() {
    // primaryExpression -> identifier | literal
    Token currentToken = getCurrentToken();

    if (currentToken.getType() == TokenType.IDENTIFIER) {
      AST.append("\t\tIdentifier { \n");
      consumeToken(TokenType.IDENTIFIER);
      AST.append("\t\t\tValue: ").append(currentToken.getLexeme()).append("\n");
      AST.append("\t\t} \n");
    }
    else if(currentToken.getType() == TokenType.LESS_THAN) {
      AST.append("\t\tOperator { \n");
      AST.append("\t\t\tValue: ").append(currentToken.getLexeme()).append("\n");
      consumeToken(TokenType.LESS_THAN);
      AST.append("\t\t} \n");
    }
    else if(currentToken.getType() == TokenType.EQUALS) {
      AST.append("\t\tOperator { \n");
      AST.append("\t\t\tValue: ").append(currentToken.getLexeme()).append("\n");
      consumeToken(TokenType.EQUALS);
      AST.append("\t\t} \n");
    }
    else if(currentToken.getType() == TokenType.RETURN) {
      AST.append("\t\tKeyword { \n");
      AST.append("\t\t\tValue: ").append(currentToken.getLexeme()).append("\n");
      consumeToken(TokenType.RETURN);
      AST.append("\t\t} \n");
    }
    else if(currentToken.getType() == TokenType.BOOLEAN) {
      AST.append("\t\tBoolean { \n");
      AST.append("\t\t\tValue: ").append(currentToken.getLexeme()).append("\n");
      consumeToken(TokenType.BOOLEAN);
      AST.append("\t\t} \n");
    }
    else if(currentToken.getType() == TokenType.SEMICOLON) {
      consumeToken(TokenType.SEMICOLON);
    }
    else {
      parseLiteral();
    }
  }


  private void parseLiteral() {
    // literal -> integerLiteral | charLiteral | stringLiteral | floatLiteral | doubleLiteral | booleanLiteral
    Token currentToken = getCurrentToken();

    switch (currentToken.getType()) {
      case INT -> {
        AST.append("\t\tInteger { \n");
        AST.append("\t\t\tValue: ").append(currentToken.getLexeme()).append("\n");
        AST.append("\t\t} \n");
        consumeToken(TokenType.INT);
      }
      case STRING -> consumeToken(TokenType.STRING);
      case FLOAT -> consumeToken(TokenType.FLOAT);
      case DOUBLE -> consumeToken(TokenType.DOUBLE);
      case BOOLEAN -> consumeToken(TokenType.BOOLEAN);
      default -> throw new IllegalArgumentException("Unexpected token: " + currentToken);
    }
  }

  // Helper methods
  private Token getCurrentToken() {
    if (currentTokenIndex >= tokens.size()) {
      throw new IllegalArgumentException("No more tokens to parse");
    }
    return tokens.get(currentTokenIndex);
  }

  private Token getNextToken() {
    if (currentTokenIndex + 1 >= tokens.size()) {
      throw new IllegalArgumentException("No more tokens to parse");
    }
    return tokens.get(currentTokenIndex + 1);
  }

  private boolean checkToken(TokenType expectedTokenType) {
    if (currentTokenIndex >= tokens.size()) {
      return false;
    }
    Token currentToken = getCurrentToken();
    return currentToken.getType() == expectedTokenType;
  }

  private void consumeToken(TokenType expectedTokenType) {
    if (!checkToken(expectedTokenType)) {
      throw new IllegalArgumentException("Expected token of type " + expectedTokenType
              + ", but found " + getCurrentToken().getType());
    }
    currentTokenIndex++;
  }

  private void consumeToken() {
    currentTokenIndex++;
  }
}
