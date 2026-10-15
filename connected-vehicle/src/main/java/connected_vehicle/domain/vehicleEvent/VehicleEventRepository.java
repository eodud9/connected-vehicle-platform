package connected_vehicle.domain.vehicleEvent;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleEventRepository extends JpaRepository<VehicleEvent, Long> {
}
