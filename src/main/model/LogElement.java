package model;

public interface LogElement {
    
    // EFFECTS: returns the multiplicative effect this operation has on the determinant
    public Fraction getDeterminantEffect();
    
    // EFFECTS: returns the string representation of this operation
    public String getString();

}
