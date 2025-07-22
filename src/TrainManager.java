

import java.sql.*;
import java.util.*;

public class TrainManager {
    public static void viewTrains(Connection conn) {
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM trains");
            System.out.println("\nAvailable Trains:");
            System.out.println("Train No\tName\t\tFrom\t\tTo\t\tSeats Left");
            while (rs.next()) {
                System.out.printf("%d\t\t%-15s%-10s%-10s%d\n",
                    rs.getInt("train_no"),
                    rs.getString("train_name"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getInt("seats_left"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching trains.");
            e.printStackTrace();
        }
    }

    public static void addTrain(Connection conn, Scanner sc) {
        sc.nextLine();
        System.out.print("Train No: ");
        int trainNo = sc.nextInt();
        sc.nextLine();
        System.out.print("Train Name: ");
        String name = sc.nextLine();
        System.out.print("Source: ");
        String src = sc.nextLine();
        System.out.print("Destination: ");
        String dest = sc.nextLine();
        System.out.print("Total Seats: ");
        int totalSeats = sc.nextInt();

        try {
            PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO trains(train_no, train_name, source, destination, total_seats, seats_left) VALUES (?, ?, ?, ?, ?, ?)"
            );
            pst.setInt(1, trainNo);
            pst.setString(2, name);
            pst.setString(3, src);
            pst.setString(4, dest);
            pst.setInt(5, totalSeats);
            pst.setInt(6, totalSeats);

            pst.executeUpdate();
            System.out.println("✅ Train added successfully.");
        } catch (SQLException e) {
            System.out.println("❌ Error adding train.");
            e.printStackTrace();
        }
    }

    public static void deleteTrain(Connection conn, Scanner sc) {
        System.out.print("Enter Train Number to delete: ");
        int trainNo = sc.nextInt();

        try {
            PreparedStatement pst = conn.prepareStatement("DELETE FROM trains WHERE train_no=?");
            pst.setInt(1, trainNo);
            int rows = pst.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Train deleted.");
            else
                System.out.println("❌ Train not found.");
        } catch (SQLException e) {
            System.out.println("❌ Error deleting train.");
            e.printStackTrace();
        }
    }
}
