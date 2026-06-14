package mouse.univ.exception;

public class InfinitelyLowWeightException extends RuntimeException {
    public InfinitelyLowWeightException() {
    }

    public InfinitelyLowWeightException(String message) {
        super(message);
    }

    public InfinitelyLowWeightException(String message, Throwable cause) {
        super(message, cause);
    }
}
