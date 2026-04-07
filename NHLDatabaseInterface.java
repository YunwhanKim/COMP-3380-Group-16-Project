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
            arg = "";
        }

        console.close();
    }

    private static void printHelp() {
        System.out.println("\n--- NHL Database Commands ---");
        System.out.println("h     - Get help");
        System.out.println("reset - Wipe all data and repopulate the database");
        System.out.println("q1    - Top 15 goal-scorers of the 2019-20 season");
        System.out.println("q2 <id> - Player lookup by name (partial) or numeric player ID");
        System.out.println("q3    - Multi-goal game specialists (2+ goals in one game)");
        System.out.println("q4    - Best two-way forwards (composite offense + defense score)");
        System.out.println("q5    - Goalies who outperformed league average save percentage");
        System.out.println("q6    - Goalies with most 'stolen' wins (won despite high shots faced)");
        System.out.println("q7    - Best forward lines by goals scored (min 150 min ice time)");
        System.out.println("q8    - Most-used lines in close games (final margin <= 1 goal)");
        System.out.println("q9    - Strictest and most lenient referees by avg PIM per game");
        System.out.println("q10   - Referee home-ice bias (home vs away PIM differential)");
        System.out.println("q11   - Top 10 most chaotic games by total play events");
        System.out.println("q12   - Top scorer (points) per team in the 2019-20 season");
        System.out.println("q     - Exit the program");
        System.out.println("-----------------------------\n");
    }
}

class MyDatabase {
    private Connection connection;

    public MyDatabase() {
        try {
            String url = "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;" +
                         "databaseName=cs338016;" +
                         "encrypt=true;" +
                         "trustServerCertificate=true;";
            String user = "kimy10";
            String password = "7900706";

            this.connection = DriverManager.getConnection(url, user, password);
            
            if (this.connection != null) {
                System.out.println("Successfully connected to Uranium (cs338016)!");
            }
            
        } catch (Exception e) {
            System.out.println("Connection failed!");
            System.out.println("Checklist: 1. VPN connected? 2. Credentials correct? 3. Driver added?");
            e.printStackTrace();
        }
    }

    public void resetDatabase() {
        System.out.println("Wiping and repopulating database...");
        // TODO: 
        System.out.println("Done.");
    }

    // -----------------------------------------------------------------------
    // Q1 - Top 15 goal-scorers of the 2019-20 season
    // FIX: CAST(... AS UNSIGNED) -> CAST(... AS BIGINT)  [UNSIGNED does not exist in SQL Server]
    //      GROUP BY must include all non-aggregate SELECT columns
    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    // Q1 - Top 15 goal-scorers of the 2019-20 season (Team 출력 제외 버전)
    // -----------------------------------------------------------------------
    public void query1() {
        String sql =
            "SELECT TOP 15 p.first, p.last, " +
            "       SUM(ss.evenGoals + ss.shortHandedGoals + ss.powerPlayGoals) AS goals, " +
            "       SUM(ss.assists) AS assists, " +
            "       SUM(ss.evenGoals + ss.shortHandedGoals + ss.powerPlayGoals + ss.assists) AS points, " +
            "       COUNT(DISTINCT ss.gameID) AS gp " +
            "FROM Skater_Stats ss " +
            "JOIN Players p ON ss.playerID = p.playerID " +
            "WHERE CAST(ss.gameID / 1000000 AS BIGINT) = 2019 " +
            "GROUP BY ss.playerID, p.first, p.last " +
            "ORDER BY goals DESC";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            System.out.println("\n=== Q1: Top 15 Goal-Scorers - 2019-20 Season ===");
            System.out.printf("%-4s %-22s %5s %5s %6s %4s%n",
                              "#", "Player", "G", "A", "Pts", "GP");
            System.out.println("-".repeat(48));
            int rank = 1;
            while (rs.next()) {
                System.out.printf("%-4d %-22s %5d %5d %6d %4d%n",
                    rank++,
                    rs.getString("first") + " " + rs.getString("last"),
                    rs.getInt("goals"),
                    rs.getInt("assists"),
                    rs.getInt("points"),
                    rs.getInt("gp"));
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    // -----------------------------------------------------------------------
    // Q2 - Player lookup by name (partial match) or numeric player ID
    // FIX: GROUP BY must include all non-aggregate SELECT columns (SQL Server strict mode)
    // -----------------------------------------------------------------------
    public void query2(String inputArg) {
        boolean isId = inputArg.matches("\\d+");
        String sql;
        if (isId) {
            sql = "SELECT p.playerID, p.first, p.last, p.position, p.birthCountry, " +
                  "       p.birthCity, p.birthDate, p.heightCM, p.weight, " +
                  "       SUM(ss.evenGoals + ss.shortHandedGoals + ss.powerPlayGoals) AS career_goals, " +
                  "       SUM(ss.assists) AS career_assists, " +
                  "       COUNT(DISTINCT ss.gameID) AS career_gp " +
                  "FROM Players p " +
                  "LEFT JOIN Skater_Stats ss ON p.playerID = ss.playerID " +
                  "WHERE p.playerID = ? " +
                  "GROUP BY p.playerID, p.first, p.last, p.position, p.birthCountry, " +
                  "         p.birthCity, p.birthDate, p.heightCM, p.weight";
        } else {
            sql = "SELECT TOP 10 p.playerID, p.first, p.last, p.position, p.birthCountry, " +
                  "       p.birthCity, p.birthDate, p.heightCM, p.weight, " +
                  "       SUM(ss.evenGoals + ss.shortHandedGoals + ss.powerPlayGoals) AS career_goals, " +
                  "       SUM(ss.assists) AS career_assists, " +
                  "       COUNT(DISTINCT ss.gameID) AS career_gp " +
                  "FROM Players p " +
                  "LEFT JOIN Skater_Stats ss ON p.playerID = ss.playerID " +
                  "WHERE CONCAT(p.first, ' ', p.last) LIKE ? " +
                  "GROUP BY p.playerID, p.first, p.last, p.position, p.birthCountry, " +
                  "         p.birthCity, p.birthDate, p.heightCM, p.weight " +
                  "ORDER BY p.last";
        }
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            if (isId) ps.setInt(1, Integer.parseInt(inputArg));
            else      ps.setString(1, "%" + inputArg + "%");
            ResultSet rs = ps.executeQuery();
            System.out.println("\n=== Q2: Player Lookup - \"" + inputArg + "\" ===");
            System.out.printf("%-10s %-22s %-4s %-12s %-18s %-12s %7s %7s %4s %4s %4s%n",
                              "ID", "Name", "Pos", "Country", "City", "Birthdate",
                              "Ht(cm)", "Wt(lb)", "GP", "G", "A");
            System.out.println("-".repeat(110));
            boolean found = false;
            while (rs.next()) {
                found = true;
                String city      = rs.getString("birthCity");
                String birthdate = rs.getString("birthDate");
                System.out.printf("%-10d %-22s %-4s %-12s %-18s %-12s %7.1f %7d %4d %4d %4d%n",
                    rs.getLong("playerID"),
                    rs.getString("first") + " " + rs.getString("last"),
                    rs.getString("position"),
                    rs.getString("birthCountry"),
                    city      != null ? city      : "-",
                    birthdate != null ? birthdate.substring(0, 10) : "-",
                    rs.getDouble("heightCM"),
                    rs.getInt("weight"),
                    rs.getInt("career_gp"),
                    rs.getInt("career_goals"),
                    rs.getInt("career_assists"));
            }
            if (!found) System.out.println("  No players found matching \"" + inputArg + "\".");
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    // -----------------------------------------------------------------------
    // Q3 - Multi-goal game specialists (2+ goals in one game)
    // FIX: GROUP BY must include all non-aggregate SELECT columns
    // -----------------------------------------------------------------------
    public void query3() {
        String sql =
            "SELECT TOP 15 p.first, p.last, " +
            "       COUNT(*) AS multi_goal_games, " +
            "       SUM(ss.evenGoals + ss.shortHandedGoals + ss.powerPlayGoals) AS goals_in_those_games " +
            "FROM Skater_Stats ss " +
            "JOIN Players p ON ss.playerID = p.playerID " +
            "WHERE (ss.evenGoals + ss.shortHandedGoals + ss.powerPlayGoals) >= 2 " +
            "GROUP BY ss.playerID, p.first, p.last " +
            "ORDER BY multi_goal_games DESC";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            System.out.println("\n=== Q3: Multi-Goal Game Specialists (2+ goals in one game) ===");
            System.out.printf("%-4s %-22s %16s %22s%n",
                              "#", "Player", "Multi-Goal Games", "Goals in Those Games");
            System.out.println("-".repeat(66));
            int rank = 1;
            while (rs.next()) {
                System.out.printf("%-4d %-22s %16d %22d%n",
                    rank++,
                    rs.getString("first") + " " + rs.getString("last"),
                    rs.getInt("multi_goal_games"),
                    rs.getInt("goals_in_those_games"));
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    // -----------------------------------------------------------------------
    // Q4 - Best two-way forwards (composite score: G + A + Tkw + Blk)
    // FIX: GROUP BY must include all non-aggregate SELECT columns
    // -----------------------------------------------------------------------
    public void query4() {
        String sql =
            "SELECT TOP 15 p.first, p.last, p.position, " +
            "       SUM(ss.evenGoals + ss.shortHandedGoals + ss.powerPlayGoals) AS goals, " +
            "       SUM(ss.assists)   AS assists, " +
            "       SUM(ss.takeaways) AS tkw, " +
            "       SUM(ss.blocked)   AS blk, " +
            "       SUM(ss.evenGoals + ss.shortHandedGoals + ss.powerPlayGoals " +
            "           + ss.assists + ss.takeaways + ss.blocked) AS composite " +
            "FROM Skater_Stats ss " +
            "JOIN Players p ON ss.playerID = p.playerID " +
            "WHERE p.position IN ('C', 'LW', 'RW') " +
            "GROUP BY ss.playerID, p.first, p.last, p.position " +
            "ORDER BY composite DESC";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            System.out.println("\n=== Q4: Best Two-Way Forwards (Score = G + A + Takeaways + Blocked) ===");
            System.out.printf("%-4s %-22s %-4s %6s %6s %6s %6s %10s%n",
                              "#", "Player", "Pos", "G", "A", "Tkw", "Blk", "Composite");
            System.out.println("-".repeat(68));
            int rank = 1;
            while (rs.next()) {
                System.out.printf("%-4d %-22s %-4s %6d %6d %6d %6d %10d%n",
                    rank++,
                    rs.getString("first") + " " + rs.getString("last"),
                    rs.getString("position"),
                    rs.getInt("goals"),
                    rs.getInt("assists"),
                    rs.getInt("tkw"),
                    rs.getInt("blk"),
                    rs.getInt("composite"));
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    // -----------------------------------------------------------------------
    // Q5 - Goalies who outperformed league average save percentage
    // FIX: HAVING alias 'gp' -> HAVING COUNT(DISTINCT gs.gameID)  [SQL Server does not allow aliases in HAVING]
    //      GROUP BY must include all non-aggregate SELECT columns
    // -----------------------------------------------------------------------
    public void query5() {
        String sql =
            "SELECT TOP 15 p.first, p.last, " +
            "       COUNT(DISTINCT gs.gameID) AS gp, " +
            "       SUM(gs.shots) AS shots_faced, " +
            "       SUM(gs.saves) AS saves, " +
            "       ROUND(100.0 * SUM(gs.saves) / NULLIF(SUM(gs.shots), 0), 2) AS save_pct, " +
            "       ROUND((100.0 * SUM(gs.saves) / NULLIF(SUM(gs.shots), 0)) " +
            "           - (SELECT 100.0 * SUM(s2.saves) / NULLIF(SUM(s2.shots), 0) " +
            "              FROM Goalie_Stats s2), 2) AS above_avg " +
            "FROM Goalie_Stats gs " +
            "JOIN Players p ON gs.playerID = p.playerID " +
            "GROUP BY gs.playerID, p.first, p.last " +
            "HAVING COUNT(DISTINCT gs.gameID) >= 10 " +
            "ORDER BY above_avg DESC";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            System.out.println("\n=== Q5: Goalies Outperforming League Average Save% (min 10 GP) ===");
            System.out.printf("%-4s %-22s %4s %12s %7s %9s %11s%n",
                              "#", "Goalie", "GP", "Shots Faced", "Saves", "Save%", "+/- Avg%");
            System.out.println("-".repeat(72));
            int rank = 1;
            while (rs.next()) {
                System.out.printf("%-4d %-22s %4d %12d %7d %9.2f %11.2f%n",
                    rank++,
                    rs.getString("first") + " " + rs.getString("last"),
                    rs.getInt("gp"),
                    rs.getInt("shots_faced"),
                    rs.getInt("saves"),
                    rs.getDouble("save_pct"),
                    rs.getDouble("above_avg"));
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
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
