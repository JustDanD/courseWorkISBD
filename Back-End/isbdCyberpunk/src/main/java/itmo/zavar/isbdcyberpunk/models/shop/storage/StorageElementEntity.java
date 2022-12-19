package itmo.zavar.isbdcyberpunk.models.shop.storage;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "storage_element")
@Getter
@Setter
@NoArgsConstructor
public class StorageElementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "storage_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StorageEntity storageId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cyberware_id")
    private CyberwareEntity cyberwareId;

    @NotNull
    @Min(0)
    private Long count;

    @NotNull
    @Min(0)
    @Max(5)
    private Double rating;

    @NotNull
    @Min(0)
    private Long price;
}