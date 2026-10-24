package connected_vehicle.domain.drivingSession;

import connected_vehicle.domain.drivingSession.dto.DrivingSessionResponse;
import connected_vehicle.domain.ownership.OwnershipService;
import connected_vehicle.domain.user.UserRole;
import connected_vehicle.exception.DrivingSessionNotFoundException;
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

    public DrivingSessionResponse getDrivingSession(Long userId, UserRole role, Long sessionId){

        DrivingSession session = drivingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new DrivingSessionNotFoundException("요청하신 세션을 찾을 수 없습니다."));

        if(role != UserRole.ADMIN){
            ownershipService.validateOwnership(userId, session.getVehicle().getId());
        }

        DrivingSessionResponse response = new DrivingSessionResponse(
                session.getId(),
                session.getStartedAt(),
                session.getEndedAt(),
                session.getTotalDistance(),
                session.getAverageSpeed(),
                session.getStartBatteryLevel(),
                session.getEndBatteryLevel()
                );

        return response;
    }
}
