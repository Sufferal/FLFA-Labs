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


    public  ChomskyType classifyGrammar() {
        // Check if the grammar is regular
        boolean isRegular = true;
        for (Production p : productions) {
            String rhs = p.getRightSide();
            if (rhs.length() == 1 && Character.isLowerCase(rhs.charAt(0))) {
                // Single terminal symbol
                continue;
            } else if (rhs.length() == 2) {
                // Two symbols
                char firstSymbol = rhs.charAt(0);
                char secondSymbol = rhs.charAt(1);
                if (Character.isUpperCase(firstSymbol) && Character.isLowerCase(secondSymbol)) {
                    // Left-linear
                    continue;
                } else if (Character.isLowerCase(firstSymbol) && Character.isUpperCase(secondSymbol)) {
                    // Right-linear
                    continue;
                }
            }
            // Not regular
            isRegular = false;
            break;
        }
        if (isRegular) {
            return ChomskyType.TYPE_3;
        }

        // Check if the grammar is context-free
        boolean isContextFree = true;
        for (Production p : this.productions) {
            if (p.getLeftSide().length() > 1 || !Character.isUpperCase(p.getLeftSide().charAt(0))) {
                isContextFree = false;
                break;
            }
        }
        if (isContextFree) {
            return ChomskyType.TYPE_2;
        }

        // Check if the grammar is context-sensitive
        boolean isContextSensitive = true;
        // Check that every production satisfies the Type 1 grammar conditions
        // Check that every production satisfies the Type 1 grammar conditions
        for (Production p : productions) {
            String leftSide = p.getLeftSide();
            String rightSide = p.getRightSide();

            if (leftSide.length() > rightSide.length()) {
                // The length of the right-hand side must be greater than or equal to the length of the left-hand side
                isContextSensitive = false;
                break;
            }

            if (rightSide.length() == 0 && !leftSide.equals(Character.toString(startingCharacter))) {
                // If ε is produced, it can only be produced from the start symbol S
                isContextSensitive = false;
                break;
            }

            // All productions must be of the form Aα → βBγ, where A and B are non-terminal symbols, and α and γ are
            // strings of non-terminal and/or terminal symbols (with α and γ not both ε)
            if (leftSide.length() == 1 && isNonTerminal(leftSide.charAt(0))) {
                // If the left side is a single non-terminal symbol
                if (!isNonTerminal(rightSide.charAt(0))) {
                    isContextSensitive = false;
                    break;
                }
            } else {
                // If the left side is a string of non-terminal and/or terminal symbols
                if (rightSide.length() == 0 || !isNonTerminal(leftSide.charAt(leftSide.length() - 1)) || !isNonTerminal(rightSide.charAt(0))) {
                    isContextSensitive = false;
                    break;
                }
            }
        }

        if (isContextSensitive) {
            return ChomskyType.TYPE_1;
        }

        // If we get here the grammar is unrestricted
        return ChomskyType.TYPE_0;
    }
}
