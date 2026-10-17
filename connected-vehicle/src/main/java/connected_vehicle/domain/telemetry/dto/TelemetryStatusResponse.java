package connected_vehicle.domain.telemetry.dto;

import java.time.LocalDateTime;

public record TelemetryStatusResponse(
        Long vehicleId,
        double latitude,
        double longitude,
        double speed,
        double batteryLevel,
        double temperature,
        boolean ignitionStatus,
        LocalDateTime recordedAt
) {
}
