package itmo.zavar.isbdcyberpunk.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class MessageResponse {
    @Setter
    @Getter
    private String message;
}
