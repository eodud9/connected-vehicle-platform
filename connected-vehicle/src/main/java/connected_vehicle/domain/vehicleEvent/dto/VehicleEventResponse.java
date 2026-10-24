package connected_vehicle.domain.vehicleEvent.dto;

import connected_vehicle.domain.vehicleEvent.EventType;

import java.time.LocalDateTime;

public record VehicleEventResponse(
        Long eventId,
        Long drivingSessionId,
        EventType eventType,
        String description,
        LocalDateTime occurredAt
) {
}
