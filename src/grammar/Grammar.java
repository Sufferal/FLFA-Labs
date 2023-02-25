package grammar;

import automaton.FiniteAutomaton;
import automaton.Transition;

import java.util.*;

public class Grammar {
    private final String[] nonTerminalVariables;
    private final String[] terminalVariables;
    private final Production[] productions;
    private final String startingCharacter;

    public Grammar(String[] nonTerminalVariables, String[] terminalVariables,
                   Production[] productions, String startingCharacter) {
        this.nonTerminalVariables = nonTerminalVariables;
        this.terminalVariables = terminalVariables;
        this.productions = productions;
        this.startingCharacter = startingCharacter;
    }

    public Production[] getProductions() {
        return this.productions;
    }

    public String generateWord() {
        return generateWord(this.startingCharacter);
    }

    private String generateWord(String symbol) {
        StringBuilder result = new StringBuilder();

        ArrayList<Production> possibleProductions = new ArrayList<>();
        for (Production production : this.productions) {
            if (Objects.equals(production.getLeftSide(), symbol)) {
                possibleProductions.add(production);
            }
        }

        Random random = new Random();
        int randomIndex = random.nextInt(possibleProductions.size());
        String rightSide = possibleProductions.get(randomIndex).getRightSide();

        for (int i = 0; i < rightSide.length(); i++) {
            String currentSymbol = String.valueOf(rightSide.charAt(i));
            if (isNonTerminal(currentSymbol)) {
                result.append(generateWord(currentSymbol));
            } else {
                result.append(currentSymbol);
            }
        }

        return result.toString();
    }

    private boolean isNonTerminal(String symbol) {
        for (String nonTerminal : this.nonTerminalVariables) {
            if (Objects.equals(nonTerminal, symbol)) {
                return true;
            }
        }
        return false;
    }

    public FiniteAutomaton toFiniteAutomaton() {
        // Q - possible states
        String[] possibleStates = Arrays.toString(this.nonTerminalVariables).split("");
        String[] newPossibleStates = new String[possibleStates.length + 1];
        System.arraycopy(possibleStates, 0, newPossibleStates, 0, possibleStates.length);
        newPossibleStates[newPossibleStates.length - 1] = "X";
        possibleStates = newPossibleStates;

        // Σ - Alphabet
        String[] alphabet = terminalVariables;

        // Δ - Transitions
        Transition[] transitions = new Transition[this.productions.length];
        int i = 0;
        for (Production production : this.productions) {
            char currentState = production.getLeftSide().charAt(0);
            char nextState = production.getRightSide().length() > 1
                    ? production.getRightSide().charAt(1)
                    : 'X';
            char transitionLabel = production.getRightSide().charAt(0);

            transitions[i] = new Transition(Character.toString(currentState), transitionLabel, Character.toString(nextState));
            i++;
        }

        // q0 - Initial state
        String initialState = String.valueOf(startingCharacter);

        // F - Final State
        String[] finalStates = new String[]{"X"};

        return new FiniteAutomaton(possibleStates, alphabet, transitions, initialState, finalStates);
    }


    public ChomskyType classifyGrammar() {
        if (isRegularGrammar()) {
            return ChomskyType.TYPE_3;
        } else if (isContextFreeGrammar()) {
            return ChomskyType.TYPE_2;
        } else if (isContextSensitiveGrammar()) {
            return ChomskyType.TYPE_1;
        } else {
            return ChomskyType.TYPE_0;
        }
    }

    public boolean isRegularGrammar() {
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
            return false;
        }

        return true;
    }

    public boolean isContextFreeGrammar() {
        // A grammar is context-free if every production rule has the form:
        //   A → α (where A is a single non-terminal symbol and α is a string of terminals and/or non-terminals)
        for (Production production : productions) {
            String leftSide = production.getLeftSide();
            String rightSide = production.getRightSide();
            if (leftSide.length() != 1 || !Character.isUpperCase(leftSide.charAt(0))) {
                return false; // Not a context-free grammar
            }
            for (int i = 0; i < rightSide.length(); i++) {
                char symbol = rightSide.charAt(i);
                if (!Character.isUpperCase(symbol) && !Character.isLowerCase(symbol)) {
                    return false; // Not a context-free grammar
                }
            }
        }
        return true; // All production rules satisfy the context-free grammar condition
    }

    public boolean isContextSensitiveGrammar() {
        // Check that every production satisfies the Type 1 grammar conditions
        for (Production p : productions) {
            String leftSide = p.getLeftSide();
            String rightSide = p.getRightSide();

            if (leftSide.length() > rightSide.length()) {
                // The length of the right-hand side must be greater than or equal to the length of the left-hand side
                return false;
            }
        }

        // If we get here, the grammar satisfies the Type 1 conditions
        return true;
    }


    @Override
    public String toString() {
        return "Grammar {" + "\n" +
                "\tVn (Non-terminal) = " + Arrays.toString(this.nonTerminalVariables) + "\n" +
                "\tVt (Terminal) = " + Arrays.toString(this.terminalVariables) + "\n" +
                "\tP (Productions) = " + Arrays.toString(this.productions) + "\n" +
                "\tS (Starting character) = " + this.startingCharacter + "\n" +
                '}';
    }
}
