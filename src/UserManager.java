import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserManager {
    public static int currentUserId = -1;
    public static String currentUsername;
    public static String currentRole = "user";  

    public static void register(Connection conn, Scanner sc) {
        System.out.print("Enter new username: ");
        String username = sc.nextLine();
        System.out.print("Enter new password: ");
        String password = sc.nextLine();

        try {
            PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO users (username, password, role) VALUES (?, ?, 'user')");
            pst.setString(1, username);
            pst.setString(2, password);
            pst.executeUpdate();
            System.out.println(" User registered successfully.");
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate")) {
                System.out.println("❌ Username already exists. Try a different one.");
            } else {
                System.out.println("❌ Registration failed: " + e.getMessage());
            }
        }
    }

    public static boolean login(Connection conn, Scanner sc) {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        try {
            PreparedStatement pst = conn.prepareStatement(
                "SELECT user_id, role FROM users WHERE username = ? AND password = ?");
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                currentUserId = rs.getInt("user_id");
                currentUsername = username;
                currentRole = rs.getString("role");  
                System.out.println("✅ Login successful. Welcome, " + username + " (" + currentRole + ")");
                return true;
            } else {
                System.out.println("❌ Invalid credentials.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Login failed: " + e.getMessage());
        }

        return false;
    }
    public static void logout() {
    currentUserId = -1;
    currentUsername = null; 
    System.out.println("Logged out successfully.");
}
public static boolean isLoggedIn() {
    return currentUserId != -1;
}


    public static boolean isAdmin() {
        return currentRole.equals("admin");
    }
}


