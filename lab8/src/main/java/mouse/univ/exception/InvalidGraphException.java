package mouse.univ.exception;

public class InvalidGraphException extends RuntimeException{
    public InvalidGraphException() {
    }

    public InvalidGraphException(String message) {
        super(message);
    }

    public InvalidGraphException(String message, Throwable cause) {
        super(message, cause);
    }
}
