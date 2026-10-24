package connected_vehicle.exception;

public class DrivingSessionNotFoundException extends RuntimeException {
    public DrivingSessionNotFoundException(String message) {
        super(message);
    }
}
