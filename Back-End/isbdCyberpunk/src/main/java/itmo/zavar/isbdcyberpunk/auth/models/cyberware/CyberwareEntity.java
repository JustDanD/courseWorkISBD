package itmo.zavar.isbdcyberpunk.auth.models.cyberware;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cyberware")
@NoArgsConstructor
@Getter
@Setter
public class CyberwareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String url;

    private String description;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "type_id")
    private CyberwareTypeEntity type;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "rarity_id")
    private CyberwareRarityEntity rarity;
}