package connected_vehicle.domain.telemetry;

import connected_vehicle.domain.drivingSession.DrivingSession;
import connected_vehicle.domain.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Getter;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "telemetries")
public class Telemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driving_session_id")
    private DrivingSession drivingSession;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private double speed;

    @Column(nullable = false)
    private double batteryLevel;

    @Column(nullable = false)
    private double temperature;

    @Column(nullable = false)
    private boolean ignitionStatus;

    @Column(nullable = false)
    private LocalDateTime recordedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Telemetry(){}

    public Telemetry(Vehicle vehicle, double speed, double longitude, double latitude, double batteryLevel, double temperature, boolean ignitionStatus, LocalDateTime recordedAt){
        this.vehicle = vehicle;
        this.speed = speed;
        this.longitude = longitude;
        this.latitude = latitude;
        this.batteryLevel = batteryLevel;
        this.temperature = temperature;
        this.ignitionStatus = ignitionStatus;
        this.recordedAt = recordedAt;
        this.createdAt = LocalDateTime.now();
    }
}
