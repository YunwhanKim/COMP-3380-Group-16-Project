import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class NHLDatabaseInterface {
    static Connection connection;

    public static void main(String[] args) throws Exception {
        MyDatabase db = new MyDatabase();
        runConsole(db);

        System.out.println("Exiting NHL Database Interface...");
    }

    public static void runConsole(MyDatabase db) {
        Scanner console = new Scanner(System.in);
        System.out.println("==========================================");
        System.out.println("  COMP 3380 Group 16 - NHL Database CLI   ");
        System.out.println("==========================================");
        System.out.print("Welcome! Type 'h' for help.\n");
        System.out.print("nhl_db > ");
        String line = console.nextLine();
        String[] parts;
        String arg = "";

        while (line != null && !line.equals("q")) {
            parts = line.split("\\s+");
            if (line.indexOf(" ") > 0)
                arg = line.substring(line.indexOf(" ")).trim();

            if (parts[0].equals("h")) {
                printHelp();
            } 
            else if (parts[0].equals("reset")) {
                db.resetDatabase();
            } 
            else if (parts[0].equals("q1")) {
                db.query1();
            } else if (parts[0].equals("q2")) {
                if (!arg.isEmpty()) db.query2(arg);
                else System.out.println("Require an argument (e.g., Player ID) for this command.");
            } else if (parts[0].equals("q3")) {
                db.query3();
            } else if (parts[0].equals("q4")) {
                db.query4();
            } else if (parts[0].equals("q5")) {
                db.query5();
            } else if (parts[0].equals("q6")) {
                db.query6();
            } else if (parts[0].equals("q7")) {
                db.query7();
            } else if (parts[0].equals("q8")) {
                db.query8();
            } else if (parts[0].equals("q9")) {
                db.query9();
            } else if (parts[0].equals("q10")) {
                db.query10();
            } else if (parts[0].equals("q11")) {
                db.query11();
            } else if (parts[0].equals("q12")) {
                db.query12();
            } 
            else {
                System.out.println("Unknown command. Read the help with 'h'.");
            }

            System.out.print("nhl_db > ");
            line = console.nextLine();
        }

        console.close();
    }

    private static void printHelp() {
        System.out.println("\n--- NHL Database Commands ---");
        System.out.println("h     - Get help");
        System.out.println("reset - Wipe all data and repopulate the database");
        System.out.println("q1    - [Empty] Describe Query 1 here");
        System.out.println("q2 <id> - [Empty] Describe Query 2 here (takes an argument)");
        System.out.println("q3    - [Empty] Describe Query 3 here");
        System.out.println("q4    - [Empty] Describe Query 4 here");
        System.out.println("q5    - [Empty] Describe Query 5 here");
        System.out.println("q6    - [Empty] Describe Query 6 here");
        System.out.println("q7    - [Empty] Describe Query 7 here");
        System.out.println("q8    - [Empty] Describe Query 8 here");
        System.out.println("q9    - [Empty] Describe Query 9 here");
        System.out.println("q10   - [Empty] Describe Query 10 here");
        System.out.println("q11   - [Empty] Describe Query 11 here");
        System.out.println("q12   - [Empty] Describe Query 12 here");
        System.out.println("q     - Exit the program");
        System.out.println("-----------------------------\n");
    }
}

class MyDatabase {
    private Connection connection;

    public MyDatabase() {
        try {
            String url = "jdbc:mariadb://uranium.cs.umanitoba.ca:3306/putID_db";
            String user = "ID";
            String password = "PASSWORD";

            // connection = DriverManager.getConnection(url, user, password);
            System.out.println("Successfully connected to Uranium!");
            
        } catch (Exception e) {
            System.out.println("Connection failed. Are you connected to the UM VPN?");
            e.printStackTrace(System.out);
        }
    }

    public void resetDatabase() {
        System.out.println("Wiping and repopulating database... (To be implemented)");

    }

    public void query1() {
        String sql = ""; 
        try {
            /* Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString(""));
            }
            */
            System.out.println("Executing Query 1... (To be implemented)");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void query2(String inputArg) {
        String sql = ""; 
        try {
            /*
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, inputArg);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("name"));
            }
            */
            System.out.println("Executing Query 2 with arg: " + inputArg + " ... (To be implemented)");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void query3() {
        System.out.println("Executing Query 3... (To be implemented)");
    }

    public void query4() {
        System.out.println("Executing Query 4... (To be implemented)");
    }

    public void query5() {
        System.out.println("Executing Query 5... (To be implemented)");
    }

    public void query6() {
        System.out.println("Executing Query 6... (To be implemented)");
    }

    public void query7() {
        System.out.println("Executing Query 7... (To be implemented)");
    }

    public void query8() {
        System.out.println("Executing Query 8... (To be implemented)");
    }

    public void query9() {
        System.out.println("Executing Query 9... (To be implemented)");
    }

    public void query10() {
        System.out.println("Executing Query 10... (To be implemented)");
    }

    public void query11() {
        System.out.println("Executing Query 11... (To be implemented)");
    }

    public void query12() {
        System.out.println("Executing Query 12... (To be implemented)");
    }
}
