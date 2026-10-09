package connected_vehicle.domain.ownership.dto;

public record OwnershipCreateResponse(
        Long id,
        Long userId,
        Long vehicleId
) {

}
