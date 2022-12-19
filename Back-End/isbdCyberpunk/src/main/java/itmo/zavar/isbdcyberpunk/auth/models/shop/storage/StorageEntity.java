package itmo.zavar.isbdcyberpunk.auth.models.shop.storage;

import itmo.zavar.isbdcyberpunk.auth.models.user.list.ListSellersEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "storage")
@Getter
@Setter
@NoArgsConstructor
public class StorageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "seller_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ListSellersEntity sellerId;

}