package connected_vehicle.exception;

public class DuplicateVinException extends RuntimeException {
    public DuplicateVinException(String message) {
        super(message);
    }
}
