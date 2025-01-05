package com.example.ca3_music_library.business;
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
    private boolean subscriptionStatus;
    private LocalDate subscriptionExpiry;
}
