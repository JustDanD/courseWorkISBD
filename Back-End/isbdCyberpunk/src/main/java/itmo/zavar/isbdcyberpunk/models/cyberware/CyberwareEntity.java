package itmo.zavar.isbdcyberpunk.models.cyberware;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @ManyToOne
    @JoinColumn(name = "type_id")
    private CyberwareTypeEntity type;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "rarity_id")
    private CyberwareRarityEntity rarity;
}