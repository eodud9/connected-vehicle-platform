package connected_vehicle.domain.vehicle;

import jakarta.persistence.*;
import lombok.Getter;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String vin;

    @Column(unique = true, nullable = false)
    private String licensePlate;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private Integer modelYear;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    @Column(nullable = false)
    private String apiKeyHash;

    protected Vehicle(){}

    public Vehicle(String vin, String licensePlate, String modelName, Integer modelYear, String apiKeyHash){
        this.vin = vin;
        this.licensePlate = licensePlate;
        this.modelName = modelName;
        this.modelYear = modelYear;
        this.registeredAt = LocalDateTime.now();
        this.apiKeyHash = apiKeyHash;
    }

}
