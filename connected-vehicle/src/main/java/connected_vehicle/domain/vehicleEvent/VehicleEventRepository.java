package connected_vehicle.domain.vehicleEvent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleEventRepository extends JpaRepository<VehicleEvent, Long> {
    Page<VehicleEvent> findByVehicle_IdOrderByOccurredAtDesc(Long vehicleId, Pageable pageable);
}
