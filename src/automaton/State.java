package automaton;

import java.util.HashMap;
import java.util.Map;

class State {
  private final char symbol;
  private final boolean isFinal;
  private final Map<Character, State> transitions;

  public State(char symbol, boolean isFinal) {
    this.symbol = symbol;
    this.isFinal = isFinal;
    this.transitions = new HashMap<>();
  }

  public char getSymbol() {
    return symbol;
  }

  public boolean isFinal() {
    return isFinal;
  }

  public void addTransition(Character symbol, State state) {
    transitions.put(symbol, state);
  }

  public State transition(Character symbol) {
    return transitions.get(symbol);
  }

  @Override
  public String toString() {
    return "State{" +
            "symbol=" + symbol +
            ", isFinal=" + isFinal +
            ", transitions=" + transitions +
            '}';
  }
}