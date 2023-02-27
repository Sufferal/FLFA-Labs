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

    public boolean isDeterministic() {
        // Create a map to keep track of transitions for each state and input symbol pair
        Map<String, Map<String, Set<String>>> transitionMap = new HashMap<>();

        // Iterate through all transitions
        for (Transition t : transitions) {
            // Get the source state and input symbol for this transition
            String sourceState = t.getCurrentState();
            String inputSymbol = String.valueOf(t.getTransitionLabel());

            // If we haven't seen this state and input symbol pair before, create a new entry in the map
            if (!transitionMap.containsKey(sourceState)) {
                transitionMap.put(sourceState, new HashMap<>());
            }
            if (!transitionMap.get(sourceState).containsKey(inputSymbol)) {
                transitionMap.get(sourceState).put(inputSymbol, new HashSet<>());
            }

            // Add the destination state to the set of possible transitions for this pair
            transitionMap.get(sourceState).get(inputSymbol).add(t.getNextState());
        }

        // Check if there is more than one possible transition for any state and input symbol pair
        for (String state : states) {
            for (String symbol : alphabet) {
                if (transitionMap.containsKey(state) && transitionMap.get(state).containsKey(symbol)
                        && transitionMap.get(state).get(symbol).size() > 1) {
                    // If there is more than one possible transition, FA is non-deterministic
                    return false;
                }
            }
        }

        // If we haven't found any non-deterministic pairs, FA is deterministic
        return true;
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