package model;

import java.util.ArrayList;
import java.util.List;

// Represents a matrix of Fractions with defined rows and columns and a log of operations
public class Matrix {
    private Fraction[][] data;       // 2D array storing matrix elements
    private int rows;                // number of rows
    private int cols;                // number of columns
    private List<LogElement> log;    // history of row operations performed

    // REQUIRES: numRows > 0, numCols > 0
    // MODIFIES: this
    // EFFECTS: constructs a matrix with given dimensions; all entries initialized to 0
    public Matrix(int numRows, int numCols) {
        this.rows = numRows;
        this.cols = numCols;
        this.log = new ArrayList<LogElement>();
        this.data = new Fraction[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                setElement(i, j, new Fraction(0, 1));
            }
        }
    }

    // REQUIRES: row >= 0, row < getNumRows(), col >= 0, col < getNumCols(), f != null
    // MODIFIES: this
    // EFFECTS: sets the element at [row][col] to f
    public void setElement(int row, int col, Fraction f) {
        data[row][col] = f;
    }

    // REQUIRES: row >= 0, row < getNumRows(), col >= 0, col < getNumCols()
    // EFFECTS: returns the Fraction at [row][col]
    public Fraction getElement(int row, int col) {
        return data[row][col];
    }

    // REQUIRES: m2 != null, getNumRows() == m2.getNumRows(), getNumCols() == m2.getNumCols()
    // EFFECTS: returns a new Matrix that is the sum of this and m2
    public Matrix add(Matrix m2) {
        Matrix m3 = new Matrix(getNumRows(), getNumCols());
        for (int i = 0; i < getNumRows(); i++) {
            for (int j = 0; j < getNumCols(); j++) {
                Fraction sum = getElement(i, j).add(m2.getElement(i, j));
                m3.setElement(i, j, sum);
            }
        }
        return m3;
    }

    // REQUIRES: m2 != null, getNumCols() == m2.getNumRows()
    // EFFECTS: returns a new Matrix that is the product (this * m2)
    public Matrix multiply(Matrix m2) {
        Matrix m3 = new Matrix(getNumRows(), m2.getNumCols());
        for (int i = 0; i < m3.getNumRows(); i++) {
            Vector aik = this.getRowVector(i);
            for (int j = 0; j < m3.getNumCols(); j++) {
                Vector bkj = m2.getColumnVector(j);
                Fraction product = aik.dot(bkj);
                m3.setElement(i, j, product);
            }
        }
        return m3;
    }

    // REQUIRES: v != null, getNumCols() == v.size()
    // EFFECTS: returns a new Vector that is the result of the transformation (this * v)
    public Vector multiplyVector(Vector v) {
        Fraction[] resultComponents = new Fraction[this.rows];
        for (int i = 0; i < this.rows; i++) {
            Vector rowVec = this.getRowVector(i);
            resultComponents[i] = rowVec.dot(v);
        }
        return new Vector(resultComponents);
    }

    // EFFECTS: returns a new Matrix that is the transpose (A^T)
    public Matrix transpose() {
        Matrix m2 = new Matrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Fraction current = getElement(i, j);
                m2.setElement(j, i, current);
            }
        }
        return m2;
    }

    // MODIFIES: this
    // EFFECTS: transforms matrix into Reduced Row Echelon Form
    // records operations in log
    public void calculaterref() {
        Fraction one = new Fraction(1, 1);

        int currentRow = 0;

        for (int j = 0; j < cols && currentRow < rows; j++) {
            int pivotRow = findPivotRow(j, currentRow);
            Fraction pivotValue = getElement(pivotRow, j);

            if (pivotValue.getNumerator() == 0) {
                continue;
            }
            if (pivotRow != currentRow) {
                swapRows(pivotRow, currentRow);
            }

            if (!pivotValue.equals(one)) {
                Fraction scalar = one.divide(pivotValue);
                scaleRow(currentRow, scalar);
            }
            elimanateColumn(j, currentRow);
            currentRow++;
        }
    }

    // REQUIRES: b != null, b.size() == getNumRows()
    // EFFECTS: returns the unique solution vector x for Ax=b
    // returns null if the system is inconsistent or has infinite solutions
    public Vector solve(Vector b) {
        Matrix augmented = augment(b);
        augmented.calculaterref();
        for (int i = 0; i < augmented.getNumRows(); i++) {
            if (augmented.isRowContradictory(i)) {
                return null;
            }
        }

        if (this.getRank() < this.cols) {
            return null;
        }

        Fraction[] solution = new Fraction[getNumCols()];
        for (int i = 0; i < getNumCols(); i++) {
            solution[i] = augmented.getElement(i, getNumCols());
        }
        return new Vector(solution);
    }

    // REQUIRES: this matrix is augmented and in RREF, i >= 0, i < getNumRows()
    // EFFECTS: returns true if row i represents a contradiction example [0 0 ... 0 | non-zero] 
    private boolean isRowContradictory(int i) {
        Fraction b = this.data[i][getNumCols() - 1];
        boolean coefficientsAreZero = true;
        for (int j = 0; j < getNumCols() - 1; j++) {
            if (data[i][j].getNumerator() != 0) {
                coefficientsAreZero = false;
                break;
            }
        }
        if (coefficientsAreZero && b.getNumerator() != 0) {
            return true;
        }
        return false;
    }

    // REQUIRES: this.isSquare() == true
    // EFFECTS: returns the determinant of this matrix
    public Fraction getDeterminant() {
        Matrix copy = this.copy();
        copy.calculaterref();
        Fraction diagProduct = new Fraction(1, 1);
        for (int i = 0; i < rows; i++) {
            diagProduct = diagProduct.multiply(copy.getElement(i, i));
        }
        return diagProduct.divide(copy.getOverallDeterminantEffect());
    }

    // REQUIRES: this.isSquare() == true, this.getDeterminant().getNumerator() != 0
    // EFFECTS: returns a new Matrix that is the inverse (A^-1)
    public Matrix invert() {
        Matrix augmented = this.copy();
        augmented = augmentIdentity(augmented);
        augmented.calculaterref();
        Matrix inverse = new Matrix(getNumRows(), getNumCols());
        for (int i = 0; i < rows; i++) {
            for (int j = getNumCols(); j < 2 * getNumCols(); j++) {
                inverse.setElement(i, (j - getNumCols()), augmented.getElement(i, j));
            }
        }
        return inverse;
    }

    // REQUIRES: r1 >= 0, r1 < getNumRows(), r2 >= 0, r2 < getNumRows()
    // MODIFIES: this
    // EFFECTS: swaps row r1 and r2; adds operation to log
    private void swapRows(int r1, int r2) {
        Fraction[] row1 = data[r1];
        Fraction[] row2 = data[r2];
        data[r2] = row1;
        data[r1] = row2;
        log.add(new SwapRow(r1, r2));
    }

    // REQUIRES: row >= 0, row < getNumRows(), scalar != null
    // MODIFIES: this
    // EFFECTS: multiplies row by scalar; adds operation to log
    private void scaleRow(int row, Fraction scalar) {
        for (int i = 0; i < cols; i++) {
            data[row][i] = data[row][i].multiply(scalar);
        }
        log.add(new ScaleRow(row, scalar));
    }

    // REQUIRES: sourceRow >= 0, sourceRow < getNumRows(), destRow >= 0, destRow < getNumRows(), scalar != null
    // MODIFIES: this
    // EFFECTS: adds (scalar * sourceRow) to destRow; adds operation to log
    private void addRowMultiple(int sourceRow, int destRow, Fraction scalar) {
        for (int i = 0; i < cols; i++) {
            Fraction termToAdd = data[sourceRow][i].multiply(scalar);
            data[destRow][i] = data[destRow][i].add(termToAdd);
        }
        log.add(new AddRowMultiple(sourceRow, destRow, scalar));
    }

    // EFFECTS: returns the rank of the matrix
    public int getRank() {
        Matrix temp = this.copy();
        temp.calculaterref();
        int rank = 0;
        for (int i = 0; i < temp.getNumRows(); i++) {
            if (!temp.isRowAllZeros(i)) {
                rank++;
            }
        }
        return rank;
    }

    // REQUIRES: rowIndex >= 0, rowIndex < getNumRows()
    // EFFECTS: returns true if every element in the specified row is zero
    private boolean isRowAllZeros(int rowIndex) {
        for (int j = 0; j < getNumCols(); j++) {
            if (data[rowIndex][j].getNumerator() != 0) {
                return false;
            }
        }
        return true;
    }

    // REQUIRES: b != null, b.size() == getNumRows()
    // EFFECTS: returns true if the system Ax = b has at least one solution
    public boolean isConsistent(Vector b) {
        int rankA = this.getRank();

        Matrix augmented = this.augment(b);
        int rankAugmented = augmented.getRank();

        return rankA == rankAugmented;
    }

    // EFFECTS: returns true if number of rows equals number of columns
    public boolean isSquare() {
     return cols == rows;
    }

    // EFFECTS: returns the history (list) of row operations performed during RREF
    public List<LogElement> getLog() {
        return this.log;
    }

    // REQUIRES: rowIndex >= 0, rowIndex < getNumRows()
    // EFFECTS: returns the row at rowIndex as a Vector
    private Vector getRowVector(int rowIndex) {
        return new Vector(data[rowIndex]);
    }

    // REQUIRES: colIndex >= 0, colIndex < getNumCols()
    // EFFECTS: returns the column at colIndex as a Vector
    private Vector getColumnVector(int colIndex) {
        Fraction[] colData = new Fraction[rows];
        for (int i = 0; i < rows; i++) {
            colData[i] = data[i][colIndex];
        }
        return new Vector(colData);
    }
    // EFFECTS: returns number of rows  in this Matrix.
    public int getNumRows() {
        return rows;
    }

    // EFFECTS: returns number of columns in this Matrix.
    public int getNumCols() {
        return cols;
    }

    // REQUIRES: col >= 0, col < getNumCols(), startRow >= 0, startRow < getNumRows()
    // EFFECTS: returns index of row with largest absolute value in column col, searching from startRow downwards
    private int findPivotRow(int col, int startRow) {
        int maxRow = startRow;
        double maxVal = Math.abs(data[startRow][col].toDecimal());
        for (int i = startRow + 1; i < rows; i++) {
            double currentVal = Math.abs(data[i][col].toDecimal());
            if (currentVal > maxVal) {
                maxVal = currentVal;
                maxRow = i;
            }
        }
        return maxRow;
    }

    // REQUIRES: j >= 0, j < getNumCols(), pivotRow >= 0, pivotRow < getNumRows(), getElement(pivotRow, j) == 1
    // MODIFIES: this
    // EFFECTS: performs row operations to eliminate values above and below the pivot at 
    private void elimanateColumn(int j, int pivotRow) {
        for (int i = 0; i < this.getNumRows(); i++) {
            if (i == pivotRow) {
                continue;
            }
            Fraction valueToEliminate = getElement(i, j);
            if (valueToEliminate.getNumerator() == 0) {
                continue;
            }
            Fraction multiplier = new Fraction(-valueToEliminate.getNumerator(),
                    valueToEliminate.getDenominator());
            addRowMultiple(pivotRow, i, multiplier);
        }
    }

    // EFFECTS: returns a new Matrix that is a copy of this.
    public Matrix copy() {
        Matrix copy = new Matrix(this.rows, this.cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copy.setElement(i, j, this.getElement(i, j));
            }
        }
        return copy;
    }

    // REQUIRES: b != null, b.size() == getNumRows()
    // EFFECTS: returns a new Matrix augmented with vector b as the last column
    private Matrix augment(Vector b) {
        Matrix augmented = new Matrix(this.rows, this.cols + 1);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                augmented.setElement(i, j, this.getElement(i, j));
            }
            augmented.setElement(i, this.cols, b.getComponent(i));
        }
        return augmented;
    }

    // REQUIRES: m != null
    // EFFECTS: returns a new Matrix consisting of m augmented with the identity matrix
    private Matrix augmentIdentity(Matrix m) {
        Matrix augmented = new Matrix(m.getNumRows(), 2 * m.getNumCols());
        for (int i = 0; i < augmented.getNumRows(); i++) {
            for (int j = 0; j < m.getNumCols(); j++) {
                augmented.setElement(i, j, m.getElement(i, j));
            }
            for (int j = m.getNumCols(); j < augmented.getNumCols(); j++) {
                if (i == j - m.getNumCols()) {
                    augmented.setElement(i, j, new Fraction(1, 1));
                }
            }
        }
        return augmented;
    }

    // EFFECTS: returns a string representation of all operations in the log
    public String getLogString() {
        String logString = "";
        for (LogElement i : log) {
            logString = logString + i.getString() + "\n";
        }
        return logString;
    }

    // EFFECTS: returns the product of determinant effects from all logged operations
    private Fraction getOverallDeterminantEffect() {
        Fraction overAllEffect = new Fraction(1, 1);
        for (LogElement i : log) {
            overAllEffect = overAllEffect.multiply(i.getDeterminantEffect());
        }
        return overAllEffect;
    }

    // REQUIRES: isSquare() == true
    // EFFECTS: returns the trace  of this matrix.
    public Fraction getTrace() {
        Fraction sum = new Fraction(0, 1);
        for (int i = 0; i < rows; i++) {
            sum = sum.add(getElement(i, i));
        }
        return sum;
    }

    // EFFECTS: returns an array of Vectors forming a basis for the column space
    public Vector[] getColumnBasisVectors() {
        Matrix m = copy();
        m.calculaterref();
        int rank = this.getRank();
        Vector[] basis = new Vector[rank];
        int basisIndex = 0;
        for (int j = 0; j < cols; j++) {
            if (isColumnPivotrref(m, j)) {
                basis[basisIndex] = getColumnVector(j);
                basisIndex++;
            }
        }
        return basis;
    }

    // EFFECTS: returns an array of Vectors forming a basis for the row space
    public Vector[] getRowBasisVectors() {
        return this.transpose().getColumnBasisVectors();
    }

    // REQUIRES: m is in Row Reduced Echelon Form, j >= 0, j < m.getNumCols()
    // EFFECTS: returns true if column j contains a pivot
    private boolean isColumnPivotrref(Matrix m, int j) {
        Fraction sum = new Fraction(0, 1);
        for (int i = 0; i < rows; i++) {
            sum = sum.add(m.getElement(i, j));
        }
        if (sum.equals(new Fraction(1, 1))) {
            return true;
        } else {
            return false;
        }

    }
}