package itmo.zavar.isbdcyberpunk.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserFullDetailsResponse {
    private String name;
    private Integer role;
    private Long balance;
}
