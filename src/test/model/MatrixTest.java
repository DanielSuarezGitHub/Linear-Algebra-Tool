package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class MatrixTest {
    private Matrix m2x2;
    private Matrix m3x3;
    private Fraction f1;

    @BeforeEach
    void runBefore() {
        f1 = new Fraction(1, 1);

        m2x2 = new Matrix(2, 2);
        m3x3 = new Matrix(3, 3);
    }

    @Test
    void testConstructorAndGetSet() {
        assertEquals(true, m2x2.isSquare());
        m2x2.setElement(0, 1, new Fraction(5, 1));
        assertEquals(5, m2x2.getElement(0, 1).getNumerator());
        assertEquals(0, m2x2.getElement(0, 0).getNumerator());
    }

    @Test
    void testAdd() {
        Matrix a = new Matrix(2, 2);
        a.setElement(0, 0, f1);
        Matrix b = new Matrix(2, 2);
        b.setElement(0, 0, f1);

        Matrix sum = a.add(b);
        assertEquals(2, sum.getElement(0, 0).getNumerator());
        assertEquals(0, sum.getElement(1, 1).getNumerator());
    }

    @Test
    void testMultiply() {
        Matrix a = new Matrix(2, 2);
        a.setElement(0, 0, new Fraction(1, 1));
        a.setElement(0, 1, new Fraction(2, 1));
        a.setElement(1, 0, new Fraction(3, 1));
        a.setElement(1, 1, new Fraction(4, 1));

        Matrix identity = new Matrix(2, 2);
        identity.setElement(0, 0, f1);
        identity.setElement(1, 1, f1);

        Matrix result = a.multiply(identity);
        assertEquals(1, result.getElement(0, 0).getNumerator());
        assertEquals(4, result.getElement(1, 1).getNumerator());
        assertEquals(1, result.getElement(0, 0).getNumerator());
        assertEquals(4, result.getElement(1, 1).getNumerator());

        result = a.multiply(a);

        assertEquals(7, result.getElement(0, 0).getNumerator());
        assertEquals(10, result.getElement(0, 1).getNumerator());
        assertEquals(15, result.getElement(1, 0).getNumerator());
        assertEquals(22, result.getElement(1, 1).getNumerator());

    }

    @Test
    void testMultiplyVector() {
        Matrix a = new Matrix(2, 2);
        a.setElement(0, 0, new Fraction(2, 1));
        a.setElement(1, 1, new Fraction(3, 1));

        Fraction[] arrV = { new Fraction(1, 1), new Fraction(1, 1) };
        Vector v = new Vector(arrV);

        Vector result = a.multiplyVector(v);

        assertEquals(2, result.size());
        assertEquals(2, result.getComponent(0).getNumerator());
        assertEquals(1, result.getComponent(0).getDenominator());
        assertEquals(3, result.getComponent(1).getNumerator());
        assertEquals(1, result.getComponent(1).getDenominator());
    }

    @Test
    void testTranspose() {
        Matrix a = new Matrix(2, 3);
        a.setElement(0, 2, new Fraction(7, 1));

        Matrix at = a.transpose();
        assertEquals(7, at.getElement(2, 0).getNumerator());
    }

    @Test
    void testCalculaterref() {
        Matrix m = new Matrix(2, 2);
        m.setElement(0, 0, new Fraction(2, 1));
        m.setElement(0, 1, new Fraction(4, 1));
        m.setElement(1, 1, f1);

        m.calculaterref();

        assertEquals(1, m.getElement(0, 0).getNumerator());
        assertEquals(0, m.getElement(0, 1).getNumerator());
        assertEquals(1, m.getElement(1, 1).getNumerator());

        List<LogElement> log = m.getLog();
        assertFalse(log.isEmpty());
        assertTrue(log.get(0).getString().contains("R"));
    }

    @Test
    void testGetDeterminant() {
        Matrix m = new Matrix(2, 2);
        m.setElement(0, 0, new Fraction(1, 1));
        m.setElement(0, 1, new Fraction(2, 1));
        m.setElement(1, 0, new Fraction(3, 1));
        m.setElement(1, 1, new Fraction(4, 1));

        assertEquals(-2, m.getDeterminant().getNumerator());

        Matrix singular = new Matrix(2, 2);
        singular.setElement(0, 0, f1);
        singular.setElement(0, 1, f1);
        singular.setElement(1, 0, f1);
        singular.setElement(1, 1, f1);
        assertEquals(0, singular.getDeterminant().getNumerator());
    }

    @Test
    void testSolveUnique() {
        Matrix identity = new Matrix(2, 2);
        identity.setElement(0, 0, f1);
        identity.setElement(1, 1, f1);

        Fraction[] arrB = { new Fraction(5, 1), new Fraction(10, 1) };
        Vector b = new Vector(arrB);

        Vector x = identity.solve(b);
        assertNotNull(x);
        assertEquals(5, x.getComponent(0).getNumerator());
        assertEquals(10, x.getComponent(1).getNumerator());
        assertTrue(identity.isConsistent(b));
    }

    @Test
    void testInconsistentSystem() {
        Matrix zero = new Matrix(1, 1);
        Fraction[] arrB = { new Fraction(5, 1) };
        Vector b = new Vector(arrB);

        assertNull(zero.solve(b));
        assertFalse(zero.isConsistent(b));
    }

    @Test
    void testSolveNonSquare() {
        Matrix m = new Matrix(2, 3);
        m.setElement(0, 0, f1);
        m.setElement(0, 1, f1);
        m.setElement(0, 2, f1);
        m.setElement(1, 1, f1);
        m.setElement(1, 2, f1);

        Vector b = new Vector(new Fraction[] { new Fraction(0, 1),
                new Fraction(1, 1) });

        assertNull(m.solve(b));
    }

    @Test
    void testSolveInfiniteSolutions() {
        Matrix m = new Matrix(2, 2);
        m.setElement(0, 0, f1);
        m.setElement(0, 1, f1);
        m.setElement(1, 0, new Fraction(2, 1));
        m.setElement(1, 1, new Fraction(2, 1));

        Vector b = new Vector(new Fraction[] { new Fraction(2, 1),
                new Fraction(4, 1) });

        assertTrue(m.isConsistent(b));
        assertNull(m.solve(b));
    }

    @Test
    void testrrefWithSwap() {
        Matrix m = new Matrix(2, 2);
        m.setElement(0, 0, new Fraction(0, 1));
        m.setElement(0, 1, new Fraction(1, 1));
        m.setElement(1, 0, new Fraction(1, 1));
        m.setElement(1, 1, new Fraction(0, 1));

        m.calculaterref();

        assertEquals(1, m.getElement(0, 0).getNumerator());
        assertEquals(0, m.getElement(0, 1).getNumerator());

        assertTrue(m.getLogString().contains("R1 <-> R0"));
    }

    @Test
    void testrrefWithElimination() {

        Matrix m = new Matrix(2, 2);
        m.setElement(0, 0, new Fraction(1, 1));
        m.setElement(1, 0, new Fraction(1, 1));
        m.setElement(1, 1, new Fraction(1, 1));

        m.calculaterref();

        assertEquals(0, m.getElement(1, 0).getNumerator());
    }

    @Test
    void testrrefWithScaling() {
        Matrix m = new Matrix(2, 2);
        m.setElement(0, 0, new Fraction(2, 1));
        m.setElement(1, 1, new Fraction(1, 1));

        m.calculaterref();

        assertEquals(1, m.getElement(0, 0).getNumerator());

        assertTrue(m.getLogString().contains(" * R0 -> R0"));
    }

    @Test
    void testInverse() {
        Matrix m1 = new Matrix(2, 2);
        m1.setElement(0, 0, new Fraction(1, 1));
        m1.setElement(0, 1, new Fraction(2, 1));
        m1.setElement(1, 0, new Fraction(3, 1));
        m1.setElement(1, 1, new Fraction(4, 1));

        Matrix inv1 = m1.invert();

        assertTrue(new Fraction(-2, 1).equals(inv1.getElement(0, 0)));
        assertTrue(new Fraction(1, 1).equals(inv1.getElement(0, 1)));
        assertTrue(new Fraction(3, 2).equals(inv1.getElement(1, 0)));
        assertTrue(new Fraction(-1, 2).equals(inv1.getElement(1, 1)));

        Matrix identity = inv1.multiply(m1);

        assertEquals(1, identity.getElement(0, 0).getNumerator());
        assertEquals(0, identity.getElement(0, 1).getNumerator());
        assertEquals(1, identity.getElement(1, 1).getNumerator());
        assertEquals(0, identity.getElement(1, 0).getNumerator());

    }

    @Test
    void testRankAndConsistencyComplex() {
        Matrix m = new Matrix(2, 2);
        m.setElement(0, 0, f1);
        m.setElement(0, 1, f1);
        m.setElement(1, 0, f1);
        m.setElement(1, 1, f1);
        assertEquals(1, m.getRank());
        Vector inSpan = new Vector(new Fraction[] { f1.add(f1), f1.add(f1) });
        Vector notSpan = new Vector(new Fraction[] { f1.add(f1), f1 });
        assertTrue(m.isConsistent(inSpan));
        assertFalse(m.isConsistent(notSpan));
    }

    @Test
    void testGetTrace() {
        m2x2.setElement(0, 0, new Fraction(1, 1));
        m2x2.setElement(1, 1, new Fraction(4, 1));
        assertEquals(5, m2x2.getTrace().getNumerator());

        m3x3.setElement(0, 0, new Fraction(-10, 1));
        m3x3.setElement(1, 1, new Fraction(3, 1));
        m3x3.setElement(2, 2, new Fraction(7, 1));
        assertEquals(0, m3x3.getTrace().getNumerator());
    }

    @Test
    void testGetColumnBasisVectors() {
        m2x2.setElement(0, 0, f1);
        m2x2.setElement(0, 1, new Fraction(2, 1));
        m2x2.setElement(1, 0, new Fraction(2, 1));
        m2x2.setElement(1, 1, new Fraction(4, 1));

        Vector[] colBasis = m2x2.getColumnBasisVectors();

        assertNotNull(colBasis);
        assertEquals(1, colBasis.length);

        assertEquals(1, colBasis[0].getComponent(0).getNumerator());
        assertEquals(2, colBasis[0].getComponent(1).getNumerator());
    }

    @Test
    void testGetRowBasisVectors() {
        m2x2.setElement(0, 0, f1);
        m2x2.setElement(0, 1, new Fraction(3, 1));
        m2x2.setElement(1, 0, new Fraction(2, 1));
        m2x2.setElement(1, 1, new Fraction(6, 1));

        Vector[] rowBasis = m2x2.getRowBasisVectors();

        assertNotNull(rowBasis);
        assertEquals(1, rowBasis.length);

        assertEquals(1, rowBasis[0].getComponent(0).getNumerator());
        assertEquals(3, rowBasis[0].getComponent(1).getNumerator());
    }

    @Test
    void testBasisForIdentity() {
        m3x3.setElement(0, 0, f1);
        m3x3.setElement(1, 1, f1);
        m3x3.setElement(2, 2, f1);

        Vector[] colBasis = m3x3.getColumnBasisVectors();
        Vector[] rowBasis = m3x3.getRowBasisVectors();

        assertEquals(3, colBasis.length);
        assertEquals(3, rowBasis.length);

        assertEquals(1, colBasis[0].getComponent(0).getNumerator());
        assertEquals(0, colBasis[0].getComponent(1).getNumerator());
        assertEquals(0, colBasis[0].getComponent(2).getNumerator());
    }

    @Test
    void testSolveInconsistentContradiction() {
        Matrix m = new Matrix(2, 2);
        m.setElement(0, 0, f1);
        m.setElement(0, 1, f1);
        m.setElement(1, 0, f1);
        m.setElement(1, 1, f1);

        Vector b = new Vector(new Fraction[] {
                new Fraction(2, 1),
                new Fraction(3, 1) });

        assertNull(m.solve(b));
        assertFalse(m.isConsistent(b));
    }

    @Test
    void testSolveTallInconsistent() {

        Matrix m = new Matrix(3, 2);
        m.setElement(0, 0, f1);
        m.setElement(1, 1, f1);
        m.setElement(2, 0, f1);
        m.setElement(2, 1, f1);

        Vector b = new Vector(new Fraction[] {
                new Fraction(1, 1),
                new Fraction(1, 1),
                new Fraction(5, 1) });

        assertNull(m.solve(b));
    }

    @Test
    public void testGetLogStringWithAddRowMultiple() {
        Matrix m = new Matrix(2, 2);
        m.setElement(0, 0, new Fraction(1, 1));
        m.setElement(0, 1, new Fraction(0, 1));

        m.setElement(1, 0, new Fraction(2, 1));
        m.setElement(1, 1, new Fraction(1, 1));
        m.calculaterref();
        assertTrue(m.getLogString().contains("+"));
        assertEquals(1, m.getElement(0, 0).getNumerator());
        assertEquals(0, m.getElement(1, 0).getNumerator());
    }

    @Test
    public void testIsSquare() {
        Matrix squareM = new Matrix(2, 2);
        assertTrue(squareM.isSquare());

        Matrix rectM = new Matrix(2, 3);
        assertFalse(rectM.isSquare());
    }
}
