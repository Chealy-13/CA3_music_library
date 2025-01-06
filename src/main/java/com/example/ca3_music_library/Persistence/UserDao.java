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
   boolean updateSubscription(String username, boolean subscriptionStatus, LocalDate subscriptionExpiry);
    boolean updateUser(User user); // General user update method
}
    //boolean deleteByUsername(String username);

    //boolean validateCCInfo(String cardNumber, String expiryDate, String cvv);

    //boolean isUsernameAvailable(String username);

    //boolean isEmailAvailable(String email);

    //List<User> getAllUsers();
