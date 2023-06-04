package Database;

import Utils.Response;

import java.util.HashMap;

public class ReviewManagementProcessor extends Processor{
    public ReviewManagementProcessor() {
        super();
        setDefaultDatabaseTable("REVIEW");
    }

    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() > 0) {
            queryCondition = "R.MOVIE_ID = M.ID AND R.USER_ID = U.ID AND U.USER_CATEGORY_ID = UC.ID AND " + queryCondition;
        } else {
            queryCondition = "R.MOVIE_ID = M.ID AND R.USER_ID = U.ID AND U.USER_CATEGORY_ID = UC.ID";
        }
        Response response = select("UC.CATEGORY, U.USERNAME, M.ID AS MOVIE_ID, R.RATING, R.COMMENT, R.DATE", from, quantity, queryCondition, sortQuery, "REVIEW R, USERS U, MOVIES M, USER_CATEGORY UC");
        return response;
    }
    public int countData(String queryCondition) {
        if (queryCondition.length() > 0) {
            queryCondition = "R.MOVIE_ID = M.ID AND R.USER_ID = U.ID AND U.USER_CATEGORY_ID = UC.ID AND " + queryCondition;
        } else {
            queryCondition = "R.MOVIE_ID = M.ID AND R.USER_ID = U.ID AND U.USER_CATEGORY_ID = UC.ID";
        }
        return count(queryCondition, "REVIEW R, USERS U, MOVIES M, USER_CATEGORY UC");
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
