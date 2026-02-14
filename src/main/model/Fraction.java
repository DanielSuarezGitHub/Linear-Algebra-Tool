package model;

import java.lang.Math;

// Represents a rational number (fraction) with a numerator and denominator.
public class Fraction {
    private long numerator;
    private long denominator;

    // REQUIRES: denominator != 0
    // EFFECTS: constructs a fraction with given numerator and denominator;
    // Fraction is reduced to simplest form and denominator is made positive.
    public Fraction(long numerator, long denominator) {
        this.numerator = numerator;         // the numerator of the fraction
        this.denominator = denominator;    // the denominator of the fraction
        this.simplify();
    }

    // REQUIRES: f2 != null
    // EFFECTS: returns a new Fraction that is the sum of this and f2
    public Fraction add(Fraction f2) {
        long commonDivisor = gcd(this.getDenominator(), f2.getDenominator());
        long commonDenominator = (this.getDenominator() * f2.getDenominator()) / commonDivisor;
        long partSumThis = this.getNumerator() * (commonDenominator / this.getDenominator());
        long partSumF2 = f2.getNumerator() * (commonDenominator / f2.getDenominator());
        long fullSum = partSumF2 + partSumThis;
        return new Fraction(fullSum, commonDenominator);
    }

    // REQUIRES: f2 != null
    // EFFECTS: returns a new Fraction that is the difference of this and f2
    public Fraction subtract(Fraction f2) {
        long commonDivisor = gcd(this.getDenominator(), f2.getDenominator());
        long commonDenominator = (this.getDenominator() * f2.getDenominator()) / commonDivisor;
        long partDiffThis = this.getNumerator() * (commonDenominator / this.getDenominator());
        long partDiffF2 = f2.getNumerator() * (commonDenominator / f2.getDenominator());
        long fullDiff = partDiffThis - partDiffF2;
        return new Fraction(fullDiff, commonDenominator);
    }

    // REQUIRES: f2 != null
    // EFFECTS: returns a new Fraction that is the product of this and f2
    public Fraction multiply(Fraction f2) {
        long newNumerator = this.getNumerator() * f2.getNumerator();
        long newDenominator = this.getDenominator() * f2.getDenominator();
        return new Fraction(newNumerator, newDenominator);
    }

    // REQUIRES: f2 != null, f2.getNumerator() != 0
    // EFFECTS: returns a new Fraction that is the quotient of this divided by f2
    public Fraction divide(Fraction f2) {
        long newNumerator = this.getNumerator() * f2.getDenominator();
        long newDenominator = this.getDenominator() * f2.getNumerator();
        return new Fraction(newNumerator, newDenominator);
    }

    // MODIFIES: this
    // EFFECTS: simplifies the fraction (e.g. 2/4 -> 1/2) and ensures denominator is
    // positive
    public void simplify() {
        if (this.numerator == 0) {
            this.denominator = 1;
            return;
        }
        long divisor = gcd(Math.abs(this.numerator), Math.abs(this.denominator));
        this.numerator /= divisor;
        this.denominator /= divisor;

        if (this.denominator < 0) {
            this.numerator = -this.numerator;
            this.denominator = -this.denominator;
        }
    }

    // REQUIRES: a >= 0, b >= 0
    // EFFECTS: returns the greatest common divisor of a and b
    private long gcd(long a, long b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    // EFFECTS: returns the numerator of this fraction
    public long getNumerator() {
        return this.numerator;
    }

    // EFFECTS: returns the denominator of this fraction
    public long getDenominator() {
        return this.denominator;
    }

    // EFFECTS: returns the decimal value of this fraction (numerator / denominator)
    public double toDecimal() {
        double numerator = Double.valueOf(this.numerator);
        double denominator = Double.valueOf(this.denominator);
        return numerator / denominator;
    }

    // REQUIRES: f2 != null
    // EFFECTS: returns true if this fraction and f2 represent the same rational value
    public boolean equals(Fraction f2) {
        if (this.getNumerator() == f2.getNumerator() && this.getDenominator() == f2.getDenominator()) {
            return true;
        }
        return false;
    }

    // EFFECTS: returns a string representation of the fraction example "1/2"
    public String toString() {
        return String.valueOf(this.getNumerator()) + '/' + String.valueOf(this.getDenominator());
    }
}