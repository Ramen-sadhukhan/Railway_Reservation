import java.sql.*;

public class Database {
    public static Connection connectDB(String url, String user, String pass) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println("Database connection error: " + e);
            System.exit(1);
        }
        return null;
    }

    public static void closeDB(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Error closing connection.");
        }
    }
}
