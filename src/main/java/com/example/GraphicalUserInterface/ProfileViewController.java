package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileViewController implements Initializable {
    @FXML
    private AnchorPane profileViewContainer;
    @FXML
    private TextField firstNameField, lastNameField, addressField, phoneField, emailField;
    @FXML
    private DatePicker dateOfBirthField;
    @FXML
    private ToggleGroup gender;
    @FXML
    private HBox genderContainerField;
    @FXML
    private RadioButton maleRadioBtn, femaleRadioBtn;
    private Main main;
    public ProfileViewController() {
        main = Main.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        profileViewInit();
    }
    public void profileViewInit() {
        firstNameField.setText(main.getSignedInUser().getFirstName());
        lastNameField.setText(main.getSignedInUser().getLastName());
        addressField.setText(main.getSignedInUser().getAddress());
        phoneField.setText(main.getSignedInUser().getPhone());
        emailField.setText(main.getSignedInUser().getEmail());
        dateOfBirthField.setValue(LocalDate.parse(main.getSignedInUser().getDateOfBirth().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if (main.getSignedInUser().getGender().equals("M")) {
            maleRadioBtn.setSelected(true);
        } else {
            femaleRadioBtn.setSelected(true);
        }
    }
    @FXML
    public void changePasswordBtnOnClick() throws IOException {
        profileViewContainer.getChildren().add(FXMLLoader.load(getClass().getResource("change-password-form.fxml")));
    }
    @FXML
    public void saveProfileBtnOnClick() {
        HashMap<String, String> userInfo = new HashMap<String, String>();
        userInfo.put("FIRST_NAME", firstNameField.getText());
        userInfo.put("LAST_NAME", lastNameField.getText());
        userInfo.put("EMAIL", emailField.getText());
        userInfo.put("PHONE", phoneField.getText());
        userInfo.put("DOB", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dateOfBirthField.getValue()));
        userInfo.put("ADDRESS", addressField.getText());
        userInfo.put("GENDER", ((RadioButton)gender.getSelectedToggle()).getText().substring(0, 1));
        Response response = main.getProcessorManager().getAccountManagementProcessor().updateData(userInfo, String.format("ID = '%s'", main.getSignedInUser().getId()), true);
        if (response.getStatusCode() == StatusCode.OK) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("User information updated!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("ok");
            } else {
                System.out.println("cancel");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText(response.getMessage());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("ok");
            } else {
                System.out.println("cancel");
            }
        }
    }
}
