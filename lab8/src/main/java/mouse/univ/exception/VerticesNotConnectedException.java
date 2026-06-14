package mouse.univ.exception;

public class VerticesNotConnectedException extends RuntimeException{

    public VerticesNotConnectedException() {

    }

    public VerticesNotConnectedException(String message) {
        super(message);
    }

    public VerticesNotConnectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
