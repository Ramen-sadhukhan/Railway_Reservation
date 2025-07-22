import java.sql.*;
import java.util.*;

public class Rail {
    static final String URL = "jdbc:mysql://localhost:3306/railway";
    static final String USER = "root";
    static final String PASS = "ramen";

    static Scanner sc = new Scanner(System.in);
    static Connection conn;

    public static void main(String[] args) {
        conn = Database.connectDB(URL, USER, PASS);
        System.out.println("Welcome to Railway Reservation System!");

        while (true) {
            if (!UserManager.isLoggedIn()) {
                System.out.println("\n1. Register");
                System.out.println("2. Login");
                System.out.println("3. Admin Login");
                System.out.println("4. Exit");
                System.out.print("Choose option: ");
                
                int authChoice = sc.nextInt();
                sc.nextLine(); 

                switch (authChoice) {
                    case 1:
                        UserManager.register(conn, sc);
                        break;
                    case 2:
                        UserManager.login(conn, sc);
                        break;
                    case 3:
                        adminLogin();
                        break;
                    case 4:
                        Database.closeDB(conn);
                        System.out.println("Thank you for using the system.");
                        System.exit(0);
                    default:
                        System.out.println("Invalid option.");
                }
            } else {
                System.out.println("\nWelcome, " + UserManager.currentUsername + "!");
                System.out.println("1. View Trains");
                System.out.println("2. Book Ticket");
                System.out.println("3. Cancel Ticket");
                System.out.println("4. Logout");
                System.out.print("Choose option: ");

                int choice = sc.nextInt();
                sc.nextLine(); 

                switch (choice) {
                    case 1:
                        TrainManager.viewTrains(conn);
                        break;
                    case 2:
                        TicketManager.bookTicket(conn, sc);
                        break;
                    case 3:
                        TicketManager.cancelTicket(conn, sc);
                        break;
                    case 4:
                        UserManager.logout();
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            }
        }
    }

    static void adminLogin() {
        System.out.print("Enter admin username: ");
        String username = sc.nextLine();
        System.out.print("Enter admin password: ");
        String password = sc.nextLine();

        if (username.equals("admin") && password.equals("admin123")) {
            adminMenu();
        } else {
            System.out.println(" Invalid admin credentials.");
        }
    }

    static void adminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Add Train");
            System.out.println("2. Delete Train");
            System.out.println("3. View All Tickets");
            System.out.println("4. Back to Main Menu");
            System.out.print("Option: ");

            int opt = sc.nextInt();
            sc.nextLine(); 

            switch (opt) {
                case 1:
                    TrainManager.addTrain(conn, sc);
                    break;
                case 2:
                    TrainManager.deleteTrain(conn, sc);
                    break;
                case 3:
                    TicketManager.viewAllTickets(conn);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}


