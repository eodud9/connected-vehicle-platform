package connected_vehicle.domain.telemetry;

import connected_vehicle.domain.telemetry.dto.TelemetryCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles/{vehicleId}/telemetry")
@RequiredArgsConstructor
public class TelemetryController {

    private final TelemetryService telemetryService;

    @PostMapping
    public ResponseEntity<Void> createTelemetry(
            @PathVariable Long vehicleId,
            @RequestHeader(name = "X-Vehicle-Api-Key") String apiKey,
            @Valid @RequestBody TelemetryCreateRequest request
            ){

        telemetryService.createTelemetry(vehicleId, apiKey, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
