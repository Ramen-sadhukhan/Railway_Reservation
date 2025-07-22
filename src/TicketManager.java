import java.sql.*;
import java.util.*;

public class TicketManager {
    public static void bookTicket(Connection conn, Scanner sc) {
        System.out.print("Enter Train Number to book: ");
        int trainNo = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Your Name: ");
        String name = sc.nextLine();

        try {
            conn.setAutoCommit(false);

            PreparedStatement pst = conn.prepareStatement("SELECT seats_left FROM trains WHERE train_no=? FOR UPDATE");
            pst.setInt(1, trainNo);
            ResultSet rs = pst.executeQuery();

            if (!rs.next()) {
                System.out.println("Train not found.");
                conn.rollback();
                return;
            }

            int seatsLeft = rs.getInt("seats_left");
            if (seatsLeft <= 0) {
                System.out.println("No seats available.");
                conn.rollback();
                return;
            }

            pst = conn.prepareStatement("INSERT INTO tickets(name, train_id, user_id) VALUES (?, ?, ?)");
            pst.setString(1, name);
            pst.setInt(2, trainNo);
            pst.setInt(3, UserManager.currentUserId);  
            pst.executeUpdate();

            pst = conn.prepareStatement("UPDATE trains SET seats_left = seats_left - 1 WHERE train_no=?");
            pst.setInt(1, trainNo);
            pst.executeUpdate();

            conn.commit();
            System.out.println("Ticket booked successfully!");

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) {}
            System.out.println("Error booking ticket: " + e.getMessage());
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }

    public static void cancelTicket(Connection conn, Scanner sc) {
        System.out.print("Enter Ticket ID to cancel: ");
        int tid = sc.nextInt();

        try {
            conn.setAutoCommit(false);

            PreparedStatement pst = conn.prepareStatement("SELECT train_id FROM tickets WHERE ticket_id=?");
            pst.setInt(1, tid);
            ResultSet rs = pst.executeQuery();

            if (!rs.next()) {
                System.out.println("Ticket not found.");
                conn.rollback();
                return;
            }

            int trainId = rs.getInt("train_id");

            pst = conn.prepareStatement("DELETE FROM tickets WHERE ticket_id=?");
            pst.setInt(1, tid);
            pst.executeUpdate();

            pst = conn.prepareStatement("UPDATE trains SET seats_left = seats_left + 1 WHERE train_no=?");
            pst.setInt(1, trainId);
            pst.executeUpdate();

            conn.commit();
            System.out.println("Ticket cancelled successfully!");

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) {}
            System.out.println("Error cancelling ticket: " + e.getMessage());
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }

    public static void viewAllTickets(Connection conn) {
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(
                "SELECT t.ticket_id, t.name, tr.train_name, tr.source, tr.destination, u.username " +
                "FROM tickets t " +
                "JOIN trains tr ON t.train_id = tr.train_no " +
                "LEFT JOIN users u ON t.user_id = u.user_id"
            );

            System.out.println("\nAll Booked Tickets:");
            System.out.println("Ticket ID\tUser\t\tName\t\tTrain\t\tFrom\tTo");

            while (rs.next()) {
                System.out.printf("%d\t\t%s\t\t%s\t\t%s\t%s\t%s\n",
                    rs.getInt("ticket_id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("train_name"),
                    rs.getString("source"),
                    rs.getString("destination"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching tickets.");
            e.printStackTrace();
        }
    }
}
