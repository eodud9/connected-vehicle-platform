package connected_vehicle.exception;

public class TelemetryNotFoundException extends RuntimeException {
    public TelemetryNotFoundException(String message) {
        super(message);
    }
}
