package Persistence;
/**
 * @author Damian Magiera
 * D00229247
 */
import business.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the UserDAO interface
 * to manage user records in database.
 */
@Slf4j
public class UserDaoImpl extends MySQLDao implements UserDao {
    public UserDaoImpl() {
        super();
    }

    public UserDaoImpl(Connection conn) {
        super(conn);
    }

    public UserDaoImpl(String propertiesFilename) {
        super(propertiesFilename);
    }
    /**
     * Validates information by checking the format of the details.
     */
    //private static final String CARD_NUM_PATTERN = "^(\\d{16})$";
    //private static final String EXPIRY_DATE_PATTERN = "^(0[1-9]|1[0-2])/\\d{2}$"; // MM/YY
    //private static final String CVV_PATTERN = "^\\d{3,4}$";

    /**
     * Validates credit card information based on predefined patterns.
     *
     * @param cardNumber the credit card number (16 digits).
     * @param expiryDate the expiry date in MM/YY format.
     * @param cvv        the CVV code (3 or 4 digits).
     * @return true if all inputs match the patterns; false otherwise.
     */
    //@Override
    //public boolean validateCCInfo(String cardNumber, String expiryDate, String cvv) {
    //  return cardNumber.matches(CARD_NUM_PATTERN) &&
    //        expiryDate.matches(EXPIRY_DATE_PATTERN) &&
    //      cvv.matches(CVV_PATTERN);
    //}

    /**
     * Registers a new user by inserting their username, password, and email into the "users" table.
     * Excecutes an SL INSERT to add a new record to the "users"table
     *
     * @param /password the password of the new user to be registered.
     * @param /email    the email of the new user to be registered.
     * @return {true} if the user was successfully registered,{false} if the insertion failed or an exception occurred.
     * @throws SQLException if a database access error occurs, or the SQL statement is invalid.
     * @para username the username of the new user, must be unique in the database.
     */

    @Override
    public boolean registerUser(User user) {
        if (!validateUser(user)) {
            throw new IllegalArgumentException("Username, password, and email must be supplied for registration.");
        }

        String sql = "INSERT INTO users (username, password, firstName, lastName, email, isAdmin) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection()) {
            if (conn == null) {
                log.error("Unable to establish connection to the database.");
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getFirstName());
                ps.setString(4, user.getLastName());
                ps.setString(5, user.getEmail());
                ps.setBoolean(6, user.isAdmin());

                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            log.error("Constraint violation appeared registering user: {}", user.getUsername(), e);
        } catch (SQLException e) {
            log.error("SQL exception appeared registering user: {}", user.getUsername(), e);
        }
        return false;
    }


    private boolean validateUser(User u) {
        return u != null
                && u.getUsername() != null && !u.getUsername().isBlank()
                && u.getPassword() != null && !u.getPassword().isBlank()
                && u.getEmail() != null && !u.getEmail().isBlank();
    }
}
    /**
     * Collects a user from the database by username.
     * The method gets the SQL query to select all fields from the 'users' table,
     * where it matches the username. If a match is found,
     * a User object is created and returned with the user's information. If no
     * users are found, or if an exception occurs the method will return null.
     *
     * @param username the username of the user to be retrieved from the database.
     * @return a User object if found, or if no matching user is found.
     * @throws SQLException if there is an error executing the SQL statement or retrieving the results.
     */
    //@Override
    //public User loginUser(String username) {
        //String sql = "SELECT * FROM users WHERE username = ?";
        //try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            //statement.setString(1, username);
            //try (ResultSet resultSet = statement.executeQuery()) {
                //if (resultSet.next()) {
                    //return new User(
                      //      resultSet.getInt("userId"),
                    //        resultSet.getString("username"),
                  //          resultSet.getString("password"),
                //            resultSet.getString("email")
              //      );
            //    }
          //  }
        //} catch (SQLException e) {
          //  System.err.println("Error during user login: " + e.getMessage());
        //}
      //  return null;
    //}

    /**
     * Checks if a username is available.
     *
     * @param username the username to check.
     * @return true if the username is available, false otherwise.
     */
   // public boolean isUsernameAvailable(String username) {
        //String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        //try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            //statement.setString(1, username);
            //try (ResultSet resultSet = statement.executeQuery()) {
              //  if (resultSet.next()) {
            //        return resultSet.getInt(1) == 0;
          //      }
          //  }
        //} catch (SQLException e) {
          //  System.err.println("Checking username availability: " + e.getMessage());
        //}
      //  return false;
    //}

    /**
     * Checks if an email is available.
     *
     * @param email the email to check.
     * @return true if the email is available, false otherwise.
     */
    //public boolean isEmailAvailable(String email) {
        //String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        //try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            //statement.setString(1, email);
            //try (ResultSet resultSet = statement.executeQuery()) {
            //    if (resultSet.next()) {
          //          return resultSet.getInt(1) == 0;
        //        }
      //      }
    //    } catch (SQLException e) {
      //      System.err.println("Checking email availability: " + e.getMessage());
       // }
        //return false;
    //}


    /**
     * Deletes user based on username from database
     * This method executes an SQL DELETE statement to remove a user from the
     * 'users' table.
     * If the deletion is successful, it returns true; otherwise, it returns false.
     *
     * @param username of user will be deleted
     * @return true if the user was successfully deleted, false otherwise or if an error occurred
     */

    //@Override
    //public boolean deleteByUsername(String username) {
        //Sql delete query is used to remove a user with specified username
      //  String sql = "DELETE FROM users WHERE username = ?";
        //This line is used to stop sql injection and handle parameters safely
  //      try (PreparedStatement state = getConnection().prepareStatement(sql)) {
          //Username provided as a method parameter will replace the '?'
     //       state.setString(1, username);
            //This line runs the delete operation, it returns the number of rows affected
       //     int rowChoice = state.executeUpdate();
         //   return rowChoice > 0;
        //} catch (SQLException E) {
          //  System.out.println("SQL Exception occurred when attempting to prepare SQL for execution.");
            //System.out.println("Error: " + E.getMessage());
            //E.printStackTrace();
        //}
        //return false;
    //}

    /**
     * Extracts a user object from the ResultSet.
     *
     * @param resultSet the ResultSet containing user data.
     * @return a User object populated with the data from the ResultSet.
     */
  //  private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
    //    return new User(
      //          resultSet.getInt("userId"),
        //        resultSet.getString("username"),
          //      resultSet.getString("password"),
            //    resultSet.getString("email"));
//    }

//}
/**
 * Collects all users from the 'users' table in the database.
 * The resulting list of users,
 * If no users are found, an empty list is returned.
 *
 * @return a List of {user} objects representing all users in the database.
 * Returns nothing if list is empty.
 */
//   public List<user> getAllUsers() {
// Creates a new Arraylist ro store objects
//     List<user> users = new ArrayList<>();
//   String sql = "SELECT * FROM users";
// try (PreparedStatement statement = connection.prepareStatement(sql)) {
//   try (ResultSet rs = statement.executeQuery()) {
//     while (rs.next()) {
//       user user = new user(
//             rs.getInt("userId"),
//           rs.getString("username"), rs.getString("password"),
//         rs.getString("email")
// );
//users.add(user);
//}
//} catch (SQLException e) {
//  System.out.println("SQL Exception occurred when executing SQL or processing results.");
//System.out.println("Error: " + e.getMessage());
//e.printStackTrace();
//}
//} catch (SQLException e) {
//   System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
//     System.out.println("Error: " + e.getMessage());
//       e.printStackTrace();
//     }
//   return users;
//}
//}