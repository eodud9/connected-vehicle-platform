package connected_vehicle.domain.drivingSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrivingSessionRepository extends JpaRepository<DrivingSession, Long> {
    Page<DrivingSession> findByVehicle_IdOrderByStartedAtDesc(Long vehicleId, Pageable pageable);
}
