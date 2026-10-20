package memorymatchingcardgame.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import memorymatchingcardgame.ConnectionHelper;
import memorymatchingcardgame.model.User;
import memorymatchingcardgame.util.PasswordUtil;


/**
 * DAO for database operation on users table.
 */
public class UserDAO {

    // Register a new user to database
    public boolean register(String username, String plainPassword) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not empty.");
        }
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password must not empty.");
        }

        Connection conn = ConnectionHelper.getConnection();
        if (conn == null) {
            return false;
        }

        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, PasswordUtil.hash(plainPassword));
            ps.setString(3, "user"); // ✅ DEFAULT ROLE

            ps.executeUpdate();
            return true;
        }
    }

    // Login and return User object if successful
    public User login(String username, String plainPassword) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not empty.");
        }
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password must not empty.");
        }

        Connection conn = ConnectionHelper.getConnection();
        if (conn == null) {
            return null;
        }

        String sql = "SELECT id, username, password, role FROM users WHERE username = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (PasswordUtil.verify(plainPassword, storedHash)) {
                        return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                        );
                    }
                }
            }
        }
        return null;
    }

    // Check whether username has been registered
    public boolean isUsernameExists(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        Connection conn = ConnectionHelper.getConnection();
        if (conn == null) {
            return false;
        }

        String sql = "SELECT 1 FROM users WHERE username = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    public void insertUser(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, PasswordUtil.hash(password));
            ps.setString(3, role.toLowerCase());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateUser(int id, String username, String password, String role) {
        String sql = "UPDATE users SET username=?, password=?, role=? WHERE id=?";

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, PasswordUtil.hash(password));
            ps.setString(3, role.toLowerCase());
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id=?";

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();

        String sql = "SELECT * FROM users";

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));

                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
