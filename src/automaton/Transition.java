package automaton;

public class Transition
{
    private String currentState;
    private String nextState;
    private String transitionLabel;

    public Transition(String currentState, String transitionLabel, String nextState) {
        this.currentState = currentState;
        this.nextState = nextState;
        this.transitionLabel = transitionLabel;
    }

    public String getCurrentState() {
        return this.currentState;
    }
    public String getNextState() {
        return this.nextState;
    }
    public String getTransitionLabel() {
        return this.transitionLabel;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
    public void setNextState(String nextState) {
        this.nextState = nextState;
    }
    public void setTransitionLabele(String transitionLabel) {
        this.transitionLabel = transitionLabel;
    }

    @Override
    public String toString() {
        return this.nextState;
    }
}
