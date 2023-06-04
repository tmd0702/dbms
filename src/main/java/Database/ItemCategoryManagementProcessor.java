package Database;

import Utils.Response;

import java.util.HashMap;

public class ItemCategoryManagementProcessor extends Processor {
    public ItemCategoryManagementProcessor() {
        super();
        setDefaultDatabaseTable("ITEM_CATEGORY");
    }
    public Response insertData(HashMap<String, String> columnValueMap, boolean isCommit) {
        return insert(columnValueMap, getDefaultDatabaseTable(), isCommit);
    }
    public int countData(String queryCondition) {
        return count(queryCondition, getDefaultDatabaseTable());
    }
    public Response deleteData(String queryCondition, boolean isCommit) {
        return delete(queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    public Response updateData(HashMap<String, String> columnValueMap, String queryCondition, boolean isCommit) {
        return update(columnValueMap, queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        return select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
    }
}
