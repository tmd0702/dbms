package SearchEngine;

import com.example.GraphicalUserInterface.Main;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class SearchEngine {
    private Main main;
    public SearchEngine() {
        main = Main.getInstance();

    }
    public HashMap<String, Double> getSearchResults(String input, String type) {
        input = input.replace(" ", "%20");
        String inputLine = "", scores = "";
        JSONObject jo = new JSONObject();
        String url = String.format("http://103.42.57.126:8085/%s?input=%s", type, input);
        System.out.println(url);
        try {
            URL keywordsSearchingURL = new URL(url);
            URLConnection keywordsSearchingHTTP = keywordsSearchingURL.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    keywordsSearchingHTTP.getInputStream()));

            while ((inputLine = in.readLine()) != null)
                scores = inputLine;
            System.out.println("scores" + " " + scores);
            jo = new JSONObject(scores);
            in.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return Utils.Utils.sortMapByValue(Utils.Utils.jsonToMap(jo));
    }
}
