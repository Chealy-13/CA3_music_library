package business;
/**
 * @author Damian Magiera
 * D00229247
 */

import lombok.*;
@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @EqualsAndHashCode.Include
    private int userId;
    private String username;
    @ToString.Exclude
    private String password;
    private String firstName;
    private String lastName;
    private boolean isAdmin;
    private String email;
}
