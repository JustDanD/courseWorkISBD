package itmo.zavar.isbdcyberpunk.auth.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        })
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Getter
    @Setter
    private String username;

    @NotBlank
    @Size(max = 120)
    @Getter
    @Setter
    private String password;

    @ManyToOne
    @JoinColumn(name = "role")
    @Getter
    @Setter
    private RoleEntity role;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
