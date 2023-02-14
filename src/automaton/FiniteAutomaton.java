package automaton;

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
        return states;
    }

    public char[] getAlphabet() {
        return alphabet;
    }

    public char getInitialState() {
        return initialState;
    }

    public char[] getFinalStates() {
        return finalStates;
    }

    @Override
    public String toString() {
        return "FiniteAutomaton{" + "\n" +
                "states = " + Arrays.toString(states) + "\n" +
                "alphabet = " + Arrays.toString(alphabet) + "\n" +
                "transitions = " + Arrays.toString(transitions) + "\n" +
                "initialState = " + initialState + "\n" +
                "finalStates = " + Arrays.toString(finalStates) + "\n" +
                '}';
    }
}