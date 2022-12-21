package itmo.zavar.isbdcyberpunk.payload.response;

import itmo.zavar.isbdcyberpunk.models.shop.order.OrderStatusEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class OrderHistoryResponse {
    private Long id;
    private Date creationTime;
    private Long price;
    private OrderStatusEntity status;
    private Long customerId;
    private String customerName;
}
