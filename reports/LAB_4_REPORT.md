# Topic: Chomsky Normal Form

### Course: Formal Languages & Finite Automata
### Author: Botnari Ciprian
### Variant: 6

## Theory
**Chomsky Normal Form** is a way of representing a context-free grammar in a specific form, where all productions are either of the form A → BC or A → a, where A, B, and C are non-terminal symbols and a is a terminal symbol. This form simplifies the analysis and manipulation of grammars in certain algorithms and can be useful in parsing natural language.

## Objectives:

1. Learn about Chomsky Normal Form (CNF)
2. Get familiar with the approaches of normalizing a grammar
3. Implement a method for normalizing an input grammar by the rules of CNF 

    I. The implementation needs to be encapsulated in a method with an appropriate signature (also ideally in an appropriate class/type)

    II. The implemented functionality needs executed and tested
    
    III. **(BONUS)** Implement unit tests that validate the functionality of the project
    
    IV. **(BONUS)** Make the aforementioned function to accept any grammar

## Implementation 

### removeEpsilonProductions
* Eliminates all ε-productions from the grammar. An ε-production is a production of the form A → ε, where A is a non-terminal symbol and ε is the empty string. 
* The non-terminals that can be derived to the empty string are called nullable non-terminals.
* Their productions have to be updated accordingly 
```
private void removeEpsilonProductions() {
  // Find nullable non-terminals
  for (Production production : productions) {
      if (production.getRightSide().equals("ε")) {
          .........
      }
  
  List<Production> newProductions = new ArrayList<>();
  for (Production production : productions) {
        // Update the productions
        
        .......
        
        newProductions.add(production);
  }

  // Update the productions
  this.productions = new Production[newProductions.size()];
  newProductions.toArray(productions);
}  
```

### removeUnitProductions
* A unit production is a production of the form A → B, where A and B are non-terminal symbols. 
* Replace any unit production A → B with all the productions of the form A → X, where X is any non-terminal symbol that can be derived from B.
```
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
            
            .......
        }
    }

    productions = new Production[newProductions.size()];
    newProductions.toArray(productions);
  }  
```

### removeNonproductiveSymbols
* A non-terminal symbol is called non-productive if it cannot  reach a terminal symbol
* For example, D -> aDa is a non-productive production, since there is no end to the derivation.
```
private void removeNonproductiveSymbols() {
    // Initialize all non-terminal variables as non-productive
    Map<String, Boolean> productiveSymbols = new HashMap<>();
    for (String nonTerminal : nonTerminalVariables) {
        productiveSymbols.put(nonTerminal, false);
    }

    // Find all productive symbols
    for (Production production : productions) {
        .......
    }

    // Find all symbols that can be derived from productive symbols
    for (Production production : productions) {
        String rightSide = production.getRightSide();
        boolean isProductive = true;
        .......
    }

    // Update production rules
    productions = new Production[newProductions.size()];
    newProductions.toArray(productions);
 }
```

### removeInaccessibleSymbols
* A symbol is unreachable if it cannot be reached from the start symbol of the grammar.
* Start by finding all the symbols that can be derived from the start symbol, then remove any symbols that cannot be reached from those symbols.
```
 public void removeInaccessibleSymbols() {
   // Step 1: Find all reachable symbols
   Set<String> reachableSymbols = new HashSet<>();
   Deque<String> stack = new ArrayDeque<>();

   // Start with the starting symbol
   stack.push(this.startingCharacter);

   while (!stack.isEmpty()) {
       String symbol = stack.pop();
       // Add the symbol to the set of reachable symbols if it hasn't been added already
          
       .........
   }

   // Step 2: Remove all productions that contain unreachable symbols
   List<Production> newProductionsList = new ArrayList<>();
   for (Production production : this.productions) {
       // Add the production to the new list if its left-hand side symbol is reachable
            
       .........
   }

   // Update the list of productions in the Grammar object
   this.productions = newProductionsList.toArray(new Production[0]);
   this.nonTerminalVariables = reachableSymbols.toArray(new String[0]);
 }
```

### toChomskyNormalFormStep
* Replace any production that has more than two non-terminal symbols on the right-hand side. 
* A production of the form A → BCD can be replaced with two productions, A → BE and E → CD, where B and C are non-terminal symbols and D and E can be either terminal or non-terminal symbols.
```
 public void toChomskyNormalFormStep() {
    List<String> newNonTerminalVariables = new ArrayList<>(List.of(this.nonTerminalVariables));
    List<Production> newProductions = new ArrayList<>();
    Map<String, String> ProductionsMap = new HashMap<>();

    // With what new non-terminal variable should we start
    char newNonTerminalStart = 'M';

    for (Production production : productions) {
        // If the right side has more than 2 symbols

        // If the right side has 2 symbols and the first is a terminal and the second is a non-terminal
    }

    // Add the new productions to the list of productions
    for (Map.Entry<String, String> entry : ProductionsMap.entrySet()) {
        String oldProduction = entry.getKey();
        String newProduction = entry.getValue();
        newProductions.add(new Production(newProduction, oldProduction));
    }

    // Update the list of productions in the Grammar object
    this.productions = newProductions.toArray(new Production[0]);
    this.nonTerminalVariables = newNonTerminalVariables.toArray(new String[0]);
 }
```



## Results
The initial grammar is shown including non-terminals, terminals, productions and starting symbol. Subsequently, only the non-terminals and productions are shown, since only they change.

===== Initial grammar =====
* Vn (Non-terminal) = [S, A, B, C, E]
* Vt (Terminal) = [a, b]
* P (Productions) = [S -> aB, S -> AC, A -> a, A -> ASC, A -> BC, B -> b, B -> bS, C -> ε, C -> BA, E -> bB]
* S (Starting symbol) = S

===== Removed epsilon productions =====
* Vn (Non-terminal) = [S, A, B, C, E]
* P (Productions) = [S -> aB, S -> AC, S -> A, A -> a, A -> ASC, A -> AS, A -> BC, A -> B, B -> b, B -> bS, C -> BA, E -> bB]

===== Removed unit productions =====
* Vn (Non-terminal) = [S, A, B, C, E]
* P (Productions) = [S -> aB, S -> AC, S -> a, S -> ASC, S -> AS, S -> BC, S -> b, S -> bS, B -> b, B -> bS, C -> BA, E -> bB, A -> a, A -> ASC, A -> AS, A -> BC, A -> b, A -> bS]

===== Removed nonproductive symbols =====
* Vn (Non-terminal) = [S, A, B, C, E]
* P (Productions) = [S -> aB, S -> AC, S -> a, S -> ASC, S -> AS, S -> BC, S -> b, S -> bS, B -> b, B -> bS, C -> BA, E -> bB, A -> a, A -> ASC, A -> AS, A -> BC, A -> b, A -> bS]

===== Removed inaccessible symbols =====
* Vn (Non-terminal) = [A, B, S, C]
* P (Productions) = [S -> aB, S -> AC, S -> a, S -> ASC, S -> AS, S -> BC, S -> b, S -> bS, B -> b, B -> bS, C -> BA, A -> a, A -> ASC, A -> AS, A -> BC, A -> b, A -> bS]

===== Chomsky Normal Form =====
* Vn (Non-terminal) = [A, B, S, C, M, N, O]
* P (Productions) = [S -> MB, S -> AC, S -> a, S -> NC, S -> AS, S -> BC, S -> b, S -> OS, B -> b, B -> OS, C -> BA, A -> a, A -> NC, A -> AS, A -> BC, A -> b, A -> OS, M -> a, N -> AS, O -> b]

## Conclusions 
In conclusion, the laboratory work on Chomsky Normal Form has been a valuable learning experience for me. Through this work, I have achieved the following objectives:

Firstly, I have learned about its importance. I have learned that CNF is a specific form in which context-free grammars can be represented, simplifying the analysis and manipulation of grammars in certain algorithms and aiding in parsing natural language.

Secondly, I have become familiar with the different approaches for normalizing a context-free grammar to CNF. I have learned about the step-by-step process involved in converting a given grammar and the different techniques used at each stage.

Finally, I have implemented a method for normalizing a given grammar to CNF and demonstrated how it works. Through this exercise, I have gained practical experience in developing a method for CNF normalization that can be applied to any context-free grammar.

Overall, this laboratory work has been an excellent opportunity for me to gain a deeper understanding of Chomsky Normal Form and its significance in context-free grammars. It has equipped me with the necessary skills and knowledge to develop a method for CNF normalization, which will be useful in my future endeavors in programming and natural language processing.





















