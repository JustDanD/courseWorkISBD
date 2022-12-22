package itmo.zavar.isbdcyberpunk.payload.response;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class CartContentResponse {
    private List<GetCyberwaresResponse> cyberwares;
    private Long price;
}
