package automaton;

import grammar.Grammar;
import grammar.Production;

import java.util.*;
import java.util.Arrays;

public class FiniteAutomaton {
    private final String[] states;
    private final String[] alphabet;
    private Transition[] transitions;
    private final String initialState;
    private final String[] finalStates;

    public FiniteAutomaton(String[] states, String[] alphabet,  Transition[] transitions,
                           String initialState, String[] finalStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.initialState = initialState;
        this.finalStates = finalStates;
    }

    public boolean isWordValid(String str) {
        Set<String> currentStates = epsilonClosure(this.initialState);

        for (char c : str.toCharArray()) {
            Set<String> nextStates = new HashSet<>();

            for (String currentState : currentStates) {
                for (Transition t : this.transitions) {
                    if (Objects.equals(t.getCurrentState(), currentState) &&
                        t.getTransitionLabel() == c) {
                        nextStates.addAll(epsilonClosure(t.getNextState()));
                    }
                }
            }

            if (nextStates.isEmpty()) {
                return false;
            }

            currentStates = nextStates;
        }

        for (String finalState : this.finalStates) {
            if (currentStates.contains(finalState)) {
                return true;
            }
        }

        return false;
    }

    public Set<String> epsilonClosure(String state) {
        Set<String> closure = new HashSet<>();
        closure.add(state);

        Stack<String> stack = new Stack<>();
        stack.push(state);

        while (!stack.isEmpty()) {
            String currentState = stack.pop();
            for (Transition t : transitions) {
                if (Objects.equals(t.getCurrentState(), currentState) && t.getTransitionLabel() == 'e') {
                    String nextState = t.getNextState();
                    if (!closure.contains(nextState)) {
                        closure.add(nextState);
                        stack.push(nextState);
                    }
                }
            }
        }

        return closure;
    }



    public Grammar convertToRegularGrammar() {
        String[] nonTerminalVariables = this.states;
        String[] terminalVariables = this.alphabet;
        String startingCharacter = this.initialState;

        ArrayList<Production> productions = new ArrayList<>();

        for (String state : this.states) {
            for (Transition t : this.transitions) {
                if (Objects.equals(t.getCurrentState(), state) && t.getTransitionLabel() != 'e') {
                    String production = state + "->" + t.getTransitionLabel() + t.getNextState();
                    productions.add(new Production(state, String.valueOf(t.getTransitionLabel()) + t.getNextState()));
                }
            }
        }

        for (String finalState : this.finalStates) {
            productions.add(new Production(finalState, "ε"));
        }

        Production[] productionsArray = new Production[productions.size()];
        productionsArray = productions.toArray(productionsArray);

        return new Grammar(nonTerminalVariables, terminalVariables, productionsArray, startingCharacter);
    }

    @Override
    public String toString() {
        return "FiniteAutomaton{" + "\n" +
                "\tQ (states) = " + Arrays.toString(states) + "\n" +
                "\tΣ (alphabet) = " + Arrays.toString(alphabet) + "\n" +
                "\tΔ (transitions) = " + Arrays.toString(transitions) + "\n" +
                "\tq0 (initial state) = " + initialState + "\n" +
                "\tF (final States) = " + Arrays.toString(finalStates) + "\n" +
                '}';
    }
}