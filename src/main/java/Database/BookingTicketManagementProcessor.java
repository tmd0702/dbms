package Database;

import Utils.Response;

import java.util.HashMap;

public class BookingTicketManagementProcessor extends Processor {
    public BookingTicketManagementProcessor() {
        super();
        setDefaultDatabaseTable("BOOKING_TICKETS");
    }

    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() == 0) {
            queryCondition = "SCHEDULES.SHOW_TIME_ID = SHOW_TIMES.ID AND SCHEDULES.ID = TICKETS.SCHEDULE_ID AND TICKETS.SEAT_ID = SEATS.ID AND CINEMAS.ID = SCREEN_ROOMS.CINEMA_ID AND SCREEN_ROOMS.ID = SCHEDULES.SCREEN_ROOM_ID AND MOVIES.ID = SCHEDULES.MOVIE_ID AND BOOKING_TICKETS.TICKET_ID = TICKETS.ID AND BOOKING_TICKETS.PAYMENT_ID = PAYMENTS.ID AND USERS.ID = PAYMENTS.USER_ID AND SEATS.SEAT_CATEGORY_ID = PRICES.COMPONENT_ID";
        } else {
            queryCondition = queryCondition + " AND SCHEDULES.SHOW_TIME_ID = SHOW_TIMES.ID AND SCHEDULES.ID = TICKETS.SCHEDULE_ID AND TICKETS.SEAT_ID = SEATS.ID AND CINEMAS.ID = SCREEN_ROOMS.CINEMA_ID AND SCREEN_ROOMS.ID = SCHEDULES.SCREEN_ROOM_ID AND MOVIES.ID = SCHEDULES.MOVIE_ID AND BOOKING_TICKETS.TICKET_ID = TICKETS.ID AND BOOKING_TICKETS.PAYMENT_ID = PAYMENTS.ID AND USERS.ID = PAYMENTS.USER_ID AND SEATS.SEAT_CATEGORY_ID = PRICES.COMPONENT_ID";
        }
        Response response = select("TICKETS.ID AS 'TICKETS.ID', PRICES.PRICE AS 'PRICES.PRICE', SCHEDULES.SHOW_DATE AS 'SCHEDULES.SHOW_DATE', SHOW_TIMES.START_TIME AS 'SHOW_TIMES.START_TIME', SEATS.NAME AS 'SEATS.NAME', CINEMAS.NAME AS 'CINEMAS.NAME', SCREEN_ROOMS.NAME AS 'SCREEN_ROOMS.NAME', MOVIES.TITLE AS 'MOVIES.TITLE', PAYMENTS.ID AS 'PAYMENTS.ID', USERS.USERNAME AS 'USERS.USERNAME'", from ,quantity, queryCondition, sortQuery, "TICKETS, SEATS, SCHEDULES, SHOW_TIMES, SCREEN_ROOMS, CINEMAS, MOVIES, PAYMENTS, BOOKING_TICKETS, USERS, PRICES");
        return response;
    }
    public int countData(String queryCondition) {
        if (queryCondition.length() == 0) {
            queryCondition = "SCHEDULES.SHOW_TIME_ID = SHOW_TIMES.ID AND SCHEDULES.ID = TICKETS.SCHEDULE_ID AND TICKETS.SEAT_ID = SEATS.ID AND CINEMAS.ID = SCREEN_ROOMS.CINEMA_ID AND SCREEN_ROOMS.ID = SCHEDULES.SCREEN_ROOM_ID AND MOVIES.ID = SCHEDULES.MOVIE_ID AND BOOKING_TICKETS.TICKET_ID = TICKETS.ID AND BOOKING_TICKETS.PAYMENT_ID = PAYMENTS.ID AND USERS.ID = PAYMENTS.USER_ID AND SEATS.SEAT_CATEGORY_ID = PRICES.COMPONENT_ID";
        } else {
            queryCondition = queryCondition + " AND SCHEDULES.SHOW_TIME_ID = SHOW_TIMES.ID AND SCHEDULES.ID = TICKETS.SCHEDULE_ID AND TICKETS.SEAT_ID = SEATS.ID AND CINEMAS.ID = SCREEN_ROOMS.CINEMA_ID AND SCREEN_ROOMS.ID = SCHEDULES.SCREEN_ROOM_ID AND MOVIES.ID = SCHEDULES.MOVIE_ID AND BOOKING_TICKETS.TICKET_ID = TICKETS.ID AND BOOKING_TICKETS.PAYMENT_ID = PAYMENTS.ID AND USERS.ID = PAYMENTS.USER_ID AND SEATS.SEAT_CATEGORY_ID = PRICES.COMPONENT_ID";
        }
        return count(queryCondition, "TICKETS, SEATS, SCHEDULES, SHOW_TIMES, SCREEN_ROOMS, CINEMAS, MOVIES, PAYMENTS, BOOKING_TICKETS, USERS, PRICES");
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
