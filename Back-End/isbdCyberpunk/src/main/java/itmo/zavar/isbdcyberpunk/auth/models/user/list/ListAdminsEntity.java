package itmo.zavar.isbdcyberpunk.auth.models.user.list;

import itmo.zavar.isbdcyberpunk.auth.models.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "list_admins")
@Getter
@Setter
@NoArgsConstructor
public class ListAdminsEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity userId;
}
