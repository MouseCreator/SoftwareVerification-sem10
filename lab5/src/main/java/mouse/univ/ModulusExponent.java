package mouse.univ;

public class ModulusExponent {

    public static int calculate(int base, int exponent, int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be positive");
        }
        if (exponent < 0) {
            throw new IllegalArgumentException("exponent must be non-negative");
        }
        long result = 1;
        long current = ((base % N) + N) % N;

        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                result = (result * current) % N;
            }

            current = (current * current) % N;
            exponent >>= 1;
        }

        return (int) result;
    }

    public static int slowExponent(int base, int exponent, int N) {
        long result = 1;

        for (int i = 0; i < exponent; i++) {
            result = (result * base) % N;
        }

        return (int) result;
    }
}
