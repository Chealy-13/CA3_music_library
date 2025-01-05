package com.example.ca3_music_library.Persistence;
/**
 * @author Damian Magiera
 * D00229247
 */
import com.example.ca3_music_library.business.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDate;

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
     * Logss in the user by validating/checking the provided username and password with the database.
     *
     * @param username the username of the user trying to log in
     * @param password the password of the user trying to log in
     * @return object if the login is successful, or null if the credentials are invalid
     * @throws IllegalArgumentException if either the username or password is null or blank
     */
    @Override
    public User login(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username required to log in");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password required to log in");
        }

        Connection conn = super.getConnection();
        User result = null;
        String sql = "SELECT * FROM users where username = ? AND password COLLATE utf8mb4_bin = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = User.builder()
                            .username(rs.getString("username"))
                            .email(rs.getString("email"))
                            .firstName(rs.getString("firstName"))
                            .lastName(rs.getString("lastName"))
                            .isAdmin(rs.getBoolean("isAdmin"))
                            .build();
                }
            }
        } catch (SQLException e) {
            log.error("An error occurred when logging in user with username: " + username, e);
        }

        super.freeConnection(conn);
        return result;
    }
    /**
     * Registers a new user to the database
     * First it validates the provided user object to make sure mandatory fields are not null or blank.
     * Then it prepares and executes the SQL `INSERT` statement to save the user's details in the database.
     * lastly logs the success or failure of the operation.
     *
     * @param user The object containing the user's details to be registered.
     * Mandatory fields are as folloes username, password, and email.
     * and then optional ones are firstName and lastName.
     * @return true if the user has successfully registered, false otherwise.
     * @throws IllegalArgumentException if the provided user object fails validation.
     * @throws IllegalStateException if a database constraint is violated (duplicate username or email).
     */
    @Override
    public boolean register(User user) {
        if (!validateUser(user)) {
            throw new IllegalArgumentException("Username, password, and email must be supplied for registration.");
        }

        String sql = "INSERT INTO users (username, password, firstName, lastName, email, isAdmin) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection()) {
            if (conn == null) {
                log.error("Failed to get database connection.");
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
                if (rowsAffected > 0) {
                    log.info("User {} successfully registered.", user.getUsername());
                    return true;
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            log.error("Constraint violation when registering user: {}", user.getUsername(), e);
            throw new IllegalStateException("Username or email is already in use.");
        } catch (SQLException e) {
            log.error("SQL exception when registering user: {}", user.getUsername(), e);
        }
        return false;
    }

    private boolean validateUser(User u) {
        return u != null
                && u.getUsername() != null && !u.getUsername().isBlank()
                && u.getPassword() != null && !u.getPassword().isBlank()
                && u.getEmail() != null && !u.getEmail().isBlank();
    }
/**
 * Updates the subscription status and expiry date for a user in the database.
 * Firstly connects to the database and prepares an SQL `UPDATE` statement.
 * Then updates the `subscriptionStatus` and `subscriptionExpiry` fields for the specified username.
 * Lastly executes the SQL statement and returns whether the operation was successful.

 * @param username The username of the user whose subscription details need to be updated.
 * @param subscriptionStatus The new subscription status.
 * @param subscriptionExpiry The new subscription expiry date as a (localdate).
 * @return returns if the subscription details are successfully updated false otherwise.
 */
    @Override
    public boolean updateSubscription(String username, boolean subscriptionStatus, LocalDate subscriptionExpiry) {
        String sql = "UPDATE users SET subscriptionStatus = ?, subscriptionExpiry = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, subscriptionStatus);
            ps.setDate(2, Date.valueOf(subscriptionExpiry));
            ps.setString(3, username);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            log.error("Error updating subscription for user: {}", username, e);
        }
        return false;
    }
/**
 * The ppoint of this method is to update the user's details in the database, including their personal information and subscription details.
 * It does the following
 * First establishes a connection to the database.
 * Then executes an SQL
 * Lastly returns whether the update operation was successful.
 *
 * @param user The {@link User} object containing updated information for the user. This includes:
 *  First name, Last name, Email, Subscription status, Subscription expiry date (nullable, can be {@code null} if no expiry date is set)
 *  Username (used to identify the user in the database)
 * @return true if the user details were successfully updated in the database and false otherwise.
 */
 @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET firstName = ?, lastName = ?, email = ?, subscriptionStatus = ?, subscriptionExpiry = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setBoolean(4, user.isSubscriptionStatus());
            ps.setDate(5, user.getSubscriptionExpiry() != null ? Date.valueOf(user.getSubscriptionExpiry()) : null);
            ps.setString(6, user.getUsername());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            log.error("Error updating user: {}", user.getUsername(), e);
        }
        return false;
    }
}

//@Override
//public boolean RegisterU(String username, String password, String email) {
    //This ine is used instert row to "users" table with values: useranme, password, email.
  //  public boolean registerUser(String username, String password, String email) {
    //    String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
      //  try (PreparedStatement state = getConnection().prepareStatement(sql)) {
            // Set the first '?' placeholder in the SQL query to the user's username
        //    state.setString(1, username);
            // Set the second '?' placeholder in the SQL query to the user's password
          //  state.setString(2, password);
            // Set the third '?' placeholder in the SQL query to the user's email
            //state.setString(3, email);
            //return state.executeUpdate() > 0;
            //try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
              //  statement.setString(1, username);
                //statement.setString(2, password);
                //statement.setString(3, email);

                //return statement.executeUpdate() > 0;
            //} catch (SQLException e) {
              //  System.out.println("SQL Exception occurred when attempting to prepare SQL for execution.");
                //System.out.println("Error: " + e.getMessage());
                //e.printStackTrace();
                //System.err.println("Error during user registration: " + e.getMessage());
                //return false;
          //  }
            // Return false if fails or an exception occurs
            //return false;
        //}
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