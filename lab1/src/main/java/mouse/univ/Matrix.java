package mouse.univ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Matrix {
    private final double[][] array;
    private final int n;
    private final int m;

    private static final int MAX_ROWS = 100;
    private static final int MAX_COLS = 100;

    public int getNumColumns() {
        return m;
    }
    public int getNumRows() {
        return n;
    }

    private Matrix(double[][] array) {
        this.array = array;
        this.n = array.length;
        if (this.n == 0) {
            this.m = 0;
        } else {
            this.m = array[0].length;
        }
    }

    public static SizedMatrixBuilder rect(int n, int m) {
        if (n > MAX_ROWS) {
            String err = String.format("Number of rows cannot exceed %d. Given: %d", MAX_ROWS, n);
            throw new IllegalArgumentException(err);
        }
        if (m > MAX_COLS) {
            String err = String.format("Number of columns cannot exceed %d. Given: %d", MAX_COLS, m);
            throw new IllegalArgumentException(err);
        }
        return new SizedMatrixBuilder(n, m);
    }

    public static SizedMatrixBuilder square(int n) {
        if (n > MAX_ROWS) {
            String err = String.format("Number of rows and columns cannot exceed %d. Given: %d", MAX_ROWS, n);
            throw new IllegalArgumentException(err);
        }
        return new SizedMatrixBuilder(n, n);
    }

    public static class SizedMatrixBuilder {
        private final int n;
        private final int m;

        private SizedMatrixBuilder(int n, int m) {
            this.n = n;
            this.m = m;
        }

        public Matrix withNumbers(List<?> numbers) {
            if (numbers.size() != n * m) {
                String err = String.format("Failed to create matrix! Number of elements in matrix should be N * M = %d * %d = %d!", n, m, n * m);
                throw new IllegalArgumentException(err);
            }

            List<Double> dList = new ArrayList<>();
            for (Object current : numbers) {
                if (current instanceof Integer c1) {
                    dList.add(Double.valueOf(c1));
                } else if (current instanceof Double c2) {
                    dList.add(c2);
                } else {
                    throw new IllegalArgumentException("Input list contains unexpected object: " + current);
                }
            }

            double[][] array = new double[n][m];
            int k = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    array[i][j] = dList.get(k);
                    k++;
                }
            }
            return new Matrix(array);
        }

        public Matrix withNumbers(Double[] numbers) {
            List<Double> list = Arrays.asList(numbers);
            return withNumbers(list);
        }

        public Matrix zero() {
            List<Double> list = new ArrayList<>();
            int size = n * m;
            for (int i = 0; i < size; i++) {
                list.add(0.0);
            }
            return withNumbers(list);
        }

        public Matrix unit() {
            if (n != m) {
                String err = String.format("Unable to create unit matrix for non-square matrix of size %dx%d", n, m);
                throw new IllegalStateException(err);
            }
            List<Double> list = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j< m; j++) {
                    list.add(i == j ? 1.0 : 0.0);
                }
            }
            return withNumbers(list);
        }

    }

    public double at(int i, int j) {
        return this.array[i][j];
    }

    public double set(int i, int j, double d) {
        double prev = this.array[i][j];
        this.array[i][j] = d;
        return prev;
    }

    public List<Double> row(int i) {
        if (i < 0 || i >= n) {
            throw new IndexOutOfBoundsException("Row index out of bounds: " + i);
        }
        List<Double> result = new ArrayList<>(m);
        for (int j = 0; j < m; j++) {
            result.add(array[i][j]);
        }
        return result;
    }

    public List<Double> column(int j) {
        if (j < 0 || j >= m) {
            throw new IndexOutOfBoundsException("Column index out of bounds: " + j);
        }
        List<Double> result = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            result.add(array[i][j]);
        }
        return result;
    }

    public List<List<Double>> asList() {
        List<List<Double>> result = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            result.add(row(i)); // reuse row() implementation
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(n, m);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Matrix m2) {
            return Arrays.deepEquals(this.array, m2.array);
        }
        return false;
    }

    public boolean deepEquals(Matrix m2) {
        return deepEquals(m2, NumberUtils.DEFAULT_THRESHOLD);
    }

    public boolean deepEquals(Matrix m2, double th) {
        if (this.n != m2.n || this.m != m2.m) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                double number1 = this.at(i, j);
                double number2 = m2.at(i, j);
                if (! NumberUtils.doubleEqual(number1, number2, th)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Matrix add(Matrix m2) {
        if (this.n != m2.n || this.m != m2.m) {
            throw new IllegalArgumentException(
                    "Matrix addition requires same dimensions"
            );
        }

        double[][] result = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result[i][j] = this.array[i][j] + m2.array[i][j];
            }
        }
        return new Matrix(result);
    }

    public Matrix subtract(Matrix m2) {
        if (this.n != m2.n || this.m != m2.m) {
            throw new IllegalArgumentException(
                    "Matrix subtraction requires same dimensions"
            );
        }

        double[][] result = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result[i][j] = this.array[i][j] - m2.array[i][j];
            }
        }
        return new Matrix(result);
    }

    public Matrix dot(Matrix m2) {
        if (this.m != m2.n) {
            throw new IllegalArgumentException(
                    "Matrix multiplication requires columns of A == rows of B"
            );
        }

        double[][] result = new double[this.n][m2.m];

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < m2.m; j++) {
                double sum = 0.0;
                for (int k = 0; k < this.m; k++) {
                    sum += this.array[i][k] * m2.array[k][j];
                }
                result[i][j] = sum;
            }
        }
        return new Matrix(result);
    }

    @Override
    public String toString() {
        if (n == 0 || m == 0) {
            return "[]";
        }

        int[] colWidths = new int[m];
        for (int j = 0; j < m; j++) {
            int max = 0;
            for (int i = 0; i < n; i++) {
                String s = Double.toString(array[i][j]);
                max = Math.max(max, s.length());
            }
            colWidths[j] = max;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("[");
            for (int j = 0; j < m; j++) {
                String s = Double.toString(array[i][j]);
                sb.append(String.format("%" + colWidths[j] + "s", s));
                if (j < m - 1) {
                    sb.append(" ");
                }
            }
            sb.append("]");
            if (i < n - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
