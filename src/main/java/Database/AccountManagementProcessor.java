package Database;
import UserManager.Manager;
import UserManager.Customer;
import UserManager.User;
import Utils.*;
import Exception.*;
import com.example.GraphicalUserInterface.ManagementMain;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class AccountManagementProcessor extends Processor {
    public AccountManagementProcessor() {
        super();
        setDefaultDatabaseTable("USERS");
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
    public Response handleSigninAction(HashMap<String, String> signinInfo) {
        String username = signinInfo.get("username");
        String password = signinInfo.get("password");
        String query = String.format("SELECT PASS FROM AUTHENTICATION A JOIN USERS U ON A.USER_ID = U.ID WHERE U.USERNAME = '%s'", username);
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next())
            {
                String truePassword = rs.getString("pass");
                if (truePassword.equals(password)) {
                    return new Response("OK", StatusCode.OK, getData(0, -1, String.format("USERNAME = '%s'", username), "").getData());
                } else {
                    return new Response("Wrong username or password", StatusCode.BAD_REQUEST);
                }
            }
            st.close();
            return new Response("Wrong username or password", StatusCode.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e);
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }
    }
    public void addAuthentication(String id, String authId, String password) {
        try {
            Statement st = getConnector().createStatement();
            String insertPasswordQuery = String.format("INSERT INTO AUTHENTICATION(ID, USER_ID, PASS) VALUES ('%s', '%s', '%s');", authId, id, password);
            System.out.println(insertPasswordQuery);
            st.execute(insertPasswordQuery);
            System.out.println("Success");
            st.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    public int countData(String queryCondition) {
        if (queryCondition.length() == 0) {
            queryCondition = "USERS.USER_CATEGORY_ID = USER_CATEGORY.ID";
        } else {
            queryCondition = queryCondition + " AND USERS.USER_CATEGORY_ID = USER_CATEGORY.ID";
        }
        return count(queryCondition, "USERS, USER_CATEGORY");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() == 0) {
            queryCondition = "USERS.USER_CATEGORY_ID = USER_CATEGORY.ID";
        } else {
            queryCondition = queryCondition + " AND USERS.USER_CATEGORY_ID = USER_CATEGORY.ID";
        }
        Response response = select("USERS.ID AS 'USERS.ID', USERS.USERNAME AS 'USERS.USERNAME', USERS.FIRST_NAME AS 'USERS.FIRST_NAME', USERS.LAST_NAME AS 'USERS.LAST_NAME', USERS.DOB AS 'USERS.DOB', USERS.GENDER AS 'USERS.GENDER', USERS.ADDRESS AS 'USERS.ADDRESS', USERS.PHONE AS 'USERS.PHONE', USERS.EMAIL AS 'USERS.EMAIL', USERS.USER_ROLE AS 'USERS.USER_ROLE', USERS.SCORE AS 'USERS.SCORE', USER_CATEGORY.CATEGORY AS 'USER_CATEGORY.CATEGORY'", from, quantity, queryCondition, sortQuery, "USERS, USER_CATEGORY");
        return response;
    }
    public Response handleSignupAction(HashMap<String, String> signupInfo) {
        try {
            if (!Validator.validateEmail(signupInfo.get("email"))) {
                throw new InvalidEmailException("Email: " + signupInfo.get("email") + " is invalid!");
            }
            if (!Validator.validateUsername(signupInfo.get("username"))) {
                throw new InvalidUsernameException("Username: " + signupInfo.get("username") + " is invalid!");
            }
            if (!Validator.validatePassword(signupInfo.get("password"))) {
                throw new InvalidPasswordException("Password: " + signupInfo.get("password") + " is invalid!");
            }
            String id = ManagementMain.getInstance().getIdGenerator().generateId("USERS");
            String authId = ManagementMain.getInstance().getIdGenerator().generateId("AUTHENTICATION");
            String insertUserQuery = String.format("INSERT INTO USERS(ID, USERNAME, FIRST_NAME, LAST_NAME, DOB, GENDER, ADDRESS, PHONE, EMAIL, USER_ROLE, SCORE) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d);", id, signupInfo.get("username"), signupInfo.get("firstName"), signupInfo.get("lastName"), new Date(new SimpleDateFormat("yyyy-MM-dd").parse(signupInfo.get("dateOfBirth")).getTime()), signupInfo.get("gender"), signupInfo.get("address"), signupInfo.get("phone"), signupInfo.get("email"), "SUBSCRIBER", 0);
            Statement st = getConnector().createStatement();
            st.execute(insertUserQuery);
            addAuthentication(id, authId, signupInfo.get("password"));
            st.close();
            return new Response("OK", StatusCode.OK);
        } catch (InvalidEmailException e) {
            System.out.println(e);
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        } catch (InvalidUsernameException e) {
            System.out.println(e);
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        } catch (InvalidPasswordException e) {
            System.out.println(e);
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e);
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }
    }
}
