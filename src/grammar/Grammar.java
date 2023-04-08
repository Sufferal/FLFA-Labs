package grammar;

import automaton.FiniteAutomaton;
import automaton.Transition;

import java.util.*;

public class Grammar {
    private final String[] nonTerminalVariables;
    private final String[] terminalVariables;
    private       Production[] productions;
    private final String startingCharacter;

    public Grammar(String[] nonTerminalVariables, String[] terminalVariables,
                   Production[] productions, String startingCharacter) {
        this.nonTerminalVariables = nonTerminalVariables;
        this.terminalVariables = terminalVariables;
        this.productions = productions;
        this.startingCharacter = startingCharacter;
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
        int nonTerminalLength = this.nonTerminalVariables[0].length();

        for (int i = 0; i <= rightSide.length() - nonTerminalLength; i++) {
            String currentSymbol = rightSide.substring(i, i + nonTerminalLength);
            if (isNonTerminal(currentSymbol)) {
                result.append(generateWord(currentSymbol));
            } else {
                result.append(currentSymbol.charAt(0));
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
            String transitionLabel = String.valueOf(production.getRightSide().charAt(0));

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

    public void convertToChomskyNormalForm() {
        System.out.println("\n===== Initial grammar =====");
        System.out.println(this);
        removeEpsilonProductions();
        System.out.println("\n===== Removed epsilon productions =====");
        System.out.println(this);
        removeUnitProductions();
        System.out.println("\n===== Removed unit productions =====");
        System.out.println(this);
        removeNonproductiveSymbols();
        System.out.println("\n===== Removed nonproductive symbols =====");
        System.out.println(this);
        removeInaccessibleSymbols();
        System.out.println("\n===== Removed inaccessible symbols =====");
        System.out.println(this);
    }

    private void removeEpsilonProductions() {
        // For now this works only for grammars with one nullable non-terminal
        String nullableNonTerminal = "";
        for (Production production : productions) {
            if (production.getRightSide().equals("ε")) {
                nullableNonTerminal = production.getLeftSide();
            }
        }

        // If there are no nullable non-terminals, we don't need to do anything
        if (nullableNonTerminal.equals("")) {
            System.out.println("No nullable non-terminals");
            return;
        }

        // Replace all nullable non-terminals with ε
        List<Production> newProductions = new ArrayList<>();
        for (Production production : productions) {
            if (production.getRightSide().equals("ε")) {
                continue;
            }

            if (production.getRightSide().contains(nullableNonTerminal)) {
                String newRightSide = production.getRightSide().replace(nullableNonTerminal, "");
                newProductions.add(production);
                newProductions.add(new Production(production.getLeftSide(), newRightSide));
                continue;
            }

            newProductions.add(production);
        }

        this.productions = new Production[newProductions.size()];
        newProductions.toArray(productions);
    }

    private void removeUnitProductions() {
        // Find all unit productions
        List<Production> unitProductions = new ArrayList<>();
        for (Production production : productions) {
            if (production.isUnitProduction()) {
                unitProductions.add(production);
            }
        }

        List<Production> newProductions = new ArrayList<>();
        // My variant: S -> A, A -> B
        for (Production unitProduction : unitProductions) {
            for (Production production : productions) {
                // When A -> Y, where Y is any symbol
                // Add to new productions: S -> Y
                if (production.getLeftSide().equals(unitProduction.getRightSide())) {
                    // If Y is a terminal symbol
                    // i.e. A -> Y -> x
                    if (production.isUnitProduction()) {
                        for (Production p : productions) {
                            if (p.getLeftSide().equals(production.getRightSide())) {
                                newProductions.add(new Production(unitProduction.getLeftSide(), p.getRightSide()));
                            }
                        }

                        continue;
                    }

                    newProductions.add(new Production(unitProduction.getLeftSide(), production.getRightSide()));
                    continue;
                }

                // Skip unit productions
                if (production.isUnitProduction()) {
                    continue;
                }

                // Doesn't allow duplicates
                if(!newProductions.contains(production)) {
                    newProductions.add(production);
                }
            }
        }

        productions = new Production[newProductions.size()];
        newProductions.toArray(productions);
    }


    private void removeNonproductiveSymbols() {
        // Initialize all non-terminal variables as non-productive
        Map<String, Boolean> productiveSymbols = new HashMap<>();
        for (String nonTerminal : nonTerminalVariables) {
            productiveSymbols.put(nonTerminal, false);
        }

        // Find all productive symbols
        for (Production production : productions) {
            if (production.getRightSide().length() == 1
                    && Character.isLowerCase(production.getRightSide().charAt(0))) {
                productiveSymbols.put(production.getLeftSide(), true);
            }
        }

        // Find all symbols that can be derived from productive symbols
        for (Production production : productions) {
            String rightSide = production.getRightSide();
            boolean isProductive = true;

            // Check if all non-terminals on the right side are already productive
            for (int i = 0; i < rightSide.length(); i++) {
                char symbol = rightSide.charAt(i);
                if (Character.isUpperCase(symbol) && !productiveSymbols.get(String.valueOf(symbol))) {
                    isProductive = false;
                    break;
                }
            }

            // If all symbols on the right side are productive, then the left side is also productive
            if (isProductive) {
                productiveSymbols.put(production.getLeftSide(), true);
            }
        }

        // Update production rules
        List<Production> newProductions = new ArrayList<>();
        for (Production production : productions) {
            if(productiveSymbols.get(production.getLeftSide())) {
                newProductions.add(production);
            }
        }
        productions = new Production[newProductions.size()];
        newProductions.toArray(productions);
    }

    public void removeInaccessibleSymbols() {
        // Step 1: Find all reachable symbols
        Set<String> reachableSymbols = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();

        // Start with the starting symbol
        stack.push(this.startingCharacter);

        while (!stack.isEmpty()) {
            String symbol = stack.pop();
            // Add the symbol to the set of reachable symbols if it hasn't been added already
            if (!reachableSymbols.contains(symbol)) {
                reachableSymbols.add(symbol);
                // Add all symbols on the right-hand side of each production for this symbol to the stack
                List<Production> productionsList = getProductionsForSymbol(symbol);
                for (Production production : productionsList) {
                    for (char c : production.getRightSide().toCharArray()) {
                        if (Character.isUpperCase(c)) {
                            stack.push(String.valueOf(c));
                        }
                    }
                }
            }
        }

        // Step 2: Remove all productions that contain unreachable symbols
        List<Production> newProductionsList = new ArrayList<>();
        for (Production production : this.productions) {
            // Add the production to the new list if its left-hand side symbol is reachable
            if (reachableSymbols.contains(production.getLeftSide())) {
                String rightSide = production.getRightSide();
                StringBuilder newRightSide = new StringBuilder();
                // Construct the new right-hand side by removing all unreachable symbols
                for (char c : rightSide.toCharArray()) {
                    if (reachableSymbols.contains(String.valueOf(c)) || Arrays.asList(this.terminalVariables).contains(String.valueOf(c))) {
                        newRightSide.append(c);
                    }
                }
                // Add the new production to the new list
                newProductionsList.add(new Production(production.getLeftSide(), newRightSide.toString()));
            }
        }

        // Update the list of productions in the Grammar object
        this.productions = newProductionsList.toArray(new Production[0]);
    }

    // Returns a list of all productions in the grammar that have the given symbol on the left-hand side.
    private List<Production> getProductionsForSymbol(String symbol) {
        List<Production> productionsList = new ArrayList<>();
        for (Production production : this.productions) {
            if (production.getLeftSide().equals(symbol)) {
                productionsList.add(production);
            }
        }
        return productionsList;
    }


    @Override
    public String toString() {
        return "Grammar {" + "\n" +
                "\tVn (Non-terminal) = " + Arrays.toString(this.nonTerminalVariables) + "\n" +
                "\tVt (Terminal) = " + Arrays.toString(this.terminalVariables) + "\n" +
                "\tP (Productions) = " + Arrays.toString(this.productions) + "\n" +
                "\tS (Starting symbol) = " + this.startingCharacter + "\n" +
                '}';
    }
}
