package itmo.zavar.isbdcyberpunk.models.shop.storage;

import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListSellersEntity;
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
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "storage_element")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StorageElementEntity storageElement;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ListCustomersEntity listCustomersEntity;

    @NotBlank
    private String review;

    @NotNull
    @Min(0)
    @Max(5)
    private Double rating;

    public ReviewEntity(StorageElementEntity storageElement, ListCustomersEntity listCustomersEntity, String review, Double rating) {
        this.storageElement = storageElement;
        this.listCustomersEntity = listCustomersEntity;
        this.review = review;
        this.rating = rating;
    }
}