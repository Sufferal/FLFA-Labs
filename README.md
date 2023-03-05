# Formal languages and finite automata
This repository contains a Java implementation of Grammar and Finite Automaton. 

## Grammar Class
The Grammar class represents a formal language defined by a set of rules or production rules. 
It provides methods for generating strings based on the rules of the grammar, converting to a 
Finite Automaton and classify the grammar type.

You have the following public methods:

* generateWord()
* toFiniteAutomaton()
* classifyGrammar()

## Finite Automaton Class
The FiniteAutomaton class represents a finite automaton (FA), a simple idealized machine used to recognize patterns within input taken from some character set (or alphabet) C.
The FiniteAutomaton class provides methods for validating words, converting to a regular grammar, determining if it is deterministic, converting Nondeterministic Finite Automaton
(NFA) to a Deterministic Finite Automaton (DFA) and showing its graph.

You have the following public methods:

* isWordValid()
* convertToRegularGrammar()
* isDeterministic() 
* convertToDFA()
* showGraph()

