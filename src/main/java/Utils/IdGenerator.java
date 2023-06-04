package Utils;
import Database.Database;
import com.example.GraphicalUserInterface.Main;

import java.net.URL;
import java.sql.*;
import java.util.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class IdGenerator {
    private HashMap<String, Integer> sequence;
    private Database database;
    private Properties config;
    private Connection con;
    public IdGenerator() throws Exception {
        this.config = new Properties();
        String fileName = Main.class.getResource("/config").getPath() + "development-duc.properties";
        InputStream is = new FileInputStream(fileName);
        this.config.load(is);

        this.sequence = new HashMap<String, Integer>();
        for (String table : config.getProperty("TABLES_WITH_ID").split(",")) {
            this.sequence.put(table, -1);
        }
        this.database = Database.getInstance();
        this.con = this.database.getConnection();
    }
    public String generateIdFormat(String table) {
        String idFormat = "";
        if (table.contains("_")) {
            String[] splitted = table.split("_");
            for (String element : splitted) {
                idFormat = idFormat + element.substring(0, 1);
            }
        } else {
            idFormat = table.substring(0, Math.min(3, table.length()));
        }
        return idFormat;
    }
    public IdGenerator(HashMap<String, Integer> sequence, Database database) {
        this.sequence = sequence;
        this.database = database;
        this.con = this.database.getConnection();
    }
    public String generateId(String table) {
        String id = "";
        if (this.sequence.get(table) == -1) {
            String query = String.format("SELECT COUNT(ID) AS total FROM %s", table);
            try {
                Statement st = this.con.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) this.sequence.put(table, rs.getInt("total"));
                st.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        this.sequence.put(table, this.sequence.get(table) + 1);
        id = generateIdFormat(table) + "_" + String.format("%05d", this.sequence.get(table));
        System.out.println(id);
        return id;
    }

}
