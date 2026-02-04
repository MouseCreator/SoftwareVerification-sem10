package mouse.univ;

import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    void testConstructor_zero() {
        int[] sizes = {1, 50, 100};
        for (int n : sizes) {
            Matrix z = Matrix.square(n).zero();

            assertEquals(n, z.getNumRows());
            assertEquals(n, z.getNumColumns());
            assertAllZero(z);
        }
    }

    @Test
    void testConstructor_unit() {
        int[] sizes = {1, 50, 100};
        for (int n : sizes) {
            Matrix u = Matrix.square(n).unit();

            assertEquals(n, u.getNumRows());
            assertEquals(n, u.getNumColumns());
            assertIdentity(u);
        }
    }
}