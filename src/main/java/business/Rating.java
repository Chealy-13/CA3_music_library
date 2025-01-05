package business;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
public class Rating {
    private int ratingId;
    private int userId;
    private int songId;
    private int rating;
}
