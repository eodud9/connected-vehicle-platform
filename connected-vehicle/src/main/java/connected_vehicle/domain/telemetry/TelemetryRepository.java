package connected_vehicle.domain.telemetry;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TelemetryRepository extends JpaRepository<Telemetry, Long> {

    Optional<Telemetry> findFirstByVehicle_IdOrderByRecordedAtDesc(Long vehicleId);
    List<Telemetry> findAllByDrivingSession_IdOrderByRecordedAtAsc(Long drivingSessionId);
}
