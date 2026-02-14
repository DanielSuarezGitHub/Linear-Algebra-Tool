package model;

public class AddRowMultiple implements LogElement {
    private Fraction DeterminantEffect = new Fraction(1, 1);
    private String operationString;

    // EFFECTS: constructs a log element representing adding (scalar * sourceRow) to destRow
    public AddRowMultiple(int sourceRow, int destRow, Fraction scalar) {
        this.operationString = (scalar.toString() + " * R" + sourceRow + " + R" + destRow + " -> R" + destRow);
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
