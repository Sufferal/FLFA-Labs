package grammar;

import automaton.FiniteAutomaton;
import automaton.Transition;

import java.util.*;

public class Grammar {
    private final char[] nonTerminalVariables;
    private final char[] terminalVariables;
    private final Production[] productions;
    private final char startingCharacter;

    public Grammar(char[] nonTerminalVariables, char[] terminalVariables,
                   Production[] productions, char startingCharacter) {
        this.nonTerminalVariables = nonTerminalVariables;
        this.terminalVariables = terminalVariables;
        this.productions = productions;
        this.startingCharacter = startingCharacter;
    }

    public char[] getNonTerminalVariables() {
        return this.nonTerminalVariables;
    }
    public char[] getTerminalVariables() {
        return this.terminalVariables;
    }
    public Production[] getProductions() {
        return this.productions;
    }
    public char getStartingCharacter() {
        return this.startingCharacter;
    }

    public String generateWord() {
        return generateWord(this.startingCharacter);
    }

    private String generateWord(char symbol) {
        StringBuilder result = new StringBuilder();

        ArrayList<Production> possibleProductions = new ArrayList<>();
        for (Production production : this.productions) {
            if (production.getLeftSide().charAt(0) == symbol) {
                possibleProductions.add(production);
            }
        }

        Random random = new Random();
        int randomIndex = random.nextInt(possibleProductions.size());
        String rightSide = possibleProductions.get(randomIndex).getRightSide();

        for (int i = 0; i < rightSide.length(); i++) {
            char currentSymbol = rightSide.charAt(i);
            if (isNonTerminal(currentSymbol)) {
                result.append(generateWord(currentSymbol));
            } else {
                result.append(currentSymbol);
            }
        }

        return result.toString();
    }

    private boolean isNonTerminal(char symbol) {
        for (char nonTerminal : this.nonTerminalVariables) {
            if (nonTerminal == symbol) {
                return true;
            }
        }
        return false;
    }

    public FiniteAutomaton toFiniteAutomaton() {
        // Q - possible states
        char[] possibleStates = this.nonTerminalVariables;
        char[] newPossibleStates = new char[possibleStates.length + 1];
        System.arraycopy(possibleStates, 0, newPossibleStates, 0, possibleStates.length);
        newPossibleStates[newPossibleStates.length - 1] = 'X';
        possibleStates = newPossibleStates;

        // Σ - Alphabet
        char[] alphabet = terminalVariables;

        // Δ - Transitions
        Transition[] transitions = new Transition[this.productions.length];
        int i = 0;
        for (Production production : this.productions) {
            char currentState = production.getLeftSide().charAt(0);
            char nextState = production.getRightSide().length() > 1
                    ? production.getRightSide().charAt(1)
                    : 'X';
            char transitionLabel = production.getRightSide().charAt(0);

            transitions[i] = new Transition(currentState, nextState, transitionLabel);
            i++;
        }

        // q0 - Initial state
        char initialState = startingCharacter;

        // F - Final State
        char[] finalStates = new char[]{'X'};

        return new FiniteAutomaton(possibleStates, alphabet, transitions, initialState, finalStates);
    }
}
