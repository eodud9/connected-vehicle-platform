package connected_vehicle.domain.telemetry.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TelemetryCreateRequest(
        @NotNull
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        Double latitude,

        @NotNull
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        Double longitude,

        @NotNull
        @DecimalMin("0.0")
        Double speed,

        @NotNull
        @DecimalMin("0.0")
        @DecimalMax("100.0")
        Double batteryLevel,

        @NotNull
        Double temperature,

        @NotNull
        Boolean ignitionStatus,

        @NotNull
        LocalDateTime recordedAt
) {
}
