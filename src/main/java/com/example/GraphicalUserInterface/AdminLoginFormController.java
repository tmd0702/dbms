package com.example.GraphicalUserInterface;

import Utils.StatusCode;
import Utils.Utils;
import Utils.Response;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class AdminLoginFormController implements Initializable {
    private Properties prop;

    private ManagementMain managementMain;
    private String filePath;
    @FXML
    private CheckBox rememberAccountCheckBox;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    public AdminLoginFormController() throws Exception {
        this.managementMain = ManagementMain.getInstance();
        this.prop = new Properties();
        InputStream is = ManagementMain.class.getResourceAsStream("/cache/account-cache.properties");
        System.out.println((Main.class.getResource("/cache").getPath() + "account-cache.properties").substring(1));
        filePath = (Main.class.getResource("/cache").getPath() + "account-cache.properties").substring(1);
        //load a properties file from class path, inside static method
        this.prop.load(is);
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Read properties
        rememberAccountCheckBox.setSelected(true);
        System.out.println(this.prop.getProperty("USERNAME") + " " +  this.prop.getProperty("PASSWORD"));
        //get the property value and print it out
        if (this.prop.getProperty("USERNAME") != "" && this.prop.getProperty("PASSWORD") != "") {
            this.usernameField.setText(this.prop.getProperty("USERNAME"));
            this.passwordField.setText(this.prop.getProperty("PASSWORD"));
        }

    }
    public void onLoginSubmitBtnClick() throws Exception {
        JSONObject signinInfo = new JSONObject();
        signinInfo.put("username", this.usernameField.getText());
        signinInfo.put("password", this.passwordField.getText());
        Response response = managementMain.getConnector().HTTPSignInRequest(signinInfo);
//        Response response = managementMain.getProcessorManager().getAccountManagementProcessor().handleSigninAction(signinInfo);
        StatusCode signinStatus = response.getStatusCode();
        if (signinStatus == StatusCode.OK) {
            System.out.println("Sign in success");

            // write username & password vo trong file account-cache.properties
            if (rememberAccountCheckBox.isSelected()) {
                Utils.writeProperties(this.prop, this.usernameField.getText(), this.passwordField.getText(), filePath);
            } else {
                Utils.writeProperties(this.prop, "", "", filePath);
            }
            this.managementMain.changeScene("management-view.fxml");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Failed");
            alert.setContentText(response.getMessage());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

            } else {

            }
        }
    }
}
