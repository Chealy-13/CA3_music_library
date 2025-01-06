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
    /**
     * Unique identifier for the user.
     */
    private int userId;
    /**
     * The username chosen by the user. This must be unique.
     */
    private String username;
    /**
     * The password for the user's account. It is stored securely.
     */
    private String password;
    /**
     * The first name of the user.
     */
    private String firstName;
    /**
     * The last name of the user.
     */
    private String lastName;
    /**
     * The email address of the user. This must be unique.
     */
    private String email;
    /**
     * Indicates whether the user has administrative privileges.
     */
    private boolean isAdmin;

    /**
     * Indicates whether the user has an active subscription.
     */
    private boolean subscriptionStatus;
    /**
     * The expiration date of the user's subscription.
     * If null, the subscription is inactive.
     */
    private LocalDate subscriptionExpiry;


}