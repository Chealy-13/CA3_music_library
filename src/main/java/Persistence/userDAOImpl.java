package Persistence;
/**
 * @author Damian Magiera
 * D00229247
 */
import business.user;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the UserDAO interface
 * to manage user records in database.
 */
public class userDAOImpl extends MySQLDao implements userDAO {
    /**
     * Validates information by checking the format of the details.
     */
    private static final String cardNum = "^(\\d{16})$";
    private static final String expireDate = "^(0[1-9]|1[0-2])/\\d{2}$"; // MM/YY
    private static final String cvv = "^\\d{3,4}$";


    /**
     * @param cardNumber the credit card number has to be exactly 16 digits.
     * @param expireD    the expiry date in a MM/YY format, from (01-12) and YY is the last two numbers of a year.
     * @param cvv1       a 3 or 4 digit security code.
     * @return {true} if all three pieces of information match the expected format. {false} otherwise
     */
    public boolean validateCCInfo(String cardNumber, String expireD, String cvv1) {
        return cardNumber.matches(cardNum) &&
                expireD.matches(expireDate) &&
                cvv1.matches(cvv);
    }

    /**
     * Constructs a UserDAOImpl with the specified database connection.
     *
     * @param con is the Connection object to connect to the database.
     */
//    public userDAOImpl(Connection con) {
//        this.connection = con;
//    }

    /**
     * Registers a new user by inserting their username, password, and email into the "users" table.
     * Excecutes an SL INSERT to add a new record to the "users"table
     * @param username the username of the new user, must be unique in the database.
     * @param password the password of the new user to be registered.
     * @param email the email of the new user to be registered.
     * @return {true} if the user was successfully registered,{false} if the insertion failed or an exception occurred.
     * @throws SQLException if a database access error occurs, or the SQL statement is invalid.
     */

    @Override
    public boolean RegisterU(String username, String password, String email) {
        //This ine is used instert row to "users" table with values: useranme, password, email.
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (PreparedStatement state = getConnection().prepareStatement(sql)) {
            // Set the first '?' placeholder in the SQL query to the user's username
            state.setString(1, username);
            // Set the second '?' placeholder in the SQL query to the user's password
            state.setString(2, password);
            // Set the third '?' placeholder in the SQL query to the user's email
            state.setString(3, email);

            return state.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        // Return false if fails or an exception occurs
        return false;
    }
    /**
     *
     * Collects a user from the database by username.
     * The method gets the SQL query to select all fields from the 'users' table,
     * where it matches the username. If a match is found,
     * a User object is created and returned with the user's information. If no
     * users are found, or if an exception occurs the method will return null.
     * @param username the username of the user to be retrieved from the database.
     * @return a User object if found, or if no matching user is found.
     * @throws SQLException if there is an error executing the SQL statement or retrieving the results.
     */
    @Override
    public user LoginU(String username) {
        //This SQL query selects all fields from the 'users' table where the username matches
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            // Sets the first parameter in the SQL query to the provided 'username'
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Creates and returns a new 'user' object with data from the result set
                    return new user(
                            rs.getInt("userId"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email")

                    );
                }
                // Catches and prints SQL exceptions related to the inner try block (ResultSet operations)
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when attempting to prepare SQL for execution.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();

            }
            // Catches and prints SQL exceptions related to the outer try block (PreparedStatement operations)
        } catch (SQLException e) {
            System.out.println("ClassNotFoundException occurred when trying to load driver: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes user based on username from database
     * This method executes an SQL DELETE statement to remove a user from the
     * 'users' table.
     * If the deletion is successful, it returns true; otherwise, it returns false.
     * @param username of user will be deleted
     * @return true if the user was successfully deleted, false otherwise or if an error occurred
     */

    @Override
    public boolean deleteByUsername(String username) {
        //Sql delete query is used to remove a user with specified username
        String sql = "DELETE FROM users WHERE username = ?";
        //This line is used to stop sql injection and handle parameters safely
        try (PreparedStatement state = getConnection().prepareStatement(sql)) {
            //Username provided as a method parameter will replace the '?'
            state.setString(1, username);
            //This line runs the delete operation, it returns the number of rows affected
            int rowChoice = state.executeUpdate();
            return rowChoice > 0;
        } catch (SQLException E) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution.");
            System.out.println("Error: " + E.getMessage());
            E.printStackTrace();
        }
        return false;

    }
}
    /**
     * Collects all users from the 'users' table in the database.
     * The resulting list of users,
     * If no users are found, an empty list is returned.
     *
     * @return a List of {user} objects representing all users in the database.
     *         Returns nothing if list is empty.
     */
   // public List<user> getAllUsers() {
     //   // Creates a new Arraylist ro store objects
       // List<user> users = new ArrayList<>();
        //String sql = "SELECT * FROM users";
        //try (PreparedStatement statement = connection.prepareStatement(sql)) {
          //  try (ResultSet rs = statement.executeQuery()) {
            //    while (rs.next()) {
              //      user user = new user(
                //            rs.getInt("userId"),
                  //          rs.getString("username"),
                    //        rs.getString("password"),
                      //      rs.getString("email")
                    //);
                    //users.add(user);
              //  }
            //} catch (SQLException e) {
                //System.out.println("SQL Exception occurred when executing SQL or processing results.");
                //System.out.println("Error: " + e.getMessage());
                //e.printStackTrace();
          //  }
        //} catch (SQLException e) {
          //  System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
            //System.out.println("Error: " + e.getMessage());
            //e.printStackTrace();
        //}
            //return users;
     //   }
    //}
