package connected_vehicle.domain.ownership;

import connected_vehicle.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OwnershipRepository extends JpaRepository<Ownership, Long> {
    boolean existsByUser_IdAndVehicle_IdAndEndedAtIsNull(Long userId, Long vehicleId);
    boolean existsByVehicle_IdAndEndedAtIsNull(Long vehicleId);

    @Query("""
            select v
            from Ownership o
            join o.vehicle v
            where o.user.id = :userId
            and o.endedAt is null
            """)
    List<Vehicle> findCurrentVehiclesByUserId(Long userId);
}
