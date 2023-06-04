package Database;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import Utils.Response;
import Utils.*;
import BookingManager.BookingInfor;
import com.example.GraphicalUserInterface.Main;

public class BookingProcessor extends Processor {
    private BookingInfor bookingInfor;
    private Main main;
    private IdGenerator idGenerator;
    public BookingProcessor() throws Exception {
        super();
        this.main = Main.getInstance();
        this.bookingInfor = new BookingInfor();
        this.idGenerator = new IdGenerator();
    }
    public int countData(String queryCondition) {
        return count(queryCondition, getDefaultDatabaseTable());
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
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
    public BookingInfor getBookingInfor() {
        return this.bookingInfor;
    }

    public String getScreen() {
        String query = String.format("SELECT SR.* FROM SCREEN_ROOMS SR, SCHEDULES SCH WHERE SCH.SCREEN_ROOM_ID = SR.ID AND SCH.MOVIE_ID = \"%s\" AND SCH.SHOW_DATE = \"%s\" AND SCH.SHOW_TIME_ID = \"%s\" AND CINEMA_ID = \"%s\";", bookingInfor.getIdMovie(), bookingInfor.getDate(), bookingInfor.getTime(), bookingInfor.getNameCinema());
        String nameScreen = null;
        try {
            Statement stmt = getConnector().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                nameScreen = rs.getString("NAME");
                bookingInfor.setScreen(rs.getString("ID"));

            }
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return nameScreen;
    }

    public ArrayList<ArrayList<String>> getSeat() {
        return main.getProcessorManager().getSeatManagementProcessor().getData(0, -1,String.format("SCREEN_ROOM_ID = \"%s\";", bookingInfor.getScreen()),"").getData();
    }

    public ArrayList<ArrayList<String>> getTimeSlotList() {
        return select("DISTINCT(ST.ID), DATE_FORMAT(START_TIME, '%H:%i') AS TIME_SLOT, SCH.ID AS SCHEDULE_ID ",0, -1, String.format("SCH.SHOW_TIME_ID = ST.ID AND SCH.SCREEN_ROOM_ID = SR.ID AND SCH.MOVIE_ID = \"%s\" AND SCH.SHOW_DATE = \"%s\" AND SR.CINEMA_ID = \"%s\" ORDER BY TIME_SLOT ASC;", bookingInfor.getIdMovie(), bookingInfor.getDate(), bookingInfor.getNameCinema()),"","SCHEDULES SCH, SHOW_TIMES ST, SCREEN_ROOMS SR").getData();
    }

    public ArrayList<ArrayList<String>> getTheaterList() {
        return select("DISTINCT(CIN.ID), CIN.NAME ", 0, -1, String.format("SCH.SCREEN_ROOM_ID = SR.ID AND SR.CINEMA_ID = CIN.ID AND MOVIE_ID = \"%s\" AND SCH.SHOW_DATE = \"%s\";", bookingInfor.getIdMovie(), bookingInfor.getDate()), "","SCHEDULES SCH, SCREEN_ROOMS SR, CINEMAS CIN " ).getData();
    }

    public ArrayList<ArrayList<String>> getPaymentMethod() {
        Response response = select("*", 0, -1, "","","PAYMENT_METHODS");
        ArrayList<ArrayList<String>> data = response.getData();
        return data;
    }
    public ArrayList<ArrayList<String>> getItems(){
        return main.getProcessorManager().getItemManagementProcessor().getData(0, -1, "", "").getData();
    }
    public String createPaymentRow(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);
        HashMap<String, String> payment = new HashMap<String, String>();
        payment.put("ID", idGenerator.generateId(main.getProcessorManager().getPaymentManagementProcessor().getDefaultDatabaseTable()));
        payment.put("USER_ID", main.getSignedInUser().getId());
        payment.put("PAYMENT_DATE", formatDateTime);
        payment.put("PAYMENT_METHOD_ID", bookingInfor.getPaymentMethodId());
        payment.put("TOTAL_AMOUNT", Integer.toString(bookingInfor.getTotal()));
        payment.put("SCHEDULE_ID", bookingInfor.getScheduleId());
        payment.put("PROMOTION_ID", bookingInfor.getPromotionCode());
        Response response = main.getProcessorManager().getPaymentManagementProcessor().insertData(payment, false);
        if (response.getStatusCode() == StatusCode.OK) {
            System.out.println("insert 1 row success" + payment.get("ID"));
        } else {
            System.out.println(" failed");
        }
        return payment.get("ID");
    }
    public ArrayList<String> createTicketRow(){
        HashMap<String, String> seatTicket = new HashMap<String, String>();
        ArrayList<String> ticketId = new ArrayList<String>();
        for(String seatid : bookingInfor.getSeats()) {
            seatTicket.put("ID", idGenerator.generateId(main.getProcessorManager().getTicketManagementProcessor().getDefaultDatabaseTable()));
            seatTicket.put("SEAT_ID", seatid);
            seatTicket.put("SCHEDULE_ID", bookingInfor.getScheduleId());
            Response response = main.getProcessorManager().getTicketManagementProcessor().insertData(seatTicket, false);
            if (response.getStatusCode() == StatusCode.OK) {
                System.out.println("insert 1 row success" + seatTicket.get("ID"));
                ticketId.add(seatTicket.get("ID"));
            } else {
                System.out.println(" failed");
            }
        }
        return ticketId;
    }
    public void createBookingTicketRow(){
        HashMap<String, String> seatBooking = new HashMap<String, String>();
        String paymentId  = createPaymentRow();
        ArrayList<String> ticketSeats = createTicketRow();
        for(String ticket : ticketSeats) {
            seatBooking.put("PAYMENT_ID", paymentId);
            seatBooking.put("TICKET_ID", ticket);
            Response response = main.getProcessorManager().getBookingTicketManagementProcessor().insertData(seatBooking, false);
            if (response.getStatusCode() == StatusCode.OK) {
                System.out.println("insert 1 row success");
            } else {
                System.out.println(" failed");
            }
        }
        if(bookingInfor.getItems().size() != 0){
            createBookingItemRow(paymentId);
        }
    }
    public void createBookingItemRow(String paymentID){
        HashMap<String, String> itemBooking = new HashMap<String, String>();
        ArrayList<ArrayList<String>> items= bookingInfor.getItems();
        for(ArrayList<String> item : items) {
            itemBooking.put("PAYMENT_ID", paymentID);
            itemBooking.put("ITEM_ID", item.get(0));
            itemBooking.put("QUANTITY", item.get(1));
            Response response = main.getProcessorManager().getBookingItemManagementProccessor().insertData(itemBooking, false);
            if (response.getStatusCode() == StatusCode.OK) {
                System.out.println("insert 1 row success" );
            } else {
                System.out.println(" failed");
            }
        }
    }




    public boolean storePaymentInfor() throws SQLException {
        for(String seatId : bookingInfor.getSeats()) {
            if (!checkStatusSeat(seatId, bookingInfor.getScheduleId())) {
                rollback();
                return false;
            }
        }
        createBookingTicketRow();
        commit();
        return true;
    }
    public boolean checkStatusSeat(String seatId, String scheduleId){
            Response response = select("*", 0, -1, String.format("SEAT_ID = '%s' AND SCHEDULE_ID = '%s'", seatId, scheduleId), "", "TICKETS");
            String status = Utils.getRowValueByColumnName(2, "ID", response.getData());
            //System.out.println(status);
            if(status != null) {
                return false;
            }
            return true;
    }
}