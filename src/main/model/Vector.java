package model;

import java.util.Arrays;

// Represents a vector composed of Fraction components
public class Vector {
    private Fraction[] components; // the compononents of the vector
    private int size;             // the dimension of the vector

    // REQUIRES: components != null, components.length > 0;
    // EFFECTS: constructs a Vector with the given fraction components
    public Vector(Fraction[] components) {
        this.size = components.length;
        this.components = Arrays.copyOf(components, components.length);
    }

    // REQUIRES: V2 != null, this.size() == v2.size()
    // EFFECTS: returns a new Vector that is the sum of this and v2
    public Vector add(Vector v2) {
        int length = size();
        Fraction[] resultComponents = new Fraction[length];

        for (int i = 0; i < length; i++) {
            Fraction f1 = this.getComponent(i);
            Fraction f2 = v2.getComponent(i);
            resultComponents[i] = f1.add(f2);
        }
        return new Vector(resultComponents);
    }

    // REQUIRES: V2 != null, this.size() == v2.size()
    // EFFECTS: returns a new Vector that is the difference of this and v2
    public Vector subtract(Vector v2) {
        int length = size();
        Fraction[] resultComponents = new Fraction[length];

        for (int i = 0; i < length; i++) {
            Fraction f1 = this.getComponent(i);
            Fraction f2 = v2.getComponent(i);
            resultComponents[i] = f1.subtract(f2);
        }
        return new Vector(resultComponents);
    }

    // REQUIRES: scalar != null
    // EFFECTS: returns a new Vector where each component is multiplied by scalar
    public Vector scalarMultiply(Fraction scalar) {
        int length = size();
        Fraction[] resultComponents = new Fraction[length];

        for (int i = 0; i < length; i++) {
            Fraction f1 = this.getComponent(i);
            resultComponents[i] = f1.multiply(scalar);
        }
        return new Vector(resultComponents);
    }

    // REQUIRES: v2 != null, this.size() == v2.size()
    // EFFECTS: returns the dot product of this vector and v2
    public Fraction dot(Vector v2) {
        int length = size();
        Fraction sum = new Fraction(0, 1);
        for (int i = 0; i < length; i++) {
            Fraction f1 = this.getComponent(i);
            Fraction f2 = v2.getComponent(i);
            Fraction product = f1.multiply(f2);
            sum = sum.add(product);
        }
        return sum;
    }

    // REQUIRES: v2 != null, size() == 3, v2.size() == 3
    // EFFECTS: returns a new Vector that is the cross product of this and v2
    public Vector cross(Vector v2) {
        Fraction a1 = this.getComponent(0);
        Fraction a2 = this.getComponent(1);
        Fraction a3 = this.getComponent(2);

        Fraction b1 = v2.getComponent(0);
        Fraction b2 = v2.getComponent(1);
        Fraction b3 = v2.getComponent(2);

        Fraction resX = a2.multiply(b3).subtract(a3.multiply(b2));
        Fraction resY = a3.multiply(b1).subtract(a1.multiply(b3));
        Fraction resZ = a1.multiply(b2).subtract(a2.multiply(b1));

        Fraction[] resultComponents = { resX, resY, resZ };
        return new Vector(resultComponents);
    }

    // EFFECTS: returns the magnitude of this vector as as a double
    public double getMagnitude() {
        int length = size();
        Fraction squaredSum = new Fraction(0, 0);
        for (int i = 0; i < length; i++) {
            Fraction f1 = this.getComponent(i);
            squaredSum = squaredSum.add(f1.multiply(f1));
        }
        return Math.sqrt(squaredSum.toDecimal());
    }

    // REQUIRES: getMagnitude() != 0
    // EFFECTS: returns a new Vector with a magnitude of 1 in the same direction as this
    public Vector normalize() {
        double mag = getMagnitude();
        Fraction[] unitComponents = new Fraction[size()];
        for (int i = 0; i < this.size; i++) {
            double originalValue = this.components[i].toDecimal();
            double normalizedValue = originalValue / mag;
            unitComponents[i] = new Fraction(Math.round(normalizedValue * 1000000), 1000000);
        }
        return new Vector(unitComponents);
    }

    // REQUIRES: v2 != null, this.size() == v2.size()
    // EFFECTS: returns true if the dot product of this and v2 is 0
    public boolean isOrthogonal(Vector v2) {
        Fraction dotProduct = this.dot(v2);
        return dotProduct.getNumerator() == 0;
    }

    // EFFECTS: gets the number of components in the vector
    public int size() {
        return this.size;
    }

    // REQUIRES index >= 0, index < this.size()
    // EFFECTS: returns the component at the specified index
    public Fraction getComponent(int index) {
        return this.components[index];
    }

    // REQUIRES: v2 != null
    // EFFECTS: returns true if this vector and the other have the same size and identical components
    public boolean equals(Vector v2) {
        int length = size();
        if (length != v2.size()) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (!this.getComponent(i).equals(v2.getComponent(i))) {
                return false;
            }
        }
        return true;
    }

    // EFFECTS: returns a string representation of the fraction example "[1/2, 2/3, -1/1]"
    public String toString() {
        String strVector = "[";
        int length = size();
        for (int i = 0; i < length; i++) {
            strVector = strVector + this.getComponent(i).toString() + ", ";
        }
        strVector = strVector.substring(0, strVector.length() - 2) + ']';
        return strVector;
    }

}
