package mouse.univ;

import org.junit.jupiter.api.Test;

import java.util.List;

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
}