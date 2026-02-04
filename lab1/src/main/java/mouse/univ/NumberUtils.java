package mouse.univ;

public class NumberUtils {

    public static double DEFAULT_THRESHOLD = 1e-6;

    public static boolean doubleEqual(double d1, double d2) {
        return doubleEqual(d1, d2, DEFAULT_THRESHOLD);
    }
    public static boolean doubleEqual(double d1, double d2, double th) {
        return Math.abs(d1 - d2) < th;
    }
}
