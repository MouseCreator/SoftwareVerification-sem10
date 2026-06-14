package mouse.univ.exception;

public class InvalidVertexException extends RuntimeException{
    public InvalidVertexException() {
    }

    public InvalidVertexException(String message) {
        super(message);
    }

    public InvalidVertexException(String message, Throwable cause) {
        super(message, cause);
    }
}
