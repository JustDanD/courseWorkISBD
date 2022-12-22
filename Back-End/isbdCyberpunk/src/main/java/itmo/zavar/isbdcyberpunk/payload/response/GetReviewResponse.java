package itmo.zavar.isbdcyberpunk.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewResponse {
    private Long reviewId;
    private Long authorId;
    private String author;
    private String review;
    private Double rating;
}
