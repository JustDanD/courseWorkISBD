package itmo.zavar.isbdcyberpunk.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCyberwareDetailsRequest {
    private Long storageElementId;
}
