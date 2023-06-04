package Database;

import Utils.Response;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class FiltererProcessor extends Processor {
    public FiltererProcessor() {
        super();
    }
    public ArrayList<String> getGenres() {
        String query = "SELECT NAME FROM GENRES;";
        ArrayList<String> genres = new ArrayList<String>();
        genres.add("All genres");
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                genres.add(rs.getString("NAME"));
            }
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return genres;
    }
    public int countData(String queryCondition) {
        return count(queryCondition, getDefaultDatabaseTable());
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
    public ArrayList<String> getLanguages() {
        String query = "SELECT DISTINCT LANGUAGE FROM MOVIES;";
        ArrayList<String> languages = new ArrayList<String>();
        languages.add("All languages");
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                languages.add(rs.getString("LANGUAGE"));
            }
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return languages;
    }
    public Response updateData(HashMap<String, String> columnValueMap, String queryCondition, boolean isCommit) {
        return update(columnValueMap, queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    public Response insertData(HashMap<String, String> columnValueMap, boolean isCommit) {
        return insert(columnValueMap, getDefaultDatabaseTable(), isCommit);
    }
    public Response deleteData(String queryCondition, boolean isCommit) {
        return delete(queryCondition, getDefaultDatabaseTable(), isCommit);
    }
}
