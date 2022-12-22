package itmo.zavar.isbdcyberpunk.models.shop.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_status",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "statusName"),
        })
@Getter
@Setter
@NoArgsConstructor
public class OrderStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String statusName;

}