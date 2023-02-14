package automaton;

import java.util.*;
import java.util.Arrays;

public class FiniteAutomaton {
    private final char[] states;
    private final char[] alphabet;
    private Transition[] transitions;
    private final char initialState;
    private final char[] finalStates;

    public FiniteAutomaton(char[] states, char[] alphabet,  Transition[] transitions,
                           char initialState, char[] finalStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.initialState = initialState;
        this.finalStates = finalStates;
    }

    public char[] getStates() {
        return this.states;
    }
    public char[] getAlphabet() {
        return this.alphabet;
    }
    public char getInitialState() {
        return this.initialState;
    }
    public char[] getFinalStates() {
        return this.finalStates;
    }

    public boolean isWordValid(String str) {
        Set<Character> currentStates = epsilonClosure(this.initialState);

        for (char c : str.toCharArray()) {
            Set<Character> nextStates = new HashSet<>();

            for (char currentState : currentStates) {
                for (Transition t : this.transitions) {
                    if (t.getCurrentState() == currentState &&
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

        for (char finalState : this.finalStates) {
            if (currentStates.contains(finalState)) {
                return true;
            }
        }

        return false;
    }

    public Set<Character> epsilonClosure(char state) {
        Set<Character> closure = new HashSet<>();
        closure.add(state);

        Stack<Character> stack = new Stack<>();
        stack.push(state);

        while (!stack.isEmpty()) {
            char currentState = stack.pop();
            for (Transition t : transitions) {
                if (t.getCurrentState() == currentState && t.getTransitionLabel() == 'e') {
                    char nextState = t.getNextState();
                    if (!closure.contains(nextState)) {
                        closure.add(nextState);
                        stack.push(nextState);
                    }
                }
            }
        }

        return closure;
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