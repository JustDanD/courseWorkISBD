package itmo.zavar.isbdcyberpunk.auth.models.cyberware;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "cyberware_rarity",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name"),
                @UniqueConstraint(columnNames = "color")
        })
@Getter
@Setter
public class CyberwareRarityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Length(max = 7)
    private String color;

    @NotBlank
    @Min(0)
    private Integer priority;
}