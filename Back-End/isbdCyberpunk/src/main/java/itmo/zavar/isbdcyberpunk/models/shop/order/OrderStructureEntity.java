package itmo.zavar.isbdcyberpunk.models.shop.order;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cyberware_id")
    private CyberwareEntity cyberwareId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "order_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private OrderEntity orderId;

}