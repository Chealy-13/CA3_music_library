package Persistence;
/**
 * @author Damian Magiera
 * D00229247
 */
import business.User;


public interface UserDao {

    //User loginUser(String username);
    User loginUser(String username, String password);

    boolean registerUser(User user);

    //boolean deleteByUsername(String username);

    //boolean validateCCInfo(String cardNumber, String expiryDate, String cvv);

    //boolean isUsernameAvailable(String username);

    //boolean isEmailAvailable(String email);

    //List<User> getAllUsers();

}