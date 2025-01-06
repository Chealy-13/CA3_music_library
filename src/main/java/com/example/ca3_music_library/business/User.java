package com.example.ca3_music_library.business;
/**
 * @author Damian Magiera
 * D00229247
 */
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isAdmin;
    private boolean subscriptionStatus;
    private LocalDate subscriptionExpiry;
}