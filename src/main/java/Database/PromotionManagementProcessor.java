package Database;

import Utils.Response;

import java.util.HashMap;

public class PromotionManagementProcessor extends Processor {
    public PromotionManagementProcessor() {
        super();
        setDefaultDatabaseTable("PROMOTIONS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() > 0) {
            queryCondition = queryCondition + " AND USER_CATEGORY.ID = PROMOTIONS.USER_CATEGORY_ID";
        } else {
            queryCondition = "USER_CATEGORY.ID = PROMOTIONS.USER_CATEGORY_ID";
        }
        Response response = select("PROMOTIONS.ID AS 'PROMOTIONS.ID', USER_CATEGORY.POINT_LOWERBOUND AS 'USER_CATEGORY.POINT_LOWERBOUND', PROMOTIONS.NAME AS 'PROMOTIONS.NAME', PROMOTIONS.START_DATE AS 'PROMOTIONS.START_DATE', PROMOTIONS.END_DATE AS 'PROMOTIONS.END_DATE', PROMOTIONS.DESCRIPTION AS 'PROMOTIONS.DESCRIPTION', PROMOTIONS.DISCOUNT AS 'PROMOTIONS.DISCOUNT', USER_CATEGORY.CATEGORY AS 'USER_CATEGORY.CATEGORY'", from, quantity, queryCondition, sortQuery, "PROMOTIONS, USER_CATEGORY");
        return response;
    }
    public int countData(String queryCondition) {
        if (queryCondition.length() > 0) {
            queryCondition = queryCondition + " AND USER_CATEGORY.ID = PROMOTIONS.USER_CATEGORY_ID";
        } else {
            queryCondition = "USER_CATEGORY.ID = PROMOTIONS.USER_CATEGORY_ID";
        }
        return count(queryCondition, "PROMOTIONS, USER_CATEGORY");
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
