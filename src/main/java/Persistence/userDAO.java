package Persistence;
/**
 * @author Damian Magiera
 * D00229247
 */
import business.user;

import java.util.List;

public interface userDAO {

    user LoginU(String username);

    boolean RegisterU(String username, String password, String email);

    boolean deleteByUsername(String username);

    boolean validateCCInfo(String cardNumber, String expiryDate, String cvv);
    //List<user> getAllUsers();


}
