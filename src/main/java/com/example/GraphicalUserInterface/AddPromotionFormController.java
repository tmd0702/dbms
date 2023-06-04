package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPromotionFormController implements Initializable {
    private ManagementMain main;
    ArrayList<ArrayList<String>> userCategoryInfo;
    ArrayList<String> userCategoryCategories;
    @FXML
    private ComboBox userCategoryCategoryField;
    @FXML
    private TextField nameField, descriptionField, discountField;
    @FXML
    private DatePicker startDateField, endDateField;
    @FXML
    private VBox addPromotionForm;

    public AddPromotionFormController() throws Exception {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userCategoryCategoryFieldInit();
    }
    public String getUserCategoryObjectIDFromComboBox(Object value) {
        String id = null;
        for (int i=0; i<userCategoryCategories.size();++i) {
            if (userCategoryCategories.get(i).equals(value)) {
                id = Utils.getRowValueByColumnName(i + 2, "ID", userCategoryInfo);
                break;
            }
        }
        return id;
    }
    public void userCategoryCategoryFieldInit() {
        userCategoryInfo = main.getProcessorManager().getUserCategoryManagementProcessor().getData(0, -1, "", "").getData();
        userCategoryCategories = Utils.getDataValuesByColumnName(userCategoryInfo, "CATEGORY");
        userCategoryCategoryField.setItems(FXCollections.observableArrayList(userCategoryCategories));
    }
    @FXML
    public void cancelInsertBtnOnClick() {
//        DialogPane cancelConfirmation = new DialogPane();
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
        ((AnchorPane)addPromotionForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)addPromotionForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveInsertBtnOnClick() throws IOException {
        System.out.println("save");
        handleInsertRecordRequest();
        disableInsertForm();
    }
    public void handleInsertRecordRequest() {
        HashMap<String, String> promotionInfo = new HashMap<String, String>();
        promotionInfo.put("ID", main.getIdGenerator().generateId("PROMOTIONS"));
        promotionInfo.put("NAME", nameField.getText());
        promotionInfo.put("START_DATE", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(startDateField.getValue()));
        promotionInfo.put("END_DATE", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(endDateField.getValue()));
        promotionInfo.put("DESCRIPTION", descriptionField.getText());
        promotionInfo.put("DISCOUNT", discountField.getText());
        promotionInfo.put("USER_CATEGORY_ID", getUserCategoryObjectIDFromComboBox(userCategoryCategoryField.getValue()));
        JSONObject jsonData = new JSONObject();
        jsonData.put("column_value_dict", promotionInfo);
        jsonData.put("is_commit", true);
        jsonData.put("processor", main.getProcessorManager().getPromotionManagementProcessor().getDefaultDatabaseTable());
        Response response = main.getConnector().HTTPInsertDataRequest(jsonData);
//        Response response = main.getProcessorManager().getPromotionManagementProcessor().insertData(promotionInfo, true);
        StatusCode status = response.getStatusCode();

        if (status == StatusCode.OK) {
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
