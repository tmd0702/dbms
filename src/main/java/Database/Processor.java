package Database;

import Utils.ColumnType;
import Utils.Response;
import Utils.StatusCode;
import com.example.GraphicalUserInterface.Main;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
public abstract class Processor {
    private Database database;
    private String defaultDatabaseTable;
    public Main main;

    public Processor() {
        this.main = Main.getInstance();
        this.database = Database.getInstance();
        this.defaultDatabaseTable = "";
    }
    public Database getDatabase() {
        return this.database;
    }
    public Connection getConnector() {
        return database.getConnection();
    }

    public void setDefaultDatabaseTable(String defaultDatabaseTable) {
        this.defaultDatabaseTable = defaultDatabaseTable;
    }
    public String getDefaultDatabaseTable() {
        return this.defaultDatabaseTable;
    }
    public void rollback() throws SQLException {
        getConnector().rollback();
    }
    public void commit() throws SQLException {
        getConnector().commit();
    }
    public abstract Response insertData(HashMap<String, String> columnValueMap, boolean isCommit);
    public Response insert(HashMap <String, String> columnValueMap, String table, boolean isCommit) {
        ArrayList<ArrayList<String>> columnsValuesList = Utils.Utils.getKeysValuesFromMap(columnValueMap);

        ArrayList<String> columns = columnsValuesList.get(0);
        ArrayList<String> values = columnsValuesList.get(1);

        String insertColumns = String.join(", ", columns);
        String insertValues = "'" + String.join("', '", values) + "'";

        String query = String.format("INSERT INTO %s (%s) VALUES (%s)", table, insertColumns, insertValues);
        try {
            System.out.println(query);
            Statement st = getConnector().createStatement();
            st.execute(query);
            st.close();
            if (isCommit) commit();
            return new Response("OK", StatusCode.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }
    }
    public String constructUpdateSQLSetStatement(ArrayList<String> columns, ArrayList<String> values) {
        String setStatement = "";
        for (int i = 0 ; i < columns.size(); ++i) {
            setStatement += String.format("%s = '%s'", columns.get(i), values.get(i));
            if (i < columns.size() - 1) {
                setStatement += ", ";
            }
        }
        return setStatement;
    }
    public abstract Response updateData(HashMap<String, String> columnValueMap, String queryCondition, boolean isCommit);
    public Response update(HashMap <String, String> columnValueMap, String queryCondition, String table, boolean isCommit) {
        ArrayList<ArrayList<String>> columnsValuesList = Utils.Utils.getKeysValuesFromMap(columnValueMap);

        ArrayList<String> columns = columnsValuesList.get(0);
        ArrayList<String> values = columnsValuesList.get(1);



        String query = String.format("UPDATE %s SET %s WHERE %s", table, constructUpdateSQLSetStatement(columns, values), queryCondition);
        System.out.println(query);
        try {
            Statement st = getConnector().createStatement();
            st.execute(query);
            st.close();
            if (isCommit) commit();
            return new Response("OK", StatusCode.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }

    }
    public abstract Response deleteData(String queryCondition, boolean isCommit);
    public Response delete(String queryCondition, String table, boolean isCommit) {
        String query = String.format("DELETE FROM %s WHERE %s", table, queryCondition);
        System.out.println(query);
        try {
            Statement st = getConnector().createStatement();
            st.execute(query);
            if (isCommit) commit();
            st.close();
            return new Response("OK", StatusCode.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }
    }
    public Response select (String values, int from, int quantity, String queryCondition, String sortQuery, String table) {
        String query = String.format("SELECT %s FROM %s", values, table);
        if (queryCondition.length() > 0) {
            query = query + " WHERE " + queryCondition;
        }
        if (sortQuery.length() > 0) {
            query = query + " ORDER BY " + sortQuery;
        }
        if (quantity > -1) {
            query = query + String.format(" LIMIT %d, %d", from, quantity);
        }

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

        try {
            System.out.println(query);
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            ArrayList<String> columnNames = new ArrayList<String>();
            ArrayList<String> columnTypes = new ArrayList<String>();
            for (int i=1; i <= rsmd.getColumnCount(); ++i) {
                columnNames.add(rsmd.getColumnLabel(i));
                columnTypes.add(ColumnType.getByValue(rsmd.getColumnType(i)).getDescription());
            }
            result.add(columnNames);
            result.add(columnTypes);
            while (rs.next()) {
                ArrayList<String> val = new ArrayList<String>();

                for (String columnName : columnNames) {
                    val.add(rs.getString(columnName));
                }
                result.add(val);
            }
            st.close();
        } catch (Exception e) {
            System.out.println(e);
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }
        return new Response("OK", StatusCode.OK, result);
    }
    public abstract Response getData(int from, int quantity, String queryCondition, String sortQuery);
    public int count(String queryCondition, String table) {
        String query = String.format("SELECT COUNT(*) FROM %s", table);
        if (queryCondition.length() > 0) {
            query = query + " WHERE " + queryCondition;
        }
        int count = 0;
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                count = rs.getInt(1);
            }
            st.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return count;
    }
    public abstract int countData(String queryCondition);
    public ArrayList<String> normColumnNames(ArrayList<String> columnNames, String tableName) {
        for (int i=0;i<columnNames.size();++i) {
            columnNames.set(i, tableName.substring(0, tableName.length()) + "_" + columnNames.get(i));

        }
        return columnNames;
    }
}
