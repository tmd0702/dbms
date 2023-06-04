package Database;

import Utils.Response;

import java.util.HashMap;

public class PaymentManagementProcessor extends Processor {
    public PaymentManagementProcessor() {
        super();
        setDefaultDatabaseTable("PAYMENTS");
    }

    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() == 0) {
            queryCondition = "USERS.ID = PAYMENTS.USER_ID AND PAYMENT_METHODS.ID = PAYMENTS.PAYMENT_METHOD_ID AND SCHEDULES.ID = PAYMENTS.SCHEDULE_ID AND SCREEN_ROOMS.ID = SCHEDULES.SCREEN_ROOM_ID AND CINEMAS.ID = SCREEN_ROOMS.CINEMA_ID AND SCHEDULES.MOVIE_ID = MOVIES.ID";
        } else {
            queryCondition = queryCondition + " AND USERS.ID = PAYMENTS.USER_ID AND PAYMENT_METHODS.ID = PAYMENTS.PAYMENT_METHOD_ID AND SCHEDULES.ID = PAYMENTS.SCHEDULE_ID AND SCREEN_ROOMS.ID = SCHEDULES.SCREEN_ROOM_ID AND CINEMAS.ID = SCREEN_ROOMS.CINEMA_ID AND SCHEDULES.MOVIE_ID = MOVIES.ID";
        }
        Response response = select("PAYMENTS.ID AS 'PAYMENTS.ID', PAYMENTS.PAYMENT_DATE AS 'PAYMENTS.PAYMENT_DATE', PAYMENT_METHODS.NAME AS 'PAYMENT_METHODS.NAME', PAYMENTS.TOTAL_AMOUNT AS 'PAYMENTS.TOTAL_AMOUNT', USERS.USERNAME AS 'USERS.USERNAME', MOVIES.TITLE AS 'MOVIES.TITLE', CINEMAS.NAME AS 'CINEMAS.NAME'", from, quantity, queryCondition, sortQuery, "PAYMENTS, USERS, PAYMENT_METHODS, SCHEDULES, MOVIES, SCREEN_ROOMS, CINEMAS");
        return response;
    }
    public int countData(String queryCondition) {
        if (queryCondition.length() == 0) {
            queryCondition = "USERS.ID = PAYMENTS.USER_ID AND PAYMENT_METHODS.ID = PAYMENTS.PAYMENT_METHOD_ID AND SCHEDULES.ID = PAYMENTS.SCHEDULE_ID AND SCREEN_ROOMS.ID = SCHEDULES.SCREEN_ROOM_ID AND CINEMAS.ID = SCREEN_ROOMS.CINEMA_ID AND SCHEDULES.MOVIE_ID = MOVIES.ID";
        } else {
            queryCondition = queryCondition + " AND USERS.ID = PAYMENTS.USER_ID AND PAYMENT_METHODS.ID = PAYMENTS.PAYMENT_METHOD_ID AND SCHEDULES.ID = PAYMENTS.SCHEDULE_ID AND SCREEN_ROOMS.ID = SCHEDULES.SCREEN_ROOM_ID AND CINEMAS.ID = SCREEN_ROOMS.CINEMA_ID AND SCHEDULES.MOVIE_ID = MOVIES.ID";
        }
        return count(queryCondition, "PAYMENTS, USERS, PAYMENT_METHODS, SCHEDULES, MOVIES, SCREEN_ROOMS, CINEMAS");
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
