package connected_vehicle.domain.drivingSession;

import connected_vehicle.domain.drivingSession.dto.DrivingSessionResponse;
import connected_vehicle.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/driving-sessions")
@RequiredArgsConstructor
public class DrivingSessionController {

    private final DrivingSessionService drivingSessionService;

    @GetMapping("/{sessionId}")
    public ResponseEntity<DrivingSessionResponse> getDrivingSession(@PathVariable Long sessionId, @AuthenticationPrincipal User user){
        return ResponseEntity.ok(drivingSessionService.getDrivingSession(user.getId(), user.getRole(), sessionId));
    }
}
