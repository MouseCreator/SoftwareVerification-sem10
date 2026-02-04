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
}