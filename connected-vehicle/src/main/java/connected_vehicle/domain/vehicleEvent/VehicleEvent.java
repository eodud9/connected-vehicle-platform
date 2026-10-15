package connected_vehicle.domain.vehicleEvent;

import connected_vehicle.domain.drivingSession.DrivingSession;
import connected_vehicle.domain.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "vehicle_events")
public class VehicleEvent {
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
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime occurredAt;

    protected VehicleEvent(){}

    public VehicleEvent(Vehicle vehicle,
                        DrivingSession drivingSession,
                        EventType eventType,
                        String description,
                        LocalDateTime occurredAt){

        this.vehicle = vehicle;
        this.drivingSession = drivingSession;
        this.eventType = eventType;
        this.description = description;
        this.occurredAt = occurredAt;

    }
}
