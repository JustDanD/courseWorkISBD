package itmo.zavar.isbdcyberpunk.payload.response;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.SellingPointEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GetCyberwaresResponse {
    private SellingPointEntity sellingPointEntity;
    private CyberwareEntity cyberwareEntity;
}
