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
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddItemFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private ComboBox categoryField;
    @FXML
    private TextField nameField, priceField;
    @FXML
    private VBox addItemForm;
    public AddItemFormController() throws Exception {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryFieldInit();
    }
    public void categoryFieldInit() {
        String movieStatus[] = {"Popcorn", "Drink"};
        categoryField.setItems(FXCollections.observableArrayList(movieStatus));
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
        ((AnchorPane)addItemForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)addItemForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveInsertBtnOnClick() throws IOException {
        System.out.println("save");
        handleInsertRecordRequest();
        disableInsertForm();
    }
    public void handleInsertRecordRequest() {
        HashMap<String, String> itemInfo = new HashMap<String, String>();
        itemInfo.put("NAME", nameField.getText());
        itemInfo.put("CATEGORY", categoryField.getValue().toString());
        itemInfo.put("PRICE", priceField.getText());
        itemInfo.put("ID", main.getIdGenerator().generateId(main.getProcessorManager().getItemManagementProcessor().getDefaultDatabaseTable()));
        JSONObject jsonData = new JSONObject();
        jsonData.put("column_value_dict", itemInfo);
        jsonData.put("is_commit", true);
        jsonData.put("processor", main.getProcessorManager().getItemManagementProcessor().getDefaultDatabaseTable());
        Response response = main.getConnector().HTTPInsertDataRequest(jsonData);
//        Response response = main.getProcessorManager().getItemManagementProcessor().insertData(itemInfo, true);
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
