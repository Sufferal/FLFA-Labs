package automaton;

public class Transition
{
    private final char currentState;
    private final char nextState;
    private final char transitionLabel;

    public Transition(char currentState, char nextState, char transitionLabel) {
        this.currentState = currentState;
        this.nextState = nextState;
        this.transitionLabel = transitionLabel;
    }

    public char getCurrentState() {
        return currentState;
    }

    public char getNextState() {
        return nextState;
    }

    public char getTransitionLabel() {
        return transitionLabel;
    }

    @Override
    public String toString() {
        return Character.toString(this.currentState);
    }
}
