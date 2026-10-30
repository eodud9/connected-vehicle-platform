package simulator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SimulatedVehicle {
    private final Long vehicleId;
    private final String apiKey;
    private final VehicleState state;
}
