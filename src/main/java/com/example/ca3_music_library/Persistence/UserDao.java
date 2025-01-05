package com.example.ca3_music_library.Persistence;
/**
 * @author Damian Magiera
 * D00229247
 */
import com.example.ca3_music_library.business.User;

import java.time.LocalDate;


public interface UserDao {

    User login(String username, String password);
    boolean register(User user);
    boolean updateUser(User user); // New method
    boolean updateSubscription(String username, boolean subscriptionStatus, LocalDate subscriptionExpiry);
}
    //boolean deleteByUsername(String username);

    //boolean validateCCInfo(String cardNumber, String expiryDate, String cvv);

    //boolean isUsernameAvailable(String username);

    //boolean isEmailAvailable(String email);

    //List<User> getAllUsers();
