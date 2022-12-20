package itmo.zavar.isbdcyberpunk.models.shop;

import itmo.zavar.isbdcyberpunk.models.shop.storage.StorageElementEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListAdminsEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "shopping_cart")
@NoArgsConstructor
@Getter
@Setter
public class ShoppingCartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ListCustomersEntity customerId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "storage_element_id")
    private StorageElementEntity storageElementEntity;

    public ShoppingCartEntity(ListCustomersEntity customerId, StorageElementEntity storageElementEntity) {
        this.customerId = customerId;
        this.storageElementEntity = storageElementEntity;
    }
}