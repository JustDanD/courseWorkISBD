package itmo.zavar.isbdcyberpunk.payload.response;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.SellingPointEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GetCyberwaresResponse {
    private Long sellingPointEntityId;
    private String sellingPointEntityName;
    private CyberwareEntity cyberwareEntity;
    private Double rating;
    private Long count;
    private Long price;
    private Long storageElementId;
}

