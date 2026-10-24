package connected_vehicle.domain.vehicleEvent;

import connected_vehicle.domain.ownership.OwnershipService;
import connected_vehicle.domain.telemetry.Telemetry;
import connected_vehicle.domain.user.UserRole;
import connected_vehicle.domain.vehicleEvent.dto.VehicleEventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class VehicleEventService {

    private final VehicleEventRepository vehicleEventRepository;
    private final OwnershipService ownershipService;

    private static final double LOW_BATTERY_THRESHOLD = 20.0;
    private static final double HIGH_TEMPERATURE_THRESHOLD = 45.0;
    private static final double RAPID_ACCELERATION_THRESHOLD = 3.0;
    private static final double RAPID_DECELERATION_THRESHOLD = -3.0;

    public void detectEvents(Telemetry prevTelemetry, Telemetry currentTelemetry){

        if(isLowBattery(prevTelemetry, currentTelemetry)){

            createEvent(currentTelemetry, EventType.LOW_BATTERY, "배터리 잔량이 20% 이하입니다.");

        }
        if(isHighTemperature(prevTelemetry, currentTelemetry)){

            createEvent(currentTelemetry, EventType.HIGH_TEMPERATURE, "배터리 온도가 45°C 이상입니다.");

        }
        if (isRapidAcceleration(prevTelemetry, currentTelemetry)){

            createEvent(currentTelemetry, EventType.RAPID_ACCELERATION, "급가속 상태입니다.");

        }
        if (isRapidDeceleration(prevTelemetry, currentTelemetry)){

            createEvent(currentTelemetry, EventType.RAPID_DECELERATION, "급감속 상태입니다.");

        }
    }

    public boolean isLowBattery(Telemetry prevTelemetry, Telemetry currentTelemetry){

        if(prevTelemetry == null){
            return currentTelemetry.getBatteryLevel() <= LOW_BATTERY_THRESHOLD;

        }

        return prevTelemetry.getBatteryLevel() > LOW_BATTERY_THRESHOLD
                && currentTelemetry.getBatteryLevel() <= LOW_BATTERY_THRESHOLD;
    }

    public boolean isHighTemperature(Telemetry prevTelemetry, Telemetry currentTelemetry){

        if(prevTelemetry == null){
            return currentTelemetry.getTemperature() >= HIGH_TEMPERATURE_THRESHOLD;
        }

        return prevTelemetry.getTemperature() < HIGH_TEMPERATURE_THRESHOLD &&
                currentTelemetry.getTemperature() >= HIGH_TEMPERATURE_THRESHOLD;

    }

    public boolean isRapidAcceleration(Telemetry prevTelemetry, Telemetry currentTelemetry){

        double acceleration = calculateAcceleration(prevTelemetry, currentTelemetry);

        return acceleration >= RAPID_ACCELERATION_THRESHOLD;
    }

    public boolean isRapidDeceleration(Telemetry prevTelemetry, Telemetry currentTelemetry){

        double acceleration = calculateAcceleration(prevTelemetry, currentTelemetry);

        return acceleration <= RAPID_DECELERATION_THRESHOLD;
    }

    public Page<VehicleEventResponse> getVehicleEvents(Long userId, UserRole role, Long vehicleId, Pageable pageable){
        if(role != UserRole.ADMIN){
            ownershipService.validateOwnership(userId, vehicleId);
        }

        Page<VehicleEvent> vehicleEventPage = vehicleEventRepository.findByVehicle_IdOrderByOccurredAtDesc(vehicleId, pageable);

        return vehicleEventPage.map(vehicleEvent -> new VehicleEventResponse(
                vehicleEvent.getId(),
                vehicleEvent.getDrivingSession() == null ? null : vehicleEvent.getDrivingSession().getId(),
                vehicleEvent.getEventType(),
                vehicleEvent.getDescription(),
                vehicleEvent.getOccurredAt()
                ));
    }

    private double calculateAcceleration(Telemetry prevTelemetry, Telemetry currentTelemetry){

        if(prevTelemetry == null) return 0;

        Duration duration = Duration.between(
                prevTelemetry.getRecordedAt(),
                currentTelemetry.getRecordedAt()
        );

        double deltaTime = duration.toMillis() / 1000.0;

        if(deltaTime <= 0) return 0;

        double prevSpeed = prevTelemetry.getSpeed() / 3.6;
        double currentSpeed = currentTelemetry.getSpeed() / 3.6;

        double deltaSpeed = currentSpeed - prevSpeed;

        return deltaSpeed / deltaTime;
    }

    public void createEvent(Telemetry telemetry,
                            EventType eventType,
                            String description){

        VehicleEvent vehicleEvent = new VehicleEvent(
                telemetry.getVehicle(),
                telemetry.getDrivingSession(),
                eventType,
                description,
                telemetry.getRecordedAt()
        );

        vehicleEventRepository.save(vehicleEvent);

    }
}
