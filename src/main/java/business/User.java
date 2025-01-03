package business;
/**
 * @author Damian Magiera
 * D00229247
 */

import lombok.*;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
@ToString
@EqualsAndHashCode
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
}


