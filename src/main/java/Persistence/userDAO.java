package Persistence;
/**
 * @author Damian Magiera
 * D00229247
 */
import business.user;

public interface userDAO {

    user loginUser(String username);

    boolean registerUser(String username, String password, String email);

    boolean deleteByUsername(String username);

    boolean validateCCInfo(String cardNumber, String expiryDate, String cvv);
    //List<user> getAllUsers();


}
