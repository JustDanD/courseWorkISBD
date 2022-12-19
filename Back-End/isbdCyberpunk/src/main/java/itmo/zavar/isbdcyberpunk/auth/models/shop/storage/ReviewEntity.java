package itmo.zavar.isbdcyberpunk.auth.models.shop.storage;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "storage_element")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StorageElementEntity storageElement;

    @NotBlank
    private String review;

    @NotBlank
    @Min(0)
    @Max(5)
    private Double rating;
}