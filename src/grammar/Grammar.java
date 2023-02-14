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
        return nonTerminalVariables;
    }
    public char[] getTerminalVariables() {
        return terminalVariables;
    }
    public Production[] getProductions() {
        return productions;
    }
    public char getStartingCharacter() {
        return startingCharacter;
    }

    public String generateWord() {
        return generateWord(startingCharacter);
    }

    private String generateWord(char symbol) {
        StringBuilder result = new StringBuilder();

        ArrayList<Production> possibleProductions = new ArrayList<>();
        for (Production production : productions) {
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
        for (char nonTerminal : nonTerminalVariables) {
            if (nonTerminal == symbol) {
                return true;
            }
        }
        return false;
    }

    public FiniteAutomaton toFiniteAutomaton() {
        HashMap<Character, Integer> stateMap = new HashMap<>();
        int stateIndex = 0;
        for (char nonTerminal : nonTerminalVariables) {
            stateMap.put(nonTerminal, stateIndex++);
        }
        for (char terminal : terminalVariables) {
            stateMap.put(terminal, stateIndex++);
        }

        int numStates = stateIndex;
        char[] possibleStates = new char[numStates];
        for (Map.Entry<Character, Integer> entry : stateMap.entrySet()) {
            possibleStates[entry.getValue()] = entry.getKey();
        }

        Transition[] transitions = new Transition[numStates * numStates];
        int transitionIndex = 0;
        for (Production production : productions) {
            char leftSide = production.getLeftSide().charAt(0);
            for (int i = 0; i < production.getRightSide().length(); i++) {
                char rightSideSymbol = production.getRightSide().charAt(i);
                transitions[transitionIndex++] = new Transition(
                        leftSide,
                        rightSideSymbol,
                        rightSideSymbol
                );
            }
        }

        char initialState = startingCharacter;
        char[] finalStates = new char[]{startingCharacter};

        return new FiniteAutomaton(possibleStates, terminalVariables, transitions, initialState, finalStates);
    }
}
