package itmo.zavar.isbdcyberpunk.auth.models.shop;

import itmo.zavar.isbdcyberpunk.auth.models.user.list.ListAdminsEntity;
import itmo.zavar.isbdcyberpunk.auth.models.user.list.ListCustomersEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ListCustomersEntity customerId;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "cyberware_id")
    private ListAdminsEntity cyberwareId;

}