package connected_vehicle.domain.user.dto;

import connected_vehicle.domain.user.UserRole;

import java.time.LocalDateTime;

public record UserCreateResponse(
        Long id,
        String name,
        String email,
        UserRole role,
        LocalDateTime createdAt
) {
}
