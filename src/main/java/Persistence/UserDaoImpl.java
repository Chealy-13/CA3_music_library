package Persistence;
/**
 * @author Damian Magiera
 * D00229247
 */
import business.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

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

    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET firstName = ?, lastName = ?, email = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getUsername());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            log.error("Error updating user: {}", user.getUsername(), e);
        }
        return false;
    }
}