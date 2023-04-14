package automaton;

import grammar.Grammar;
import grammar.Production;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public String[] getStates() { return this.states; }
    public String[] getAlphabet() { return this.alphabet; }
    public Transition[] getTransitions() { return this.transitions; }
    public String getInitialState() { return this.initialState; }
    public String[] getFinalStates() { return this.finalStates; }

    public boolean isWordValid(String str) {
        Set<String> currentStates = epsilonClosure(this.initialState);

        for (char c : str.toCharArray()) {
            Set<String> nextStates = new HashSet<>();

            for (String currentState : currentStates) {
                for (Transition t : this.transitions) {
                    if (Objects.equals(t.getCurrentState(), currentState) &&
                            Objects.equals(t.getTransitionLabel(), String.valueOf(c))) {
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
                if (Objects.equals(t.getCurrentState(), currentState)
                        && Objects.equals(t.getTransitionLabel(), "e")) {
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
                if (Objects.equals(t.getCurrentState(), state) && !Objects.equals(t.getTransitionLabel(), "ε")) {
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

    public FiniteAutomaton convertToDFA() {
        Set<Set<String>> powerSet = getPowerSet(states);
        Map<Set<String>, Map<String, Set<String>>> dfaTransitions = new HashMap<>();
        Set<String> dfaFinalStates = new HashSet<>();
        String dfaInitialState = initialState;

        // Compute DFA transitions and final states
        for (Set<String> stateSet : powerSet) {
            Map<String, Set<String>> transitions = new HashMap<>();
            for (String symbol : alphabet) {
                Set<String> nextStates = new HashSet<>();
                for (String state : stateSet) {
                    for (Transition transition : transitionsFrom(state, symbol)) {
                        nextStates.add(transition.getNextState());
                    }
                }
                if (!nextStates.isEmpty()) {
                    transitions.put(symbol, nextStates);
                }
            }
            dfaTransitions.put(stateSet, transitions);
            if (stateSet.containsAll(Arrays.asList(finalStates))) {
                dfaFinalStates.add(stateSet.toString());
            }
        }

        return new FiniteAutomaton(
                powerSet.stream().map(Set::toString).toArray(String[]::new),
                alphabet,
                dfaTransitions.entrySet().stream()
                        .flatMap(e -> e.getValue().entrySet().stream()
                                .map(entry -> new Transition(e.getKey().toString(), entry.getKey(),
                                        entry.getValue().toString())))
                        .toArray(Transition[]::new),
                dfaInitialState,
                dfaFinalStates.toArray(String[]::new));
    }

    private Set<Set<String>> getPowerSet(String[] set) {
        Set<Set<String>> powerSet = new HashSet<>();
        int setSize = set.length;
        long powSetSize = (long) Math.pow(2, setSize);
        for (int counter = 0; counter < powSetSize; counter++) {
            Set<String> subset = new HashSet<>();
            for (int j = 0; j < setSize; j++) {
                if ((counter & (1 << j)) != 0) {
                    subset.add(set[j]);
                }
            }
            powerSet.add(subset);
        }
        return powerSet;
    }

    private List<Transition> transitionsFrom(String state, String symbol) {
        return Arrays.stream(transitions)
                .filter(t -> t.getCurrentState().equals(state) && t.getTransitionLabel().equals(symbol))
                .collect(Collectors.toList());
    }

    public String toDot() {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph finite_automaton {\n");
        dot.append("\trankdir=LR;\n");
        dot.append("\tsize=\"8,5\"\n");

        // add the states
        dot.append("\tnode [shape = circle];\n");
        for (String state : states) {
            dot.append("\t").append(state);
            if (finalStates != null && Arrays.asList(finalStates).contains(state)) {
                dot.append(" [shape=doublecircle]");
            }
            dot.append(";\n");
        }

        // add the transitions
        for (Transition transition : transitions) {
            dot.append("\t").append(transition.getCurrentState()).append(" -> ")
                    .append(transition.getNextState()).append(" [label=\"")
                    .append(transition.getTransitionLabel()).append("\"];\n");
        }

        // add the initial state
        dot.append("\tnode [shape = point]; qi\n");
        dot.append("\tnode [shape = circle]; ").append(initialState).append("\n");
        dot.append("\tedge [dir=none]; qi -> ").append(initialState).append("\n");

        dot.append("}\n");

        return dot.toString();
    }

    public void showGraph() {
        try {
            // generate the dot file
            String dot = this.toDot();

            // create a temporary file for the dot input
            File dotFile = File.createTempFile("finite_automaton", ".dot");
            BufferedWriter bw = new BufferedWriter(new FileWriter(dotFile));
            bw.write(dot);
            bw.close();

            // create a temporary file for the image output
            File imgFile = File.createTempFile("finite_automaton", ".png");

            // run the Graphviz command to generate the image
            ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", "-o", imgFile.getAbsolutePath(),
                    dotFile.getAbsolutePath());
            pb.redirectErrorStream(true);
            pb.start().waitFor();

            // open the image in the default image viewer
            Desktop.getDesktop().open(imgFile);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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