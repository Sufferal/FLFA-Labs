package grammar;

public class Production
{
    private final String leftSide;
    private final String rightSide;

    public Production(String leftSide, String rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    public String getLeftSide() {
        return this.leftSide;
    }
    public String getRightSide() {
        return this.rightSide;
    }

    @Override
    public String toString() {
        return this.leftSide + "->" + this.rightSide;
    }
}
