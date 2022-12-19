package itmo.zavar.isbdcyberpunk.auth.models.user.info;

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
@Table(name = "list_owned_cyberwares")
@NoArgsConstructor
@Getter
@Setter
public class ListOwnedCyberwaresEntity {
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

    @NotBlank
    private Boolean installed;

}