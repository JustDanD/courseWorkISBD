package itmo.zavar.isbdcyberpunk.auth.models.user.info;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "billing")
@NoArgsConstructor
@Getter
@Setter
public class BillingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Min(0)
    private Long sum;
}