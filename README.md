# Formal Languages and Finite Automata
This is a Java implementation for working with grammars, finite automata, and lexers. 
It provides functionality for the following: 
* Generate words from a grammar
* Convert a grammar to a finite automaton
* Classify the grammar based on Chomsky hierarchy
* Convert a grammar to Chomsky Normal Form
* Check if a word is valid in a finite automaton
* Convert a finite automaton to regular grammar
* Check if an automaton is deterministic
* Convert a finite automaton to a DFA
* Represent an automaton graphically
* Tokenize input using a lexer
* Print tokens

## Grammar 
A set of rules that specify how to generate a language. 
It consists of a set of production rules that define how to generate strings in the language. 
In formal language theory, grammars are often classified according to the Chomsky hierarchy, which is a hierarchy of formal languages based on their generative power.

```
Grammar grammar = new Grammar(
    new String[]{"S", "I", "J", "K"},
    new String[]{"a", "b", "c", "e", "n", "f", "m"},
    new Production[]{
        new Production("S", "cI"),
        new Production("I", "bJ"),
        new Production("I", "fI"),
        new Production("J", "nJ"),
        new Production("J", "cS"),
        new Production("I", "eK"),
        new Production("K", "nK"),
        new Production("I", "e"),
        new Production("K", "m"),
    },
    "S"
);

grammar.generateWord();

grammar.toFiniteAutomaton();

grammar.classifyGrammar();

grammar.toChomskyNormalForm();
```

## Finite Automaton 
A mathematical model used to recognize patterns within strings of symbols. 
It consists of a set of states and a set of transitions between those states, which define how the automaton moves from one state to another based on the input symbols. 
Finite automata are often used in computer science for tasks such as lexical analysis and pattern recognition.

```
FiniteAutomaton FA = new FiniteAutomaton(
    new String[]{"q0", "q1", "q2", "q3", "q4"},
    new String[]{"a", "b"},
    new Transition[]{
        new Transition("q0", "a", "q1"),
        new Transition("q1", "b", "q1"),
        new Transition("q1", "b", "q2"),
        new Transition("q2", "b", "q3"),
        new Transition("q3", "a", "q1"),
        new Transition("q2", "a", "q4"),
    },
    "q0",
    new String[]{"q4"}
);

FA.convertToRegularGrammar();

FA.isDeterministic();

FA.convertToDFA();
```

## Lexer
A program that performs lexical analysis on input text. 
It reads in a stream of characters and produces a stream of tokens, which are the basic units of syntax for a programming language. 
A lexer typically uses a set of rules to recognize different types of tokens, such as keywords, identifiers, and operators.

```
String input_1 = "5+(6-2)*3/4";
Lexer lexer = new Lexer(input_1);
System.out.println("===== 1. For input: " + input_1 + " the tokens are: =====");
lexer.printTokens();
```
