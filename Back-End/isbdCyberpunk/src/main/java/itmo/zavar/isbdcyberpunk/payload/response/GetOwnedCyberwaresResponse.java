package itmo.zavar.isbdcyberpunk.payload.response;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GetOwnedCyberwaresResponse {
    private Long sellingPointEntityId;
    private String sellingPointEntityName;
    private CyberwareEntity cyberwareEntity;
    private Double rating;
    private Long count;
    private Long price;
    private Long storageElementId;
    private Boolean installed;
}
