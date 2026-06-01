package mouse.univ;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ModulusExponentTest {

    @ParameterizedTest
    @CsvSource({
            "2, 1, 5, 2",
            "2, 3, 5, 3",
            "3, 4, 7, 4",
            "10, 2, 6, 4",
            "5, 3, 13, 8",
            "-2, 3, 5, 2",
            "-2, 4, 5, 1",
            "0, 5, 7, 0",
            "7, 2, 1, 0"
    })
    void calculate_returnsBaseToExponentModuloN(
            int base,
            int exponent,
            int N,
            int expected
    ) {
        assertEquals(expected, ModulusExponent.calculate(base, exponent, N));
    }

    @ParameterizedTest
    @CsvSource({
            "2, 0, 5, 1",
            "1, 0, 1, 1",
            "24, 0, 25, 1",
    })
    void calculate_returnsOneWhenExponentIsZero(
            int base,
            int exponent,
            int N,
            int expected
    ) {
        assertEquals(expected, ModulusExponent.calculate(base, exponent, N));
    }

    @ParameterizedTest
    @CsvSource({
            "2, 3, 0",
            "2, 3, -5"
    })
    void calculate_throwsWhenNIsNotPositive(
            int base,
            int exponent,
            int N
    ) {
        assertThrows(
                IllegalArgumentException.class,
                () -> ModulusExponent.calculate(base, exponent, N)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "2, -1, 5",
            "-2, -3, 7"
    })
    void calculate_throwsWhenExponentIsNegative(
            int base,
            int exponent,
            int N
    ) {
        assertThrows(
                IllegalArgumentException.class,
                () -> ModulusExponent.calculate(base, exponent, N)
        );
    }

    @ParameterizedTest
    @MethodSource("randomInputs")
    void calculate_worksWithRandomBaseInRange(
            int base,
            int exponent,
            int N
    ) {
        int expected = ModulusExponent.slowExponent(base, exponent, N);
        assertEquals(expected, ModulusExponent.calculate(base, exponent, N));
    }

    private static Stream<Arguments> randomInputs() {
        Random random = new Random(12345);

        return IntStream.range(0, 100)
                .mapToObj(i -> {
                    int N = random.nextInt(1, 1_000);
                    int base = random.nextInt(N);
                    int exponent = random.nextInt(0, 50);

                    return Arguments.of(base, exponent, N);
                });
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/exponent/exponent-cases.csv", numLinesToSkip = 1)
    void calculate_readFromFile(int base, int exponent, int N, int expected) {
        assertEquals(expected, ModulusExponent.calculate(base, exponent, N));
    }

    static List<Arguments> exponentCases = List.of(
            Arguments.of(2, 3, 10, 8),
            Arguments.of(5, 0, 10, 1),
            Arguments.of(3, 4, 10, 1),
            Arguments.of(2, 5, 10, 2)
    );

    @ParameterizedTest
    @FieldSource("exponentCases")
    void calculate_withFieldSource(int base, int exponent, int N, int expected) {
        assertEquals(expected, ModulusExponent.calculate(base, exponent, N));
    }

}