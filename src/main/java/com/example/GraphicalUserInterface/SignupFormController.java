package com.example.GraphicalUserInterface;
import Utils.Response;
import Utils.StatusCode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.json.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.*;
public class SignupFormController {
    private Main main;
    @FXML
    ToggleGroup gender;
    @FXML
    private AnchorPane signupFormRoot;
    @FXML
    private TextField firstNameField, lastNameField, emailField, usernameField, passwordField, phoneField, addressField;
    @FXML
    private DatePicker dateOfBirthField;
    public SignupFormController() throws Exception {
        main = Main.getInstance();
    }
    public void onSubmitSignUpBtn() {
        HashMap<String, String> signUpInfo = new HashMap<String, String>();
        signUpInfo.put("firstName", firstNameField.getText());
        signUpInfo.put("lastName", lastNameField.getText());
        signUpInfo.put("email", emailField.getText());
        signUpInfo.put("phone", phoneField.getText());
        signUpInfo.put("dateOfBirth", DateTimeFormatter.ofPattern("dd-MM-yyyy").format(dateOfBirthField.getValue()));
        signUpInfo.put("address", addressField.getText());
        signUpInfo.put("username", usernameField.getText());
        signUpInfo.put("password", passwordField.getText());
        signUpInfo.put("gender", ((RadioButton)gender.getSelectedToggle()).getText().substring(0, 1));
        JSONObject jsonData = new JSONObject();
        jsonData.put("first_name", firstNameField.getText());
        jsonData.put("last_name", lastNameField.getText());
        jsonData.put("email", emailField.getText());
        jsonData.put("phone", phoneField.getText());
        jsonData.put("dob", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dateOfBirthField.getValue()));
        jsonData.put("address", addressField.getText());
        jsonData.put("username", usernameField.getText());
        jsonData.put("pass", passwordField.getText());
        jsonData.put("gender", ((RadioButton)gender.getSelectedToggle()).getText().substring(0, 1));
        Response response = main.getConnector().HTTPSignUpRequest(jsonData);
//        Response response = main.getProcessorManager().getAccountManagementProcessor().handleSignupAction(signUpInfo);
        StatusCode signupStatus = response.getStatusCode();
        if (signupStatus == StatusCode.OK) {
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Dialog");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText("Sign up done!");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
            disableForm();
        } else {
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Dialog");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText(response.getMessage());
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
        }
    }
    public void disableForm() {
        main.getNodeById("#mainOutlineContentView").setDisable(false);
        ((AnchorPane)signupFormRoot.getParent()).getChildren().remove(signupFormRoot);
    }
    public void onSignInBtnClick() throws Exception {
        ((AnchorPane)signupFormRoot.getParent()).getChildren().add(FXMLLoader.load(getClass().getResource("login-form.fxml")));
        ((AnchorPane)signupFormRoot.getParent()).getChildren().remove(signupFormRoot);
    }
}
