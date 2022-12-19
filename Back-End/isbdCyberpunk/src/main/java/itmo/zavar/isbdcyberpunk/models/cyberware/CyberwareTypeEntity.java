package itmo.zavar.isbdcyberpunk.models.cyberware;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cyberware_type",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "typeName"),
        })
@NoArgsConstructor
@Getter
@Setter
public class CyberwareTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String typeName;
}