package connected_vehicle.exception;

public class ActiveOwnershipAlreadyExistsException extends RuntimeException {
    public ActiveOwnershipAlreadyExistsException(String message) {
        super(message);
    }
}
