package Database;

import Utils.Response;

import java.util.HashMap;

public class BookingItemManagementProcessor extends Processor{
    public BookingItemManagementProcessor() {
        setDefaultDatabaseTable("BOOKING_ITEMS");
    }

    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() == 0) {
            queryCondition = "BOOKING_ITEMS.ITEM_ID = ITEMS.ID AND BOOKING_ITEMS.PAYMENT_ID = PAYMENTS.ID AND PAYMENTS.USER_ID = USERS.ID AND ITEMS.ITEM_CATEGORY_ID = PRICES.COMPONENT_ID AND ITEM_CATEGORY.ID = ITEMS.ITEM_CATEGORY_ID";
        } else {
            queryCondition = queryCondition + " AND BOOKING_ITEMS.ITEM_ID = ITEMS.ID AND BOOKING_ITEMS.PAYMENT_ID = PAYMENTS.ID AND PAYMENTS.USER_ID = USERS.ID AND ITEMS.ITEM_CATEGORY_ID = PRICES.COMPONENT_ID AND ITEM_CATEGORY.ID = ITEMS.ITEM_CATEGORY_ID";
        }
        Response response = select("ITEMS.ID AS 'ITEMS.ID', ITEMS.NAME AS 'ITEMS.NAME', ITEM_CATEGORY.CATEGORY AS 'ITEM_CATEGORY.CATEGORY', PRICES.PRICE AS 'PRICES.PRICE', BOOKING_ITEMS.QUANTITY AS 'BOOKING_ITEMS.QUANTITY', PAYMENTS.ID AS 'PAYMENTS.ID', USERS.USERNAME AS 'USERS.USERNAME'", from, quantity, queryCondition, sortQuery, "BOOKING_ITEMS, ITEMS, PAYMENTS, USERS, PRICES, ITEM_CATEGORY");
        return response;
    }
    public int countData(String queryCondition) {
        if (queryCondition.length() == 0) {
            queryCondition = "BOOKING_ITEMS.ITEM_ID = ITEMS.ID AND BOOKING_ITEMS.PAYMENT_ID = PAYMENTS.ID AND PAYMENTS.USER_ID = USERS.ID AND ITEMS.ITEM_CATEGORY_ID = PRICES.COMPONENT_ID AND ITEM_CATEGORY.ID = ITEMS.ITEM_CATEGORY_ID";
        } else {
            queryCondition = queryCondition + " AND BOOKING_ITEMS.ITEM_ID = ITEMS.ID AND BOOKING_ITEMS.PAYMENT_ID = PAYMENTS.ID AND PAYMENTS.USER_ID = USERS.ID AND ITEMS.ITEM_CATEGORY_ID = PRICES.COMPONENT_ID AND ITEM_CATEGORY.ID = ITEMS.ITEM_CATEGORY_ID";
        }
        return count(queryCondition, "BOOKING_ITEMS, ITEMS, PAYMENTS, USERS, PRICES, ITEM_CATEGORY");
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
