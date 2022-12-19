package itmo.zavar.isbdcyberpunk.auth.models.shop.order;

import itmo.zavar.isbdcyberpunk.auth.models.cyberware.CyberwareEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "order_structure")
@Getter
@Setter
@NoArgsConstructor
public class OrderStructureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "cyberware_id")
    private CyberwareEntity cyberwareId;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "order_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private OrderEntity orderId;

}