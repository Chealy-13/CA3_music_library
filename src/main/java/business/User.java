package business;
/**
 * @author Damian Magiera
 * D00229247
 */

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private boolean isAdmin;
    private String email;
}


