package itmo.zavar.isbdcyberpunk.models.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@NoArgsConstructor
public class RoleEntity {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserRole name;

    public RoleEntity(UserRole name) {
        this.name = name;
    }
}
