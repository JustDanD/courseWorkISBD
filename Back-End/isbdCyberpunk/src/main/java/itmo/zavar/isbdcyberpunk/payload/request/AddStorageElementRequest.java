package itmo.zavar.isbdcyberpunk.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddStorageElementRequest {
    private Long cyberwareId;
    private Long price;
    private Long count;
}
