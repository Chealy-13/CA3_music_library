package business;
/**
 * @author Damian Magiera
 * D00229247
 */
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @EqualsAndHashCode.Include
    private String username;
    @ToString.Exclude
    private String password;
    private String firstName;
    private String lastName;
    private boolean isAdmin;
    private String email;
    private boolean subscriptionStatus; // To track if the subscription is active
    private LocalDate subscriptionExpiry; // To track when the subscription expires
}
