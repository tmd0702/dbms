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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;

public class UpdateItemFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private TextField idField, nameField, priceField;
    @FXML
    private ComboBox categoryField;
    @FXML
    private VBox updateItemForm;
    public UpdateItemFormController() {
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
        ((AnchorPane)updateItemForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)updateItemForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveUpdateBtnOnClick() throws IOException {
        System.out.println("save");
        handleUpdateRecordRequest();
        disableUpdateForm();
    }
    @FXML
    public void cancelUpdateBtnOnClick() {
//        DialogPane cancelConfirmation = new DialogPane();
        System.out.println("cancel");
        disableUpdateForm();
    }
    public void handleUpdateRecordRequest() {
        HashMap<String, String> itemInfo = new HashMap<String, String>();
        itemInfo.put("NAME", nameField.getText());
        itemInfo.put("CATEGORY", categoryField.getValue().toString());
        itemInfo.put("PRICE", priceField.getText());
        JSONObject jsonData = new JSONObject();
        jsonData.put("column_value_dict", itemInfo);
        jsonData.put("query_condition", String.format("ID = '%s'", idField.getText()));
        jsonData.put("is_commit", true);
        jsonData.put("processor", main.getProcessorManager().getItemManagementProcessor().getDefaultDatabaseTable());
        Response response = main.getConnector().HTTPUpdateDataRequest(jsonData);
//        Response response = main.getProcessorManager().getItemManagementProcessor().updateData(itemInfo, String.format("ID = '%s'", idField.getText()), true);
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
