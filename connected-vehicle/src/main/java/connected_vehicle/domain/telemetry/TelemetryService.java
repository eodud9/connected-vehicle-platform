package connected_vehicle.domain.telemetry;

import connected_vehicle.domain.telemetry.dto.TelemetryCreateRequest;
import connected_vehicle.domain.vehicle.Vehicle;
import connected_vehicle.domain.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelemetryService {

    private final TelemetryRepository telemetryRepository;
    private final VehicleService vehicleService;

    public void createTelemetry(Long vehicleId, String apiKey, TelemetryCreateRequest request){

        Vehicle vehicle = vehicleService.authenticateVehicle(vehicleId, apiKey);

        Telemetry telemetry = new Telemetry(
                vehicle,
                request.latitude(),
                request.longitude(),
                request.speed(),
                request.batteryLevel(),
                request.temperature(),
                request.ignitionStatus(),
                request.recordedAt());

        telemetryRepository.save(telemetry);

    }
}
