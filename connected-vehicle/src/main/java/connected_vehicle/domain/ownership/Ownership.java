package connected_vehicle.domain.ownership;

import connected_vehicle.domain.user.User;
import connected_vehicle.domain.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ownerships")
@Getter
public class Ownership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    protected Ownership(){}

    public Ownership(User user, Vehicle vehicle){
        this.user = user;
        this.vehicle = vehicle;
        this.startedAt = LocalDateTime.now();
    }
}
