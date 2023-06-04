package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class AddAccountFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private ComboBox genderField;
    @FXML
    private TextField firstNameField, lastNameField, emailField, usernameField, passwordField, phoneField, addressField;
    @FXML
    private DatePicker dobField;
    @FXML
    private VBox addAccountForm;

    public AddAccountFormController() throws Exception {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderInputFieldInit();
    }
    public void genderInputFieldInit() {
        String genders[] = {"M - Male", "F - Female"};
        genderField.setItems(FXCollections.observableArrayList(genders));
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
        ((AnchorPane)addAccountForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)addAccountForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveInsertBtnOnClick() throws IOException {
        System.out.println("save");
        handleInsertRecordRequest();
        disableInsertForm();
    }
    public void handleInsertRecordRequest() {
        HashMap<String, String> signUpInfo = new HashMap<String, String>();
        signUpInfo.put("firstName", firstNameField.getText());
        signUpInfo.put("lastName", lastNameField.getText());
        signUpInfo.put("email", emailField.getText());
        signUpInfo.put("phone", phoneField.getText());
        signUpInfo.put("dateOfBirth", DateTimeFormatter.ofPattern("dd-MM-yyyy").format(dobField.getValue()));
        signUpInfo.put("address", addressField.getText());
        signUpInfo.put("username", usernameField.getText());
        signUpInfo.put("password", passwordField.getText());
        signUpInfo.put("gender", genderField.getValue().toString().substring(0, 1));
        Response response = main.getProcessorManager().getAccountManagementProcessor().handleSignupAction(signUpInfo);
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

        }
    }
}
