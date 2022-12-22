package itmo.zavar.isbdcyberpunk.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SellerDetailsResponse {
    private String name;
    private Integer role;
    private Long balance;
    private Long storageId;
    private Long sellingPointId;
    private String sellingPointName;
}
