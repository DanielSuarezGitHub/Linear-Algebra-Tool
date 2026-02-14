package model;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VectorTest {
    private Vector vecZero2D;
    private Vector vecUnitX;
    private Vector vecUnitY;
    private Vector vecMessy2D;
    private Vector vecStandard3D;

    @BeforeEach
    void runBefore() {
        Fraction[] zeroArray = { new Fraction(0, 1),
                new Fraction(0, 1) };
        vecZero2D = new Vector(zeroArray);

        Fraction[] arrayX = { new Fraction(1, 1),
                new Fraction(0, 1) };
        Fraction[] arrayY = { new Fraction(0, 1),
                new Fraction(1, 1) };

        vecUnitX = new Vector(arrayX);
        vecUnitY = new Vector(arrayY);

        Fraction[] messyArray = { new Fraction(1, 2),
                new Fraction(-3, 4) };
        vecMessy2D = new Vector(messyArray);

        Fraction[] array3D = { new Fraction(1, 1),
                new Fraction(2, 1), new Fraction(3, 1) };
        vecStandard3D = new Vector(array3D);
    }

    @Test
    void testConstructorAndSize() {
        assertEquals(2, vecUnitX.size());
        assertEquals(3, vecStandard3D.size());
        assertEquals(1, vecUnitX.getComponent(0).getNumerator());
        assertEquals(0, vecUnitX.getComponent(1).getNumerator());
    }

    @Test
    void testAdd() {
        Vector result = vecUnitX.add(vecUnitY);
        assertEquals(1, result.getComponent(0).getNumerator());
        assertEquals(1, result.getComponent(1).getNumerator());

        Vector messyResult = vecMessy2D.add(vecUnitX);
        assertEquals(3, messyResult.getComponent(0).getNumerator());
        assertEquals(2, messyResult.getComponent(0).getDenominator());
        assertEquals(-3, messyResult.getComponent(1).getNumerator());
    }

    @Test
    void testSubtract() {
        Vector result = vecUnitX.subtract(vecUnitX);
        assertEquals(0, result.getComponent(0).getNumerator());
        assertEquals(0, result.getComponent(1).getNumerator());
    }

    @Test
    void testScalarMultiply() {
        Fraction scalar = new Fraction(2, 1);
        Vector result = vecMessy2D.scalarMultiply(scalar);

        assertEquals(1, result.getComponent(0).getNumerator());
        assertEquals(1, result.getComponent(0).getDenominator());
        assertEquals(-3, result.getComponent(1).getNumerator());
        assertEquals(2, result.getComponent(1).getDenominator());
    }

    @Test
    void testDotProduct() {
        assertEquals(0, vecUnitX.dot(vecUnitY).getNumerator());
        assertEquals(14, vecStandard3D.dot(vecStandard3D).getNumerator());
    }

    @Test
    void testIsOrthogonal() {
        assertTrue(vecUnitX.isOrthogonal(vecUnitY));
        assertFalse(vecUnitX.isOrthogonal(vecMessy2D));
        assertTrue(vecZero2D.isOrthogonal(vecMessy2D));
    }

    @Test
    void testMagnitude() {
        assertEquals(1.0, vecUnitX.getMagnitude(), 0.00001);
        assertEquals(Math.sqrt(14), vecStandard3D.getMagnitude(), 0.00001);
    }

    @Test
    void testNormalize() {
        Fraction[] fiveZero = { new Fraction(5, 1),
                new Fraction(0, 1) };
        Vector v = new Vector(fiveZero);
        Vector unit = v.normalize();

        assertEquals(1.0, unit.getMagnitude(), 0.00001);
        assertEquals(1.0, unit.getComponent(0).toDecimal(), 0.00001);
    }

    @Test
    void testEquals() {
        Fraction[] arrayX2 = { new Fraction(1, 1),
                new Fraction(0, 1) };
        Vector vecUnitX2 = new Vector(arrayX2);

        assertTrue(vecUnitX.equals(vecUnitX2));
        assertFalse(vecUnitX.equals(vecUnitY));
        assertFalse(vecUnitX.equals(vecStandard3D));
    }

    @Test
    void testToString() {
        assertEquals("[1/1, 0/1]", vecUnitX.toString());
        assertEquals("[1/2, -3/4]", vecMessy2D.toString());
    }

    @Test
    void testCrossProductStandardBasis() {
        Fraction[] arri = { new Fraction(1, 1), new Fraction(0, 1), new Fraction(0, 1) };
        Fraction[] arrj = { new Fraction(0, 1), new Fraction(1, 1), new Fraction(0, 1) };
        Fraction[] zeroArr3D = { new Fraction(0, 1), new Fraction(0, 1), new Fraction(0, 1) };
        Vector zeroVec3D = new Vector(zeroArr3D);
        Vector i = new Vector(arri);
        Vector j = new Vector(arrj);


        Vector k = i.cross(j);
        assertTrue(vecStandard3D.cross(vecStandard3D).equals(zeroVec3D));
        assertEquals(0, k.getComponent(0).getNumerator());
        assertEquals(0, k.getComponent(1).getNumerator());
        assertEquals(1, k.getComponent(2).getNumerator());
    }

}
