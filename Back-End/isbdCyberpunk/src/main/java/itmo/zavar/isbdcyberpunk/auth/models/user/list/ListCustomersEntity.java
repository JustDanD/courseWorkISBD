package itmo.zavar.isbdcyberpunk.auth.models.user.list;

import itmo.zavar.isbdcyberpunk.auth.models.user.info.BillingEntity;
import itmo.zavar.isbdcyberpunk.auth.models.user.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity userId;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "billing_id")
    private BillingEntity billingId;
}
