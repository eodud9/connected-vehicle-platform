package connected_vehicle.domain.drivingSession;

import connected_vehicle.domain.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "driving_sessions")
public class DrivingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private Double totalDistance;

    private Double averageSpeed;

    @Column(nullable = false)
    private Double startBatteryLevel;

    private Double endBatteryLevel;

    protected DrivingSession(){}

    public DrivingSession(Vehicle vehicle, LocalDateTime startedAt, Double startBatteryLevel){
        this.vehicle = vehicle;
        this.startedAt = startedAt;
        this.startBatteryLevel = startBatteryLevel;
    }

    public void end(LocalDateTime endedAt, Double endBatteryLevel, Double totalDistance, Double averageSpeed){
        this.endedAt = endedAt;
        this.endBatteryLevel = endBatteryLevel;
        this.totalDistance = totalDistance;
        this.averageSpeed = averageSpeed;

    }
}
