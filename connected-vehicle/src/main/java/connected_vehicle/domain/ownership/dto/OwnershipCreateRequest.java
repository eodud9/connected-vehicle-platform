package connected_vehicle.domain.ownership.dto;

import jakarta.validation.constraints.NotNull;

public record OwnershipCreateRequest(
        @NotNull
        Long userId,

        @NotNull
        Long vehicleId
) {
}
