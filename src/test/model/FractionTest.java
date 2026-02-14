package model;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FractionTest {
    private Fraction oneHalf;
    private Fraction twoThirds;
    private Fraction negFiveSixths;

    @BeforeEach
    void runBefore() {
        oneHalf = new Fraction(1, 2);
        twoThirds = new Fraction(2, 3);
        negFiveSixths = new Fraction(5, -6);
    }

    @Test
    void testConstructorSimplification() {
        Fraction f = new Fraction(10, 20);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());

        Fraction f2 = new Fraction(0, 5);
        assertEquals(0, f2.getNumerator());
        assertEquals(1, f2.getDenominator());
    }

    @Test
    void testConstructorSignManagement() {
        Fraction negBottom = new Fraction(1, -2);
        assertEquals(-1, negBottom.getNumerator());
        assertEquals(2, negBottom.getDenominator());

        Fraction doubleNeg = new Fraction(-1, -2);
        assertEquals(1, doubleNeg.getNumerator());
        assertEquals(2, doubleNeg.getDenominator());
    }

    @Test
    void testAdd() {
        Fraction res = oneHalf.add(twoThirds);
        assertEquals(7, res.getNumerator());
        assertEquals(6, res.getDenominator());

        Fraction res2 = oneHalf.add(negFiveSixths);
        assertEquals(-1, res2.getNumerator());
        assertEquals(3, res2.getDenominator());
    }

    @Test
    void testSubtract() {
        Fraction res = twoThirds.subtract(oneHalf);
        assertEquals(1, res.getNumerator());
        assertEquals(6, res.getDenominator());

        Fraction res2 = negFiveSixths.subtract(oneHalf);
        assertEquals(-4, res2.getNumerator());
        assertEquals(3, res2.getDenominator());
    }

    @Test
    void testMultiply() {
        Fraction res = oneHalf.multiply(twoThirds);
        assertEquals(1, res.getNumerator());
        assertEquals(3, res.getDenominator());

        Fraction res2 = twoThirds.multiply(negFiveSixths);
        assertEquals(-5, res2.getNumerator());
        assertEquals(9, res2.getDenominator());
    }

    @Test
    void testDivide() {
        Fraction res = oneHalf.divide(twoThirds);
        assertEquals(3, res.getNumerator());
        assertEquals(4, res.getDenominator());
    }

    @Test
    void testToDecimal() {
        assertEquals(0.5, oneHalf.toDecimal(), 0.000001);
        assertEquals(-0.833333, negFiveSixths.toDecimal(), 0.000001);
    }

    @Test
    void testToString() {
        assertEquals("1/2", oneHalf.toString());
        assertEquals("-5/6", negFiveSixths.toString());
        assertEquals("0/1", new Fraction(0, 10).toString());
    }

    @Test
    void testEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 4);
        Fraction f3 = new Fraction(1, 3);

        assertTrue(f1.equals(f2));
        assertFalse(f1.equals(f3));
        assertTrue(f1.equals(f1));
    }

    @Test
    void testZeroNumerator() {
        Fraction zero = new Fraction(0, 5);
        assertEquals(0, zero.getNumerator());
        assertEquals(1, zero.getDenominator()); 
    }

    @Test
    void testLargeNumbers() {
        Fraction large1 = new Fraction(1000000, 1);
        Fraction large2 = new Fraction(1000000, 1);
        Fraction result = large1.multiply(large2);
        assertEquals(1000000000000L, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test
    void testAddToZero() {
        Fraction res = oneHalf.add(new Fraction(-1, 2));
        assertEquals(0, res.getNumerator());
        assertEquals(1, res.getDenominator());
    }

    @Test
    void testSimplifyNegativeOnBoth() {
        Fraction res = new Fraction(-10, -20);
        assertEquals(1, res.getNumerator());
        assertEquals(2, res.getDenominator());
    }

    @Test
    void testSubtractToNegative() {
        Fraction res = oneHalf.subtract(twoThirds);
        assertEquals(-1, res.getNumerator());
        assertEquals(6, res.getDenominator());
    }
}

