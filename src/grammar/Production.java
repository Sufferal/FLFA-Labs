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

    public boolean isUnitProduction() {
        return this.rightSide.length() == 1 && this.leftSide.length() == 1
                && this.rightSide.matches("[A-Z]")
                && this.leftSide.matches("[A-Z]");
    }

    @Override
    public String toString() {
        return this.leftSide + " -> " + this.rightSide;
    }
}
