package Test;
import Utils.IdGenerator;
import javafx.scene.control.RadioButton;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public class HTTPPostRequestTest {
    public HTTPPostRequestTest() throws Exception {
    }
    public static void signInPostRequest() throws Exception {
        String body = "{\"username\": \"admin1\", \"password\": \"sa123456\"}";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3300/auth/sign_in"))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject jo = new JSONObject(response.body());
        System.out.println(jo.get("message"));
    }
    public static void signUpPostRequest() throws Exception {
        IdGenerator idGenerator = new IdGenerator();
        JSONObject signUpInfo = new JSONObject();
        signUpInfo.put("id", idGenerator.generateId("USERS"));
        signUpInfo.put("first_name", "DUc");
        signUpInfo.put("last_name", "Truong");
        signUpInfo.put("email", "mduc017@gmail.com");
        signUpInfo.put("phone", "0123456789");
        signUpInfo.put("dob", "2000-04-22");
        signUpInfo.put("address", "test add");
        signUpInfo.put("username", "ductruong01");
        signUpInfo.put("pass", "ducTruong808@");
        signUpInfo.put("user_role", "CUSTOMER");
        signUpInfo.put("gender", "M");
        String body = signUpInfo.toString();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3300/auth/sign_up"))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject jo = new JSONObject(response.body());
        System.out.println(jo.get("message"));
    }
    public static void main(String[] args) throws Exception {
//        signInPostRequest();
        signUpPostRequest();
    }
}
