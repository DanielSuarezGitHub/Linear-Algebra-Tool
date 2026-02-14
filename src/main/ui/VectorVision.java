package ui;

import java.util.HashMap;
import java.util.Map;

import model.Fraction;
import model.Vector;
import model.Matrix;
import java.util.Scanner;

public class VectorVision {
    private Map<String, Matrix> matrices;
    private Map<String, Vector> vectors;
    private Scanner input;

    // EFFECTS: Initializes the application with empty storage and starts the main loop
    public VectorVision() {
        matrices = new HashMap<String, Matrix>();
        vectors = new HashMap<String, Vector>();
        input = new Scanner(System.in);
        input.useDelimiter("\r?\n|\r");
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input until the user chooses to quit
    private void runApp() {
        boolean keepGoing = true;
        String command = null;

        while (keepGoing) {
            printMainMenu();
            command = input.next();
            command = command.toLowerCase();
            if (command.equals("q")) {
                keepGoing = false;
            } else {
                dispatchCommand(command);
            }
        }
        System.out.println("Succesfully Exited.");
    }

    // MODIFIES: this
    // EFFECTS: directs user input to the appropriate submenu or operation
    private void dispatchCommand(String command) {
        if (command.equals("m")) {
            openMatrixMenu();
        } else if (command.equals("v")) {
            openVectorMenu();
        } else if (command.equals("w")) {
            openWorkSpace();
        } else if (command.equals("r")) {
            openRemoveItem();
        }
    }

    // EFFECTS: displays the main menu options to the console
    private void printMainMenu() {
        System.out.println("----VectorVision----");
        System.out.println("\tm -> Matrix Operations");
        System.out.println("\tv -> Vector Operations");
        System.out.println("\tw -> View Workspace");
        System.out.println("\tr -> Remove Item");
        System.out.println("\tq -> Quit");
    }

// MODIFIES: this
    // EFFECTS: displays the matrix menu and processes matrix related commands
    private void openMatrixMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- MATRIX OPERATIONS ---");
            System.out.println("\tn -> New Matrix");
            System.out.println("\ta -> Add (A + B)");
            System.out.println("\tm -> Multiply (A * B)");
            System.out.println("\tv -> Multiply Vector (A * v)");
            System.out.println("\tt -> Transpose (A^T)");
            System.out.println("\td -> Determinant |A|");
            System.out.println("\ti -> Inverse (A^-1)");
            System.out.println("\te -> RREF (Row Reduce)");
            System.out.println("\ts -> Solve System (Ax = b)");
            System.out.println("\tk -> Rank");
            System.out.println("\tr -> Trace");
            System.out.println("\tp -> Basis (Column/Row Space)");
            System.out.println("\tb -> Back to Main Menu");
            System.out.print("Select: ");

            String command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                back = true;
            } else if (command.equals("n")) {
                readMatrix();
            } else if (command.equals("a")) {
                addMatrix();
            } else if (command.equals("m")) {
                multiplyMatrix();
            } else if (command.equals("v")) {
                multiplyMatrixVector();
            } else if (command.equals("t")) {
                transposeMatrix();
            } else if (command.equals("d")) {
                determinantMatrix();
            } else if (command.equals("i")) {
                inverseMatrix();
            } else if (command.equals("e")) {
                rrefMatrix();
            } else if (command.equals("s")) {
                solveSystem();
            } else if (command.equals("k")) {
                rankMatrix();
            } else if (command.equals("r")) {
                traceMatrix();
            } else if (command.equals("p")) {
                basisMatrix();
            } else {
                System.out.println("Invalid selection.");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: displays the vector menu and processes vector related commands
    private void openVectorMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- VECTOR OPERATIONS ---");
            System.out.println("\tn -> New Vector");
            System.out.println("\ta -> Add (u + v)");
            System.out.println("\ts -> Subtract (u - v)");
            System.out.println("\tm -> Scalar Multiply (c * v)");
            System.out.println("\td -> Dot Product (u . v)");
            System.out.println("\tc -> Cross Product (u x v)");
            System.out.println("\tg -> Magnitude ||v||");
            System.out.println("\tz -> Normalize (Unit Vector)");
            System.out.println("\to -> Check Orthogonality");
            System.out.println("\tb -> Back to Main Menu");
            System.out.print("Select: ");

            String command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                back = true;
            } else if (command.equals("n")) {
                readVector();
            } else if (command.equals("a")) {
                addVector();
            } else if (command.equals("s")) {
                subtractVector();
            } else if (command.equals("m")) {
                scalarMultiplyVector();
            } else if (command.equals("d")) {
                dotProductVector();
            } else if (command.equals("c")) {
                crossProductVector();
            } else if (command.equals("g")) {
                magnitudeVector();
            } else if (command.equals("z")) {
                normalizeVector();
            } else if (command.equals("o")) {
                orthogonalCheckVector();
            } else {
                System.out.println("Invalid selection.");
            }
        }
    }

    // MODIFIES: this 
    // EFFECTS: prints the list of all stored matrices and vectors to the console
    private void openWorkSpace() {
        boolean back = false;
        if (matrices.isEmpty() && vectors.isEmpty()) {
            System.out.println("Workspace is empty. \n");
            return;
        }
        System.out.println("\t Your Current Matrices");
        for (String name : matrices.keySet()) {
            Matrix m = matrices.get(name);
            System.out.println("[Matrix] " + name + " (" + m.getNumRows() + "x" + m.getNumCols() + ")");
        }
        System.out.println("Your Current Vectors");
        for (String name : vectors.keySet()) {
            Vector v = vectors.get(name);
            System.out.println("[Vector] " + name + " (Size: " + v.size() + ") " + v.toString());
        }
        System.out.println("b -> Back to Main Menu");

        while (!back) {
            String command = input.next();
            command = command.toLowerCase();
            if (command.equals("b")) {
                back = true;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: removes a matrix or vector specified by name; prints error if not found
    public void openRemoveItem() {
        System.out.print("name to remove: ");
        String name = input.next();
        if (matrices.remove(name) != null) {
            System.out.println("Removed Matrix " + name + "\n");
        } else if (vectors.remove(name) != null) {
            System.out.println("Removed Vector " + name + "/n");
        } else {
            System.out.println("Not found. \n");
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user for vector details and adds a new vector to the workspace
    public void readVector() {
        System.out.print("Name of your vector: ");
        String name = input.next();
        if (vectors.containsKey(name)) {
            System.out.println("Exists!");
            return;
        }
        System.out.println("\n");
        System.out.print("Size of your vector: ");
        int size = input.nextInt();
        Fraction[] components = new Fraction[size];
        for (int i = 0; i < size; i++) {
            System.out.print("[" + i + "]: ");
            components[i] = readFraction();
        }
        vectors.put(name, new Vector(components));
        System.out.println("Saved" + name);
    }

    // MODIFIES: this
    // EFFECTS: prompts user for matrix details and adds a new matrix to the workspace
    public void readMatrix() {
        System.out.print("Name of your Matrix: ");
        String name = input.next();
        if (matrices.containsKey(name)) {
            System.out.println("Exists!");
            return;
        }
        System.out.println("\n");
        System.out.print("Row: ");
        int rows = input.nextInt();
        System.out.println("\n");
        System.out.print("Columns: ");
        int columns = input.nextInt();
        Matrix m = new Matrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print("[" + i + "][" + j + "]: ");
                m.setElement(i, j, readFraction());
            }
        }
        matrices.put(name, m);
        System.out.println("Saved" + name);
    }

    // MODIFIES: this
    // EFFECTS: reads a string from input and returns it as a Fraction object
    private Fraction readFraction() {
        String s = input.next();
        long numerator;
        long denominator = 1;
        if (s.contains("/")) {
            String[] parts = s.split("/");
            numerator = Long.parseLong(parts[0]);
            denominator = Long.parseLong(parts[1]);
        } else {
            numerator = Long.parseLong(s);
        }
        return new Fraction(numerator, denominator);
    }

    // MODIFIES: this
    // EFFECTS: computes the sum of two user selected vectors and saves the result
    private void addVector() {
        Vector v1 = selectVector();
        if (v1 == null) {
            return;
        }
        Vector v2 = selectVector();
        if (v2 == null) {
            return;
        }
        if (v1.size() != v2.size()) {
            System.out.println("Incompatible Vector Sizes");
            return;
        }
        saveVector(v1.add(v2));
    }

    // MODIFIES: this
    // EFFECTS: prompts user for a vector name and returns the vector, or null if not found
    private Vector selectVector() {
        if (vectors.isEmpty()) {
            System.out.println("No vectors.");
            return null;
        }
        System.out.print("Vector Name: ");
        String n = input.next();
        if (!vectors.containsKey(n)) {
            System.out.println("Not found.");
            return null;
        }
        return vectors.get(n);
    }

    // MODIFIES: this
    // EFFECTS: adds the given vector to the workspace with a user specified name
    private void saveVector(Vector v) {
        System.out.println("Result:\n");
        System.out.println(v.toString());
        System.out.print("Save as (name): ");
        String name = input.next();
        vectors.put(name, v);
        System.out.println("Saved.");
    }

    // EFFECTS: prints the elements of the given matrix to the console
    private void printMatrixData(Matrix m) {
        for (int i = 0; i < m.getNumRows(); i++) {
            System.out.print("[ ");
            for (int j = 0; j < m.getNumCols(); j++) {
                System.out.print(m.getElement(i, j).toString() + " ");
            }
            System.out.println("]");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds the given matrix to the workspace with a user specified name
    private void saveMatrix(Matrix m) {
        System.out.println("Result:\n");
        printMatrixData(m);
        System.out.print("Save as (name): ");
        String name = input.next();
        matrices.put(name, m);
        System.out.println("Saved.");
    }

    // MODIFIES: this
    // EFFECTS: prompts user for a matrix name and returns the matrix, or null if not found
    private Matrix selectMatrix() {
        if (matrices.isEmpty()) {
            System.out.println("No matrices available.");
            return null;
        }
        System.out.print("Enter matrix name: ");
        String name = input.next();
        if (matrices.containsKey(name)) {
            return matrices.get(name);
        } else {
            System.out.println("Matrix not found.");
            return null;
        }
    }

    // MODIFIES: this
    // EFFECTS: computes the sum of two user selected matrices and saves the result
    private void addMatrix() {
        System.out.println("Select Matrix A:");
        Matrix m1 = selectMatrix();
        if (m1 == null) {
            return;
        }
        System.out.println("Select Matrix B:");
        Matrix m2 = selectMatrix();
        if (m2 == null) {
            return;
        }

        if (m1.getNumRows() != m2.getNumRows() || m1.getNumCols() != m2.getNumCols()) {
            System.out.println("Dimensions do not match.");
            return;
        }

        saveMatrix(m1.add(m2));
    }

    // MODIFIES: this
    // EFFECTS: computes the product of two user selected matrices and saves the result
    private void multiplyMatrix() {
        System.out.println("Select Matrix A:");
        Matrix m1 = selectMatrix();
        if (m1 == null) {
            return;
        }
        System.out.println("Select Matrix B:");
        Matrix m2 = selectMatrix();
        if (m2 == null) {
            return;
        }

        if (m1.getNumCols() != m2.getNumRows()) {
            System.out.println("Invalid dimensions: Cols of A must equal Rows of B.");
            return;
        }

        saveMatrix(m1.multiply(m2));
    }

    // MODIFIES: this
    // EFFECTS: computes the product of a matrix and a vector, saving the result
    private void multiplyMatrixVector() {
        System.out.println("Select Matrix:");
        Matrix m = selectMatrix();
        if (m == null) {
            return;
        }
        System.out.println("Select Vector:");
        Vector v = selectVector();
        if (v == null) {
            return;
        }

        if (m.getNumCols() != v.size()) {
            System.out.println("Dimensions mismatch.");
            return;
        }
        saveVector(m.multiplyVector(v));
    }

    // MODIFIES: this
    // EFFECTS: computes the transpose of a user selected matrix and saves the result
    private void transposeMatrix() {
        Matrix m = selectMatrix();
        if (m == null) {
            return;
        }
        saveMatrix(m.transpose());
    }

    // MODIFIES: this
    // EFFECTS: calculates and prints the determinant of a user selected square matrix
    private void determinantMatrix() {
        Matrix m = selectMatrix();
        if (m == null) {
            return;
        }
        if (!m.isSquare()) {
            System.out.println("Matrix must be square.");
            return;
        }
        System.out.println("Determinant: " + m.getDeterminant().toString());
    }

    // MODIFIES: this
    // EFFECTS: calculates the inverse of a user selected matrix and saves the result
    private void inverseMatrix() {
        Matrix m = selectMatrix();
        if (m == null) {
            return;
        }
        if (!m.isSquare()) {
            System.out.println("Matrix must be square.");
            return;
        }
        if (m.getDeterminant().getNumerator() == 0) {
            System.out.println("Matrix is singular (determinant is 0). Cannot invert.");
            return;
        }
        saveMatrix(m.invert());
    }

    // MODIFIES: this
    // EFFECTS: calculates and prints the RREF of a matrix, including the operations log
    private void rrefMatrix() {
        Matrix m = selectMatrix();
        if (m == null) {
            return;
        }
        Matrix copy = m.copy();
        copy.calculaterref();
        
        System.out.println("RREF Result:");
        printMatrixData(copy);
        System.out.println("\nOperations Log:");
        System.out.println(copy.getLogString());
    }

    // MODIFIES: this
    // EFFECTS: solves the system Ax=b for user selected A and b; saves the solution
    private void solveSystem() {
        System.out.println("Select Coefficient Matrix A:");
        Matrix A = selectMatrix();
        if (A == null) {
            return;
        }
        System.out.println("Select Result Vector b:");
        Vector b = selectVector();
        if (b == null) {
            return;
        }

        Vector solution = A.solve(b);
        if (solution == null) {
            System.out.println("No unique solution exists (inconsistent or infinite solutions).");
        } else {
            System.out.println("Unique solution found:");
            saveVector(solution);
        }
    }

    // MODIFIES: this
    // EFFECTS: subtracts two user selected vectors and saves the result
    private void subtractVector() {
        Vector v1 = selectVector();
        if (v1 == null) {
            return;
        }
        Vector v2 = selectVector();
        if (v2 == null) {
            return;
        }
        if (v1.size() != v2.size()) {
            System.out.println("Sizes differ.");
            return;
        }
        saveVector(v1.subtract(v2));
    }

    // MODIFIES: this
    // EFFECTS: multiplies a user selected vector by a scalar and saves the result
    private void scalarMultiplyVector() {
        Vector v = selectVector();
        if (v == null) {
            return;
        }
        System.out.print("Enter scalar (e.g. 1/2 or 5): ");
        Fraction scalar = readFraction();
        saveVector(v.scalarMultiply(scalar));
    }

    // MODIFIES: this
    // EFFECTS: calculates and prints the dot product of two user selected vectors
    private void dotProductVector() {
        Vector v1 = selectVector();
        if (v1 == null) {
            return;
        }
        Vector v2 = selectVector();
        if (v2 == null) {
            return;
        }
        if (v1.size() != v2.size()) {
            System.out.println("Sizes differ.");
            return;
        }
        System.out.println("Dot Product: " + v1.dot(v2).toString());
    }

    // MODIFIES: this
    // EFFECTS: calculates the cross product of two 3D vectors and saves the result
    private void crossProductVector() {
        Vector v1 = selectVector();
        if (v1 == null) {
            return;
        }
        Vector v2 = selectVector();
        if (v2 == null) {
            return;
        }
        if (v1.size() != 3 || v2.size() != 3) {
            System.out.println("Cross product requires 3D vectors.");
            return;
        }
        saveVector(v1.cross(v2));
    }

    // MODIFIES: this
    // EFFECTS: calculates and prints the magnitude of a user selected vector
    private void magnitudeVector() {
        Vector v = selectVector();
        if (v == null) {
            return;
        }
        System.out.println("Magnitude: " + v.getMagnitude());
    }

    // MODIFIES: this
    // EFFECTS: calculates the normalized unit vector and saves the result
    private void normalizeVector() {
        Vector v = selectVector();
        if (v == null) {
            return;
        }
        if (v.getMagnitude() == 0) {
            System.out.println("Cannot normalize zero vector.");
            return;
        }
        saveVector(v.normalize());
    }

    // MODIFIES: this
    // EFFECTS: checks and prints if two user selected vectors are orthogonal
    private void orthogonalCheckVector() {
        Vector v1 = selectVector();
        if (v1 == null) {
            return;
        }
        Vector v2 = selectVector();
        if (v2 == null) {
            return;
        }
        if (v1.size() != v2.size()) {
            System.out.println("Sizes differ.");
            return;
        }
        System.out.println("Orthogonal? " + v1.isOrthogonal(v2));
    }

    // MODIFIES: this
    // EFFECTS: calculates and prints the rank of a user selected matrix
    private void rankMatrix() {
        Matrix m = selectMatrix();
        if (m == null) {
            return;
        }
        System.out.println("Rank: " + m.getRank());
    }

    // MODIFIES: this
    // EFFECTS: calculates and prints the trace of a user selected square matrix
    private void traceMatrix() {
        Matrix m = selectMatrix();
        if (m == null) {
            return;
        }
        if (!m.isSquare()) {
            System.out.println("Trace is only defined for square matrices.");
            return;
        }
        System.out.println("Trace: " + m.getTrace().toString());
    }

    // MODIFIES: this
    // EFFECTS: prompts user to choose column or row basis and displays the result;
    //          allows user to save result vectors to the workspace
    private void basisMatrix() {
        Matrix m = selectMatrix();
        if (m == null) {
            return;
        }
        System.out.println("Select basis type:");
        System.out.println("\tc -> Column Space Basis");
        System.out.println("\tr -> Row Space Basis");
        String choice = input.next().toLowerCase();
        
        Vector[] basis;
        if (choice.equals("c")) {
            basis = m.getColumnBasisVectors();
            System.out.println("Column Space Basis:");
        } else if (choice.equals("r")) {
            basis = m.getRowBasisVectors();
            System.out.println("Row Space Basis:");
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        for (Vector v : basis) {
            System.out.println(v.toString());
        }
        
        handleBasisSaving(basis);
    }

    // MODIFIES: this
    // EFFECTS: helper to prompt user to save a set of vectors to the workspace
    private void handleBasisSaving(Vector[] basis) {
        if (basis.length == 0) {
            System.out.println("Basis is empty (Zero Space).");
            return;
        }
        System.out.print("Save these vectors to workspace? (y/n): ");
        if (input.next().toLowerCase().equals("y")) {
            for (int i = 0; i < basis.length; i++) {
                System.out.print("Name for basis vector " + (i + 1) + ": ");
                String name = input.next();
                vectors.put(name, basis[i]);
            }
            System.out.println("Vectors saved.");
        }
    }

}