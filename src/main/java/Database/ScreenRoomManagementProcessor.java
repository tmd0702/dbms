package Database;

import Utils.Response;

import java.util.HashMap;

public class ScreenRoomManagementProcessor extends Processor {
    public ScreenRoomManagementProcessor() {
        super();
        setDefaultDatabaseTable("SCREEN_ROOMS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() > 0) {
            queryCondition = "SCREEN_ROOMS.CINEMA_ID = CINEMAS.ID AND " + queryCondition;
        } else {
            queryCondition = "SCREEN_ROOMS.CINEMA_ID = CINEMAS.ID";
        }
        Response response = select("SCREEN_ROOMS.ID AS 'SCREEN_ROOMS.ID', SCREEN_ROOMS.NAME AS 'SCREEN_ROOMS.NAME', SCREEN_ROOMS.CAPACITY AS 'SCREEN_ROOMS.CAPACITY', CINEMAS.NAME AS 'CINEMAS.NAME'", from, quantity, queryCondition, sortQuery, String.format("%s, CINEMAS", getDefaultDatabaseTable()));
        return response;
    }
    public int countData(String queryCondition) {
        if (queryCondition.length() > 0) {
            queryCondition = "SCREEN_ROOMS.CINEMA_ID = CINEMAS.ID AND " + queryCondition;
        } else {
            queryCondition = "SCREEN_ROOMS.CINEMA_ID = CINEMAS.ID";
        }
        return count(queryCondition, String.format("%s, CINEMAS", getDefaultDatabaseTable()));
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
