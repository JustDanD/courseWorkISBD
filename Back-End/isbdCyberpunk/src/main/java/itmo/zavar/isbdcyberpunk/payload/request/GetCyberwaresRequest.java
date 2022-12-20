package itmo.zavar.isbdcyberpunk.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetCyberwaresRequest {
    private Long startPosition;
    private Long size;
    private List<String> rarity;
    private List<String> type;
    private Long startPrice;
    private Long endPrice;
}
