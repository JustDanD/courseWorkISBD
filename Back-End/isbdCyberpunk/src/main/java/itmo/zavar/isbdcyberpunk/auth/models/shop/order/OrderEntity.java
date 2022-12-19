package itmo.zavar.isbdcyberpunk.auth.models.shop.order;

import itmo.zavar.isbdcyberpunk.auth.models.user.list.ListCustomersEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table(name = "`order`")
@Getter
@Setter
@NoArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @NotBlank
    @Min(0)
    private Long price;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "status")
    private OrderStatusEntity status;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ListCustomersEntity customerId;

}