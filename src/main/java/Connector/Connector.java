package Connector;

import Configuration.Config;
import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class Connector {
    private Config config;
    public Connector(Config config) {
        this.config = config;
    }
    public static String constructURLParamsFromJSONObject(JSONObject jsonData) {
        String urlParams = "";
        ArrayList<String> keys = new ArrayList<String>(jsonData.keySet());
        for (int i=0; i<jsonData.length();++i) {
            urlParams += keys.get(i) + "=" + jsonData.get(keys.get(i)).toString().replace("%", "%25").replace(" ", "%20");
            if (i < jsonData.length() - 1) {
                urlParams += "&";
            }
        }
        return urlParams;
    }
    public JSONObject HTTPGetRequest(String route, JSONObject jsonData) throws Exception {
        String params = constructURLParamsFromJSONObject(jsonData);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("http://%s:%s%s?%s", this.config.get("API_HOST"), this.config.get("API_PORT"), route, params)))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse;
    }
    public JSONObject HTTPPostRequest(String route, JSONObject jsonData) throws Exception {
        String body = "";
        if (jsonData.length() > 0) {
            body = jsonData.toString();
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("http://%s:%s%s",this.config.get("API_HOST"),this.config.get("API_PORT"),route)))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse;
    }
    public JSONObject HTTPPutRequest(String route, JSONObject jsonData) throws Exception {
        String body = jsonData.toString();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("http://%s:%s%s", this.config.get("API_HOST"), this.config.get("API_PORT"), route)))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse;
    }
    public JSONObject HTTPDeleteRequest(String route, JSONObject jsonData) throws Exception {
        String params = constructURLParamsFromJSONObject(jsonData);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("http://%s:%s%s?%s", this.config.get("API_HOST"), this.config.get("API_PORT"), route, params)))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse;
    }
    public HashMap<String, Double> HTTPSearchEngineRequest(JSONObject jsonData) {
        try {
            JSONObject requestResponse = HTTPGetRequest(this.config.get("API_SEARCH_ENGINE_ROUTE"), jsonData);
            return Utils.sortMapByValue(Utils.jsonToMap(requestResponse));
        } catch (Exception e){
            System.out.println(e);
            return new HashMap<String, Double>();
        }
    }

    public Response HTTPSignInRequest(JSONObject jsonData) {
        try {
            JSONObject requestResponse = HTTPPostRequest(this.config.get("API_SIGN_IN_ROUTE"), jsonData);
            return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())));
        } catch (Exception e){
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }

    }
    public Response HTTPSignUpRequest(JSONObject jsonData) {
        try {
            JSONObject requestResponse = HTTPPostRequest(this.config.get("API_SIGN_UP_ROUTE"), jsonData);
            return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())));
        } catch (Exception e){
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }
    }
    public Response HTTPInsertDataRequest(JSONObject jsonData) {
        try {
            JSONObject requestResponse = HTTPPostRequest(this.config.get("API_INSERT_DATA_ROUTE"), jsonData);
            return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())));
        } catch (Exception e){
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }
    }
    public Response HTTPUpdateDataRequest(JSONObject jsonData) {
        try {
            JSONObject requestResponse = HTTPPutRequest(this.config.get("API_UPDATE_DATA_ROUTE"), jsonData);
            return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())));
        } catch (Exception e){
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }
    }
    public Response HTTPDeleteDataRequest(JSONObject jsonData) {
        try {
            JSONObject requestResponse = HTTPDeleteRequest(this.config.get("API_DELETE_DATA_ROUTE"), jsonData);
            return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())));
        } catch (Exception e){
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }
    }
    public Response HTTPRollbackRequest() {
        try {
            JSONObject requestResponse = HTTPPostRequest(this.config.get("API_ROLLBACK_ROUTE"), new JSONObject());
            return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())));
        } catch (Exception e){
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }
    }
    public Response HTTPCommitRequest() {
        try {
            JSONObject requestResponse = HTTPPostRequest(this.config.get("API_COMMIT_ROUTE"), new JSONObject());
            return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())));
        } catch (Exception e){
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }
    }
    public ArrayList<ArrayList<String>> getDataFetcherFromJSONData(JSONObject jsonData) {
        ArrayList<ArrayList<String>> dataFetcher = new ArrayList<ArrayList<String>>();
        ArrayList<String> columnNames = Utils.convertJSONArrayToArrayList((JSONArray) jsonData.get("column_names"));
        ArrayList<String> columnTypes = Utils.convertJSONArrayToArrayList((JSONArray) jsonData.get("column_datatypes"));
        dataFetcher.add(columnNames);
        dataFetcher.add(columnTypes);
        JSONArray values = (JSONArray) jsonData.get("values");
        for (int i = 0; i < values.length(); ++i) {
            dataFetcher.add(Utils.convertJSONArrayToArrayList((JSONArray) values.get(i)));
        }
        return dataFetcher;
    }
    public Response HTTPSelectDataRequest(JSONObject jsonData) {
        try {
            JSONObject requestResponse = HTTPGetRequest(this.config.get("API_SELECT_DATA_ROUTE"), jsonData);
            return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())), getDataFetcherFromJSONData((JSONObject) requestResponse.get("data")));
        } catch (Exception e){
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }
    }
}
