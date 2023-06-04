package Utils;

import Utils.StatusCode;

import java.util.ArrayList;

public class Response {
    private String message;
    private ArrayList<ArrayList<String>> data;
    private StatusCode statusCode;
    public Response() {
        message = "OK";
        statusCode = StatusCode.OK;
        data = new ArrayList<>();
    }
    public Response(String message, StatusCode statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
    public Response(String message, StatusCode statusCode, ArrayList<ArrayList<String>> data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }
    public ArrayList<ArrayList<String>> getData() {
        return this.data;
    }
    public String getMessage() {
        return this.message;
    }
    public StatusCode getStatusCode() {
        return this.statusCode;
    }
}
