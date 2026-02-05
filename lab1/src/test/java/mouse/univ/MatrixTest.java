package mouse.univ;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static mouse.univ.NumberUtils.generateRandomNumbers;
import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {


    @Test
    void testConstructor_createsSquareMatrix() {
        Matrix matrix = Matrix.square(3).withNumbers(List.of(
                1, 2, 3,
                4, 5, 6,
                7, 8, 9
        ));

        assertNotNull(matrix);
        assertEquals(3, matrix.getNumRows());
        assertEquals(3, matrix.getNumColumns());
    }

    @Test
    void testConstructor_createsRectangularMatrix() {
        Matrix matrix = Matrix.rect(2, 3).withNumbers(List.of(
                1, 2, 3,
                4, 5, 6
        ));

        assertNotNull(matrix);
        assertEquals(2, matrix.getNumRows());
        assertEquals(3, matrix.getNumColumns());
    }

    @Test
    void testConstructor_throwsOnSizeMismatch() {
        assertThrows(IllegalArgumentException.class, () ->
                Matrix.rect(2, 3).withNumbers(List.of(
                1, 2, 3,
                4, 5, 6,
                7, 8, 9
                )));

        assertThrows(IllegalArgumentException.class, () ->
                Matrix.rect(2, 3).withNumbers(List.of(
                        1, 2, 3,
                        4, 5
                )));

        assertThrows(IllegalArgumentException.class, () ->
                Matrix.rect(2, 3).withNumbers(List.of(
                        1, 2, 3,
                        4, 5, 6,
                        7
                )));
        assertThrows(IllegalArgumentException.class, () ->
                Matrix.rect(2, 3).withNumbers(List.of()));
    }

    @Test
    void testConstructor_createsMinSizeMatrix() {
        Matrix matrix = Matrix.square(1).withNumbers(List.of(0));
        assertEquals(1, matrix.getNumRows());
        assertEquals(1, matrix.getNumColumns());
        assertEquals(0, matrix.at(0,0));
    }

    @Test
    void testConstructor_reads_100x100Matrix() {
        List<Double> content = generateRandomNumbers(100 * 100);
        Matrix matrix = Matrix.square(100).withNumbers(content);
        assertEquals(100, matrix.getNumRows());
        assertEquals(100, matrix.getNumColumns());
    }

    @Test
    void testConstructor_throwsFor101Columns() {
        List<Double> content = generateRandomNumbers(101 * 100);
        assertThrows(IllegalArgumentException.class, () -> Matrix.rect(101, 100).withNumbers(content));
    }

    @Test
    void testConstructor_throwsFor101Rows() {
        List<Double> content = generateRandomNumbers(100 * 101);
        assertThrows(IllegalArgumentException.class, () -> Matrix.rect(100, 101).withNumbers(content));
    }

    @Test
    void testConstructor_cannotCreateEmptyMatrix() {
        assertThrows(IllegalArgumentException.class, () -> Matrix.square(0).withNumbers(List.of()));
    }

    @Test
    void testConstructor_cannotCreateNegativeSizedMatrix() {
        assertThrows(IllegalArgumentException.class, () -> Matrix.square(Integer.MIN_VALUE).withNumbers(List.of()));
        assertThrows(IllegalArgumentException.class, () -> Matrix.square(Integer.MIN_VALUE/2).withNumbers(List.of()));
        assertThrows(IllegalArgumentException.class, () -> Matrix.square(-1).withNumbers(List.of()));
        assertThrows(IllegalArgumentException.class, () -> Matrix.square(-100).withNumbers(List.of()));
    }

    @Test
    void testConstructor_cannotCreateLargeSizedMatrix() {
        assertThrows(IllegalArgumentException.class, () -> Matrix.square(Integer.MAX_VALUE));
        assertThrows(IllegalArgumentException.class, () -> Matrix.square(Integer.MAX_VALUE/2));
        assertThrows(IllegalArgumentException.class, () -> Matrix.square(101));
        assertThrows(IllegalArgumentException.class, () -> Matrix.square(10000));
    }

    private void testAllowedNumber(double d) {
        assertEquals(d, Matrix.square(1).withNumbers(List.of(d)).at(0,0));
    }

    @Test
    void testConstructor_allowedMatrixValues() {
        testAllowedNumber(0);
        testAllowedNumber(10000);
        testAllowedNumber(-10000);
        testAllowedNumber(5000);
        testAllowedNumber(-5000);
    }

    private void assertAllZero(Matrix m) {
        for (int i = 0; i < m.getNumRows(); i++) {
            for (int j = 0; j < m.getNumColumns(); j++) {
                assertEquals(0.0, m.at(i, j), "Expected 0 at (" + i + "," + j + ")");
            }
        }
    }

    private void assertIdentity(Matrix m) {
        int n = m.getNumRows();
        assertEquals(n, m.getNumColumns(), "Identity matrix must be square");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double expected = (i == j) ? 1.0 : 0.0;
                assertEquals(expected, m.at(i, j), "Wrong value at (" + i + "," + j + ")");
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 50, 100})
    void testConstructor_zero(int n) {
        Matrix z = Matrix.square(n).zero();

        assertEquals(n, z.getNumRows());
        assertEquals(n, z.getNumColumns());
        assertAllZero(z);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 50, 100})
    void testConstructor_unit(int n) {
        Matrix u = Matrix.square(n).unit();

        assertEquals(n, u.getNumRows());
        assertEquals(n, u.getNumColumns());
        assertIdentity(u);
    }

    @ParameterizedTest
    @ValueSource(doubles = {
            -10_000, -100, -10, -1, 0, 1, 10, 100, 10_000
    })
    void testSet_setsNewValueAndReturnsPrevious(double newValue) {
        Matrix matrix = Matrix.square(1).withNumbers(new Double[]{11.0});

        double previous = matrix.set(0, 0, newValue);

        assertEquals(11.0, previous, 0.0, "Returned previous value is incorrect");
        assertEquals(newValue, matrix.at(0, 0), 0.0, "New value was not set correctly");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 10, 100})
    void row_shouldReturnCorrectRowElements(int n) {
        Matrix matrix = randomSquare(n);

        for (int i = 0; i < n; i++) {
            List<Double> row = matrix.row(i);
            assertEquals(n, row.size(), "Row size mismatch for n=" + n);

            for (int j = 0; j < n; j++) {
                assertEquals(
                        matrix.at(i, j),
                        row.get(j),
                        0.0,
                        "Mismatch at row(" + i + "), col(" + j + "), n=" + n
                );
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 10, 100})
    void column_shouldReturnCorrectColumnElements(int n) {
        Matrix matrix = randomSquare(n);

        for (int j = 0; j < n; j++) {
            List<Double> column = matrix.column(j);
            assertEquals(n, column.size(), "Column size mismatch for n=" + n);

            for (int i = 0; i < n; i++) {
                assertEquals(
                        matrix.at(i, j),
                        column.get(i),
                        0.0,
                        "Mismatch at row(" + i + "), col(" + j + "), n=" + n
                );
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 10, 100})
    void asList_shouldReturnAllRowsCorrectly(int n) {
        Matrix matrix = randomSquare(n);

        List<List<Double>> rows = matrix.asList();
        assertEquals(n, rows.size(), "Outer list size mismatch for n=" + n);

        for (int i = 0; i < n; i++) {
            List<Double> row = rows.get(i);
            assertEquals(n, row.size(), "Row size mismatch at row " + i + ", n=" + n);

            for (int j = 0; j < n; j++) {
                assertEquals(
                        matrix.at(i, j),
                        row.get(j),
                        0.0,
                        "Mismatch at row(" + i + "), col(" + j + "), n=" + n
                );
            }
        }
    }

    private static Matrix randomRect(int n, int m) {
        List<Double> nums = NumberUtils.generateRandomNumbers(n * m);
        return Matrix.rect(n, m).withNumbers(nums);
    }

    private static Matrix randomSquare(int n) {
        return randomRect(n, n);
    }

    private static Matrix zeroSquare(int n) {
        return Matrix.square(n).zero();
    }

    private static Matrix identity(int n) {
        return Matrix.square(n).unit();
    }

    private static Matrix constantSquare(int n, double value) {
        List<Double> nums = new ArrayList<>(n * n);
        for (int i = 0; i < n * n; i++) nums.add(value);
        return Matrix.square(n).withNumbers(nums);
    }

    private static void assertMatrixEqualsByAt(Matrix actual, Matrix expected) {
        assertEquals(expected.getNumRows(), actual.getNumRows(), "Row count mismatch");
        assertEquals(expected.getNumColumns(), actual.getNumColumns(), "Column count mismatch");

        for (int i = 0; i < expected.getNumRows(); i++) {
            for (int j = 0; j < expected.getNumColumns(); j++) {
                assertEquals(expected.at(i, j), actual.at(i, j), 0.0,
                        "Mismatch at (" + i + "," + j + ")");
            }
        }
    }

    private static Matrix manualDot(Matrix a, Matrix b) {
        if (a.getNumColumns() != b.getNumRows()) {
            throw new IllegalArgumentException("Manual dot: incompatible dimensions");
        }
        int n = a.getNumRows();
        int mid = a.getNumColumns();
        int m = b.getNumColumns();
        Matrix res = Matrix.rect(n, m).zero();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                double sum = 0.0;
                for (int k = 0; k < mid; k++) {
                    sum += a.at(i, k) * b.at(k, j);
                }
                res.set(i, j, sum);
            }
        }
        return res;
    }

    private static Stream<Arguments> rectangularSameSizePairs() {
        return Stream.of(
                Arguments.of(3, 5),
                Arguments.of(10, 50),
                Arguments.of(100, 50)
        );
    }

    private static Stream<Arguments> dotDifferentSizePairs() {
        return Stream.of(
                Arguments.of(3, 5, 5, 2),       // 3x5 · 5x2 -> 3x2
                Arguments.of(10, 50, 50, 7),    // 10x50 · 50x7 -> 10x7
                Arguments.of(100, 50, 50, 100)  // 100x50 · 50x100 -> 100x100
        );
    }

    // ---------- correct data ----------

    @ParameterizedTest
    @ValueSource(ints = {1, 50, 100})
    void testAdd_randomSquareMatrices(int n) {
        Matrix a = randomSquare(n);
        Matrix b = randomSquare(n);

        Matrix c = a.add(b);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                assertEquals(a.at(i, j) + b.at(i, j), c.at(i, j), 0.0,
                        "Mismatch at (" + i + "," + j + "), n=" + n);
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 50, 100})
    void testSubtract_randomSquareMatrices(int n) {
        Matrix a = randomSquare(n);
        Matrix b = randomSquare(n);

        Matrix c = a.subtract(b);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                assertEquals(a.at(i, j) - b.at(i, j), c.at(i, j), 0.0,
                        "Mismatch at (" + i + "," + j + "), n=" + n);
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 50, 100})
    void testAdd_addZeroSquareMatrix(int n) {
        Matrix a = randomSquare(n);
        Matrix z = zeroSquare(n);

        Matrix c = a.add(z);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                assertEquals(a.at(i, j), c.at(i, j), 0.0,
                        "Mismatch at (" + i + "," + j + "), n=" + n);
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 50, 100})
    void testSubtract_subtractZeroSquareMatrix(int n) {
        Matrix a = randomSquare(n);
        Matrix z = zeroSquare(n);

        Matrix c = a.subtract(z);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                assertEquals(a.at(i, j), c.at(i, j), 0.0,
                        "Mismatch at (" + i + "," + j + "), n=" + n);
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 50, 100})
    void testDot_multiplyByIdentityMatrix(int n) {
        Matrix a = randomSquare(n);
        Matrix i = identity(n);

        Matrix right = a.dot(i);
        Matrix left = i.dot(a);

        assertMatrixEqualsByAt(right, a);
        assertMatrixEqualsByAt(left, a);
    }

    @ParameterizedTest
    @MethodSource("rectangularSameSizePairs")
    void testAdd_randomRectangularMatrices(int n, int m) {
        Matrix a = randomRect(n, m);
        Matrix b = randomRect(n, m);

        Matrix c = a.add(b);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                assertEquals(a.at(i, j) + b.at(i, j), c.at(i, j), 0.0,
                        "Mismatch at (" + i + "," + j + "), size=" + n + "x" + m);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("rectangularSameSizePairs")
    void testSubtract_randomRectangularMatrices(int n, int m) {
        Matrix a = randomRect(n, m);
        Matrix b = randomRect(n, m);

        Matrix c = a.subtract(b);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                assertEquals(a.at(i, j) - b.at(i, j), c.at(i, j), 0.0,
                        "Mismatch at (" + i + "," + j + "), size=" + n + "x" + m);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("dotDifferentSizePairs")
    void testDot_multiplyDifferentSizeMatrices(int aN, int aM, int bN, int bM) {
        Matrix a = randomRect(aN, aM);
        Matrix b = randomRect(bN, bM);

        Matrix actual = a.dot(b);
        Matrix expected = manualDot(a, b);

        assertMatrixEqualsByAt(actual, expected);
    }

    @Test
    void testAdd_largeConstant3x3Matrices() {
        Matrix a = constantSquare(3, 10_000.0);
        Matrix b = constantSquare(3, 10_000.0);

        Matrix c = a.add(b);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(20_000.0, c.at(i, j), 0.0, "Mismatch at (" + i + "," + j + ")");
            }
        }
    }

    @Test
    void testSubtract_largeConstant3x3Matrices() {
        Matrix a = constantSquare(3, 10_000.0);
        Matrix b = constantSquare(3, -10_000.0);

        Matrix c = a.subtract(b); // 10k - (-10k) = 20k

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(20_000.0, c.at(i, j), 0.0, "Mismatch at (" + i + "," + j + ")");
            }
        }
    }

    @Test
    void testDot_largeConstant3x3Matrices() {
        Matrix p = constantSquare(3, 10_000.0);
        Matrix n = constantSquare(3, -10_000.0);

        double pp = 300_000_000.0;
        double pn = -300_000_000.0;
        double nn = 300_000_000.0;

        Matrix ppRes = p.dot(p);
        Matrix pnRes = p.dot(n);
        Matrix nnRes = n.dot(n);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(pp, ppRes.at(i, j), NumberUtils.DEFAULT_THRESHOLD, "p·p mismatch at (" + i + "," + j + ")");
                assertEquals(pn, pnRes.at(i, j), NumberUtils.DEFAULT_THRESHOLD, "p·n mismatch at (" + i + "," + j + ")");
                assertEquals(nn, nnRes.at(i, j), NumberUtils.DEFAULT_THRESHOLD, "n·n mismatch at (" + i + "," + j + ")");
            }
        }
    }

    @Test
    void testAdd_mismatchSizes_throws() {
        Matrix a = randomRect(3, 4);
        Matrix b = randomRect(3, 5);
        assertThrows(IllegalArgumentException.class, () -> a.add(b));
    }

    @Test
    void testSubtract_mismatchSizes_throws() {
        Matrix a = randomRect(10, 10);
        Matrix b = randomRect(10, 9);
        assertThrows(IllegalArgumentException.class, () -> a.subtract(b));
    }

    @Test
    void testDot_mismatchSizes_throws() {
        Matrix a = randomRect(3, 4);
        Matrix b = randomRect(5, 2); // a.m=4 != b.n=5
        assertThrows(IllegalArgumentException.class, () -> a.dot(b));
    }
}