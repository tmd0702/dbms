package Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import Utils.Utils.*;
import java.net.URLConnection;
import java.util.HashMap;
import org.json.JSONObject;
public class HTTPGetRequestTest{
    public static String getKeywordsSearchingResults() {
        String input = "an adventure of alive toys".replace(" ", "%20"), inputLine = "", scores = "";
        String url = String.format("http://103.42.57.126:8085/semantic_searching?input=%s", input);
        try {
            URL keywordsSearchingURL = new URL(url);
            URLConnection keywordsSearchingHTTP = keywordsSearchingURL.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    keywordsSearchingHTTP.getInputStream()));

            while ((inputLine = in.readLine()) != null)
                scores = inputLine;
            in.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return scores;
    }
    public static String helloWorld() {
        String inputLine = "";
        String url = String.format("http://localhost:3300/");
        try {
            URL keywordsSearchingURL = new URL(url);
            URLConnection keywordsSearchingHTTP = keywordsSearchingURL.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    keywordsSearchingHTTP.getInputStream()));

            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);

            in.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(inputLine);
        return inputLine;
    }
    public static String signIn() {
        String inputLine = "";
        String url = String.format("http://localhost:3300/auth/sign_in?username=admin1&password=sa123456");
        try {
            URL keywordsSearchingURL = new URL(url);
            URLConnection keywordsSearchingHTTP = keywordsSearchingURL.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    keywordsSearchingHTTP.getInputStream()));

            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);

            in.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(inputLine);
        return inputLine;
    }
    public static void main(String[] args) {
        signIn();
    }

}
