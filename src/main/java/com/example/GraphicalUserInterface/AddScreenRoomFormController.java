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
import java.util.Optional;
import java.util.ResourceBundle;

public class AddScreenRoomFormController implements Initializable {
    private ArrayList<ArrayList<String>> cinemaInfo;
    private ArrayList<String> cinemaNames;
    @FXML
    private ComboBox cinemaNameField;
    @FXML
    private TextField nameField, capacityField;
    @FXML
    private VBox addScreenRoomForm;
    private ManagementMain main;
    public AddScreenRoomFormController() throws Exception {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cinemaNameFieldInit();
    }

    public ArrayList<String> getCinemaNames() {
        cinemaNames = new ArrayList<String>();
        for (int i=2; i<cinemaInfo.size();++i) {
            cinemaNames.add(cinemaInfo.get(i).get(1));
        }
        return cinemaNames;
    }
    public void cinemaNameFieldInit() {
        cinemaInfo = main.getProcessorManager().getCinemaManagementProcessor().getData(0, -1, "", "").getData();
        cinemaNames = getCinemaNames();
        cinemaNameField.setItems(FXCollections.observableArrayList(cinemaNames));
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
        ((AnchorPane)addScreenRoomForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)addScreenRoomForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveInsertBtnOnClick() throws IOException {
        System.out.println("save");
        handleInsertRecordRequest();
        disableInsertForm();
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
        HashMap<String, String> screenRoomInfo = new HashMap<String, String>();
        screenRoomInfo.put("ID", main.getIdGenerator().generateId(main.getProcessorManager().getScreenRoomManagementProcessor().getDefaultDatabaseTable()));
        screenRoomInfo.put("NAME", nameField.getText());
        screenRoomInfo.put("CINEMA_ID", getCinemaObjectIDFromComboBox(cinemaNameField.getValue()));
        screenRoomInfo.put("CAPACITY", capacityField.getText());
        JSONObject jsonData = new JSONObject();
        jsonData.put("column_value_dict", screenRoomInfo);
        jsonData.put("is_commit", true);
        jsonData.put("processor", main.getProcessorManager().getScreenRoomManagementProcessor().getDefaultDatabaseTable());
        Response response = main.getConnector().HTTPInsertDataRequest(jsonData);
//        Response response = main.getProcessorManager().getScreenRoomManagementProcessor().insertData(screenRoomInfo, true);
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
