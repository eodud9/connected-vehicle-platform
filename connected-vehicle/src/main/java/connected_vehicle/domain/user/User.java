package connected_vehicle.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name= "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected User(){}

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = UserRole.USER;
        this.createdAt = LocalDateTime.now();
    }

}
