package connected_vehicle.domain.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleCreateRequest(@NotBlank String vin,
                                   @NotBlank String licensePlate,
                                   @NotBlank String modelName,
                                   @NotNull Integer modelYear) {
}
