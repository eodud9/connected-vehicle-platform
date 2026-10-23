package connected_vehicle.domain.drivingSession;

import connected_vehicle.domain.drivingSession.dto.DrivingSessionResponse;
import connected_vehicle.domain.ownership.OwnershipService;
import connected_vehicle.domain.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrivingSessionService {

    private final DrivingSessionRepository drivingSessionRepository;
    private final OwnershipService ownershipService;

    public Page<DrivingSessionResponse> getDrivingSessions(Long userId, UserRole role, Long vehicleId, Pageable pageable){

        if(role != UserRole.ADMIN){
            ownershipService.validateOwnership(userId, vehicleId);
        }

        Page<DrivingSession> drivingSessionPage = drivingSessionRepository
                .findByVehicle_IdOrderByStartedAtDesc(vehicleId, pageable);

        Page<DrivingSessionResponse> drivingSessionResponses = drivingSessionPage.map(
                drivingSession -> new DrivingSessionResponse(
                        drivingSession.getId(),
                        drivingSession.getStartedAt(),
                        drivingSession.getEndedAt(),
                        drivingSession.getTotalDistance(),
                        drivingSession.getAverageSpeed(),
                        drivingSession.getStartBatteryLevel(),
                        drivingSession.getEndBatteryLevel()
                ));

        return drivingSessionResponses;
    }
}
