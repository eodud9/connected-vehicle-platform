package connected_vehicle.domain.vehicle.dto;

import java.time.LocalDateTime;

public record VehicleCreateResponse(
        Long id,
        String vin,
        String licensePlate,
        String modelName,
        Integer modelYear,
        LocalDateTime registeredAt,
        String apiKey
) {
}
