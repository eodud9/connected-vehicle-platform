package connected_vehicle.domain.vehicle;

import connected_vehicle.domain.drivingSession.DrivingSessionService;
import connected_vehicle.domain.drivingSession.dto.DrivingSessionResponse;
import connected_vehicle.domain.telemetry.TelemetryService;
import connected_vehicle.domain.telemetry.dto.TelemetryResponse;
import connected_vehicle.domain.telemetry.dto.TelemetryStatusResponse;
import connected_vehicle.domain.user.User;
import connected_vehicle.domain.vehicle.dto.VehicleCreateRequest;
import connected_vehicle.domain.vehicle.dto.VehicleCreateResponse;
import connected_vehicle.domain.vehicle.dto.VehicleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final TelemetryService telemetryService;
    private final DrivingSessionService drivingSessionService;

    @PostMapping
    public ResponseEntity<VehicleCreateResponse> createVehicle(@Valid @RequestBody VehicleCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.createVehicle(request));
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> getVehicles(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(vehicleService.getVehicles(user.getId(), user.getRole()));
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponse> getVehicle(@PathVariable Long vehicleId, @AuthenticationPrincipal User user){

        return ResponseEntity.ok(vehicleService.getVehicle(user.getId(), user.getRole(), vehicleId));

    }

    @GetMapping("/{vehicleId}/status")
    public ResponseEntity<TelemetryStatusResponse> getVehicleStatus(@PathVariable Long vehicleId, @AuthenticationPrincipal User user){
        return ResponseEntity.ok(telemetryService.getLatestStatus(user.getId(), user.getRole(), vehicleId));
    }

    @GetMapping("/{vehicleId}/telemetry")
    public ResponseEntity<Page<TelemetryResponse>> getTelemetries(@PathVariable Long vehicleId,
                                                                  @AuthenticationPrincipal User user,
                                                                  Pageable pageable
    ){

        return ResponseEntity.ok(telemetryService.getTelemetryHistory(user.getId(), user.getRole(), vehicleId, pageable));

    }

    @GetMapping("/{vehicleId}/driving-sessions")
    public ResponseEntity<Page<DrivingSessionResponse>> getDrivingSessions(@PathVariable Long vehicleId,
                                                                           @AuthenticationPrincipal User user,
                                                                           Pageable pageable){

        return ResponseEntity.ok(drivingSessionService.getDrivingSessions(user.getId(), user.getRole(), vehicleId, pageable));

    }
}
