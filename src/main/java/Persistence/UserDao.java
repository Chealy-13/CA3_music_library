package Persistence;
/**
 * @author Damian Magiera
 * D00229247
 */
import business.User;



public interface UserDao {

    User login(String username, String password);
    boolean register(User user);
    boolean updateUser(User user); // New method

    //boolean deleteByUsername(String username);

    //boolean validateCCInfo(String cardNumber, String expiryDate, String cvv);

    //boolean isUsernameAvailable(String username);

    //boolean isEmailAvailable(String email);

    //List<User> getAllUsers();

}