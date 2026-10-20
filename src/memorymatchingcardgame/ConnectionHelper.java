package memorymatchingcardgame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton database connection helper MySQL via JDBC.
 */
public final class ConnectionHelper {

    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "memorycard_game";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static final String JDBC_URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";

    private static Connection connecting;

    private ConnectionHelper() {
    }

    // Getting the database connection (singleton, auto reconnect if closed)
    public static synchronized Connection getConnection() {
        try {
            if (connecting == null || connecting.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connecting = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                System.out.println("Connection Berhasil");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver was not found. Add the mysql-connector-j to Libraries.");
            e.printStackTrace();
            connecting = null;
        } catch (SQLException e) {
            System.err.println("Connection Failed: " + e.getMessage());
            e.printStackTrace();
            connecting = null;
        }
        return connecting;
    }

    // Close the database connection when exiting the application
    public static synchronized void closeConnection() {
        if (connecting != null) {
            try {
                if (!connecting.isClosed()) {
                    connecting.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connecting = null;
            }
        }
    }
}
