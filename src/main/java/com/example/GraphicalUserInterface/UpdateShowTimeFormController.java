package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
import javafx.collections.FXCollections;
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
import java.util.ResourceBundle;

public class UpdateShowTimeFormController implements Initializable {
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField idField;
    @FXML
    private VBox updateShowTimeForm;
    private ManagementMain main;
    private ArrayList<ArrayList<String>> cinemaInfo, screenRoomInfo;
    private ArrayList<String> cinemaNames, screenRoomNames;
    public UpdateShowTimeFormController() {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idFieldInit();
    }
    public void idFieldInit() {
        idField.setDisable(true);
    }
    public void disableUpdateForm() {
        ((AnchorPane)updateShowTimeForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)updateShowTimeForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveUpdateBtnOnClick() throws IOException {
        handleUpdateRecordRequest();
        disableUpdateForm();
    }
    @FXML
    public void cancelUpdateBtnOnClick() {
        disableUpdateForm();
    }
    public ArrayList<String> getScreenRoomNames() {
        screenRoomNames = new ArrayList<String>();
        for (int i=2; i<screenRoomInfo.size();++i) {
            screenRoomNames.add(screenRoomInfo.get(i).get(1));
        }
        return screenRoomNames;
    }
    public ArrayList<String> getCinemaNames() {
        cinemaNames = new ArrayList<String>();
        for (int i=2; i<cinemaInfo.size();++i) {
            cinemaNames.add(cinemaInfo.get(i).get(1));
        }
        return cinemaNames;
    }
    public void handleUpdateRecordRequest() {
        HashMap<String, String> showTimeInfo = new HashMap<String, String>();
        showTimeInfo.put("START_TIME", startTimeField.getText());
        JSONObject jsonData = new JSONObject();
        jsonData.put("column_value_dict", showTimeInfo);
        jsonData.put("query_condition", String.format("ID = '%s'", idField.getText()));
        jsonData.put("is_commit", true);
        jsonData.put("processor", main.getProcessorManager().getShowTimeManagementProcessor().getDefaultDatabaseTable());
        Response response = main.getConnector().HTTPUpdateDataRequest(jsonData);
//        Response response = main.getProcessorManager().getShowTimeManagementProcessor().updateData(showTimeInfo, String.format("ID = '%s'", idField.getText()), true);
        StatusCode status = response.getStatusCode();
        if (status == StatusCode.OK) {
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Success");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText("The record has been updated successfully");
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
