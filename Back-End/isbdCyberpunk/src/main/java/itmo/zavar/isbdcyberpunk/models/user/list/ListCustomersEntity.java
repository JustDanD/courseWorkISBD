package itmo.zavar.isbdcyberpunk.models.user.list;

import itmo.zavar.isbdcyberpunk.models.user.info.BillingEntity;
import itmo.zavar.isbdcyberpunk.models.user.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "list_customers")
@Getter
@Setter
@NoArgsConstructor
public class ListCustomersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity userId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "billing_id")
    private BillingEntity billingId;

    public ListCustomersEntity(UserEntity userId, BillingEntity billingId) {
        this.userId = userId;
        this.billingId = billingId;
    }
}
