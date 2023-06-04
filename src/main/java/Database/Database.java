package Database;

import com.example.GraphicalUserInterface.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static Database database;
    private Connection con;
    private String databaseUsername, databasePassword, databaseDns;

    public Database() {
        database = this;
        this.databaseUsername = "4hb_admin";
        this.databasePassword = "sa123456";
        this.databaseDns = "jdbc:mysql://127.0.0.1:3306/";
        this.connect();
    }
    public static synchronized Database getInstance() {
        if (database == null) {
            try {
                database = new Database();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return database;
    }
    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }
    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
    public void setDatabaseDns(String databaseDns) {
        this.databaseDns = databaseDns;
    }
    public String getDatabaseUsername() {
        return this.databaseUsername;
    }
    public String getDatabasePassword() {
        return this.databasePassword;
    }
    public String getDatabaseDns() {
        return this.databaseDns;
    }
    public Database(String databaseUsername, String databasePassword, String databaseDns) {
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
        this.databaseDns = databaseDns;
        this.connect();
    }
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully");
            this.con = DriverManager.getConnection(this.databaseDns, this.databaseUsername, this.databasePassword); // not the actual password
            System.out.println("Successful Connection");
            Statement st = this.con.createStatement();
            st.execute("USE movie;");
            this.con.setAutoCommit(false);
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe);
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }
    public Connection getConnection() {
        return this.con;
    }
}
