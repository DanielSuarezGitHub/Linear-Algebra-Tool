package model;

public class SwapRow implements LogElement {
    private Fraction DeterminantEffect = new Fraction(-1, 1);
    private String operationString;

    // EFFECTS: constructs a log element representing swapping r1 and r2
    public SwapRow(int r1, int r2) {
        operationString = ("R" + r1 + " <-> " + "R" + r2);
    }

    // EFFECTS: returns the multiplicative effect this operation has on the determinant
    public Fraction getDeterminantEffect() {
        return this.DeterminantEffect;
    }
    // EFFECTS: returns the formatted string description of the operation
    public String getString() {
        return this.operationString;
    }
}
