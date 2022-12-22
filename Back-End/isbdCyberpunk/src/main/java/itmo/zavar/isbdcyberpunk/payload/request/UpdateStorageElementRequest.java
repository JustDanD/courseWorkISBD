package itmo.zavar.isbdcyberpunk.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStorageElementRequest {
    private Long storageElementId;
    private Long count;
    private Long price;
}
