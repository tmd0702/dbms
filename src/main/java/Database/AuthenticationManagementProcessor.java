package Database;

import Utils.Response;

import java.util.HashMap;

public class AuthenticationManagementProcessor extends Processor {
    public AuthenticationManagementProcessor() {
        super();
        setDefaultDatabaseTable("AUTHENTICATION");
    }
    public int countData(String queryCondition) {
        return count(queryCondition, getDefaultDatabaseTable());
    }
    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        return select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
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
