package itmo.zavar.isbdcyberpunk.models.shop.order;

import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @NotNull
    @Min(0)
    private Long price;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "status")
    private OrderStatusEntity status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ListCustomersEntity customerId;

}