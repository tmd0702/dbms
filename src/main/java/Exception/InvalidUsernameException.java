package Exception;

public class InvalidUsernameException extends Exception {
    public InvalidUsernameException(String errorMessage) {
        super(errorMessage);
    }
}
