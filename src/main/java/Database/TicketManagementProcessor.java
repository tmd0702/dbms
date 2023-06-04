package Database;

import Utils.Response;

import java.util.HashMap;

public class TicketManagementProcessor extends Processor{
    public TicketManagementProcessor(){
        super();
        setDefaultDatabaseTable("TICKETS");
    }
    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() == 0) {
            queryCondition = "SC.SHOW_TIME_ID = ST.ID AND S.ID = T.SCHEDULE_ID AND T.SEAT_ID = S.ID AND C.ID = SR.CINEMA_ID AND SR.ID = SC.SCREEN_ROOM_ID";
        } else {
            queryCondition = queryCondition + " AND SC.SHOW_TIME_ID = ST.ID AND S.ID = T.SCHEDULE_ID AND T.SEAT_ID = S.ID AND C.ID = SR.CINEMA_ID AND SR.ID = SC.SCREEN_ROOM_ID";
        }
        Response response = select("T.ID, T.AMOUNT, SC.SHOW_DATE, ST.START_TIME, S.NAME", from ,quantity, queryCondition, sortQuery, "TICKETS T, SEATS S, SCHEDULES SC, SHOW_TIMES ST, SCREEN_ROOMS SR, CINEMAS C");
        return response;
    }
    public int countData(String queryCondition) {
        return count(queryCondition, getDefaultDatabaseTable());
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
