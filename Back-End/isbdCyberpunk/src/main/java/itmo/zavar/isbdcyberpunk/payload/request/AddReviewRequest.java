package itmo.zavar.isbdcyberpunk.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddReviewRequest {
    private Long id;
    private Double rating;
    private String reviewText;
}
