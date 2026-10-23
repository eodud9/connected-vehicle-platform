package connected_vehicle.domain.drivingSession.dto;

import java.time.LocalDateTime;

public record DrivingSessionResponse(
        Long sessionId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Double totalDistance,
        Double averageSpeed,
        double startBatteryLevel,
        Double endBatteryLevel
) {
}
