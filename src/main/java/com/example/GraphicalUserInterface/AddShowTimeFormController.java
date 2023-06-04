package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddShowTimeFormController implements Initializable {
    @FXML
    private TextField startTimeField;
    @FXML
    private VBox addShowTimeForm;
    private ManagementMain main;
    private ArrayList<ArrayList<String>> cinemaInfo, screenRoomInfo;
    private ArrayList<String> cinemaNames, screenRoomNames;
    public AddShowTimeFormController() throws Exception {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public ArrayList<String> getCinemaNames() {
        cinemaNames = new ArrayList<String>();
        for (int i=2; i<cinemaInfo.size();++i) {
            cinemaNames.add(cinemaInfo.get(i).get(1));
        }
        return cinemaNames;
    }
    @FXML
    public void cancelInsertBtnOnClick() {
        System.out.println("cancel");
        cancelInsertConfirmationAlert("Are you sure to ged rid of this record?");
    }
    public void cancelInsertConfirmationAlert(String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText(contentText);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("ok");
            disableInsertForm();
        } else {
            System.out.println("cancel");
        }
    }
    public void disableInsertForm() {
        ((AnchorPane)addShowTimeForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)addShowTimeForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveInsertBtnOnClick() throws IOException {
        handleInsertRecordRequest();
        disableInsertForm();
    }
    public String getScreenRoomObjectIDFromComboBox(Object value) {
        String id = null;
        for (int i=0; i<screenRoomNames.size();++i) {
            if (screenRoomNames.get(i).equals(value)) {
                id = screenRoomInfo.get(2 + i).get(0);
                break;
            }
        }
        return id;
    }
    public String getCinemaObjectIDFromComboBox(Object value) {
        String id = null;
        for (int i=0; i<cinemaNames.size();++i) {
            if (cinemaNames.get(i) == value) {
                id = cinemaInfo.get(2 + i).get(0);
                break;
            }
        }
        return id;
    }
    public void handleInsertRecordRequest() {
        HashMap<String, String> showTimeInfo = new HashMap<String, String>();
        showTimeInfo.put("ID", main.getIdGenerator().generateId(main.getProcessorManager().getShowTimeManagementProcessor().getDefaultDatabaseTable()));
        showTimeInfo.put("START_TIME", startTimeField.getText());
        JSONObject jsonData = new JSONObject();
        jsonData.put("column_value_dict", showTimeInfo);
        jsonData.put("is_commit", true);
        jsonData.put("processor", main.getProcessorManager().getShowTimeManagementProcessor().getDefaultDatabaseTable());
        Response response = main.getConnector().HTTPInsertDataRequest(jsonData);
//        Response response = main.getProcessorManager().getShowTimeManagementProcessor().insertData(showTimeInfo, true);
        StatusCode signupStatus = response.getStatusCode();
        if (signupStatus == StatusCode.OK) {
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Success");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText("The record has been added successfully");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
        } else {
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Failed");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText(response.getMessage());
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
        }
    }
}
