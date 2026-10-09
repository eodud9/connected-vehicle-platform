package connected_vehicle.exception;

public class VehicleAccessDeniedException extends RuntimeException {
    public VehicleAccessDeniedException(String message) {
        super(message);
    }
}
