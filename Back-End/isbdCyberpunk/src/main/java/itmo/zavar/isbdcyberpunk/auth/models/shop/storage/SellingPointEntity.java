package itmo.zavar.isbdcyberpunk.auth.models.shop.storage;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "selling_point")
@Getter
@Setter
@NoArgsConstructor
public class SellingPointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
}