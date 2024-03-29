package itmo.zavar.isbdcyberpunk.models.user.info;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
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
@Table(name = "list_owned_cyberwares")
@NoArgsConstructor
@Getter
@Setter
public class ListOwnedCyberwaresEntity {
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
    @JoinColumn(name = "cyberware_id")
    private CyberwareEntity cyberwareId;

    @NotNull
    private Boolean installed;

    public ListOwnedCyberwaresEntity(ListCustomersEntity customerId, CyberwareEntity cyberwareId, Boolean installed) {
        this.customerId = customerId;
        this.cyberwareId = cyberwareId;
        this.installed = installed;
    }
}