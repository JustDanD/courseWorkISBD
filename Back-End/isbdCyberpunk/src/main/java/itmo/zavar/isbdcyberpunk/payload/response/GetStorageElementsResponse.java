package itmo.zavar.isbdcyberpunk.payload.response;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GetStorageElementsResponse {
    private Long id;
    private CyberwareEntity cyberware;
    private Long count;
    private Double rating;
    private Long price;
}
