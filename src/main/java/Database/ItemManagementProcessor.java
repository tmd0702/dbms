package Database;

import Utils.Response;

import java.util.HashMap;

public class ItemManagementProcessor extends Processor {
    public ItemManagementProcessor() {
        super();
        setDefaultDatabaseTable("ITEMS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() > 0) {
            queryCondition = " AND ITEM_CATEGORY.ID = ITEMS.ITEM_CATEGORY_ID AND PRICES.COMPONENT_ID = ITEM_CATEGORY.ID";
        } else {
            queryCondition = "ITEM_CATEGORY.ID = ITEMS.ITEM_CATEGORY_ID AND PRICES.COMPONENT_ID = ITEM_CATEGORY.ID";
        }
        Response response = select("ITEMS.ID AS 'ITEMS.ID', ITEMS.NAME AS 'ITEMS.NAME', ITEM_CATEGORY.CATEGORY AS 'ITEM_CATEGORY.CATEGORY', PRICES.PRICE AS 'PRICES.PRICE', ITEMS.REVENUE AS 'ITEMS.REVENUE'", from, quantity, queryCondition, sortQuery, "ITEMS, ITEM_CATEGORY, PRICES");
        return response;
    }
    public int countData(String queryCondition) {
        if (queryCondition.length() > 0) {
            queryCondition = " AND ITEM_CATEGORY.ID = ITEMS.ITEM_CATEGORY_ID AND PRICES.COMPONENT_ID = ITEM_CATEGORY.ID";
        } else {
            queryCondition = "ITEM_CATEGORY.ID = ITEMS.ITEM_CATEGORY_ID AND PRICES.COMPONENT_ID = ITEM_CATEGORY.ID";
        }
        return count(queryCondition, "ITEMS, ITEM_CATEGORY, PRICES");
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
