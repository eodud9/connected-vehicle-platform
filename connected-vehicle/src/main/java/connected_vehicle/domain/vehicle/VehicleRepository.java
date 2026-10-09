package connected_vehicle.domain.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByVin(String vin);
    boolean existsByVin(String vin);
    boolean existsByLicensePlate(String licensePlate);
}
