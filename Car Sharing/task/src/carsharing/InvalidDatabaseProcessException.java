package carsharing;

public class InvalidDatabaseProcessException extends RuntimeException {

    public InvalidDatabaseProcessException(String message) {
        super(message);
    }
}
