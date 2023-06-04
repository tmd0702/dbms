package com.example.GraphicalUserInterface;
import UserManager.Customer;
import UserManager.Manager;
import Utils.Response;
import Utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import Utils.StatusCode;
import javafx.scene.layout.AnchorPane;
import org.json.JSONObject;

public class LoginFormController implements Initializable {
    private Properties prop;
    private String filePath;
    @FXML
    private AnchorPane loginFormRoot;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox rememberAccountCheckBox;
    private Main main;
    public LoginFormController() throws IOException {
        main = Main.getInstance();
        this.prop = new Properties();
        InputStream is = ManagementMain.class.getResourceAsStream("/cache/account-cache.properties");
        System.out.println((Main.class.getResource("/cache").getPath() + "account-cache.properties").substring(1));
        filePath = (Main.class.getResource("/cache").getPath() + "account-cache.properties").substring(1);
        //load a properties file from class path, inside static method
        this.prop.load(is);
    }

    @Override
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

    public void modifyHeaderUI() {
        main.getNodeById("#signInBtn").setVisible(false);
        main.getNodeById("#signUpBtn").setVisible(false);
        main.getNodeById("#userProfileBtn").setVisible(true);
        ((Label)main.getNodeById("#userFullNameDisplayField")).setText(main.getSignedInUser().getFirstName() + " " + main.getSignedInUser().getLastName());
    }
    public void disableForm() {
        modifyHeaderUI();
        main.getNodeById("#mainOutlineContentView").setDisable(false);
        ((AnchorPane)loginFormRoot.getParent()).getChildren().remove(loginFormRoot);
    }
    public void onLoginSubmitBtnClick() throws Exception {
        JSONObject signinInfo = new JSONObject();
        signinInfo.put("username", this.usernameField.getText());
        signinInfo.put("password", this.passwordField.getText());
        Response response = main.getConnector().HTTPSignInRequest(signinInfo);
//        Response response =  main.getProcessorManager().getAccountManagementProcessor().handleSigninAction(signinInfo);
        StatusCode signinStatus = response.getStatusCode();
        if (signinStatus == StatusCode.OK) {
            JSONObject userInfoFetcher = new JSONObject();
            userInfoFetcher.put("start_index", 0);
            userInfoFetcher.put("quantity", 1);
            userInfoFetcher.put("query_condition", String.format("USERS.USERNAME = '%s'", this.usernameField.getText()));
            userInfoFetcher.put("sort_query", "");
            userInfoFetcher.put("processor", main.getProcessorManager().getAccountManagementProcessor().getDefaultDatabaseTable());
            ArrayList<ArrayList<String>> userInfo = main.getConnector().HTTPSelectDataRequest(userInfoFetcher).getData();
            if (rememberAccountCheckBox.isSelected()) {
                Utils.writeProperties(this.prop, this.usernameField.getText(), this.passwordField.getText(), filePath);
            } else {
                Utils.writeProperties(this.prop, "", "", filePath);
            }
            System.out.println("Sign in success");
            System.out.println(userInfo);
            main.setSignedInUser(new Manager(Utils.getRowValueByColumnName(2, "USERS.USERNAME", userInfo), Utils.getRowValueByColumnName(2, "USERS.ID", userInfo), Utils.getRowValueByColumnName(2, "USERS.FIRST_NAME", userInfo), Utils.getRowValueByColumnName(2, "USERS.LAST_NAME", userInfo), new Date(new SimpleDateFormat("yyyy-MM-dd").parse(Utils.getRowValueByColumnName(2, "USERS.DOB", userInfo)).getTime()), Utils.getRowValueByColumnName(2, "USERS.PHONE", userInfo), Utils.getRowValueByColumnName(2, "USERS.EMAIL", userInfo), Utils.getRowValueByColumnName(2, "USERS.GENDER", userInfo), Utils.getRowValueByColumnName(2, "USERS.ADDRESS", userInfo), Utils.getRowValueByColumnName(2, "USERS.USER_CATEGORY_CATEGORY", userInfo)));
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Sign in success");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("ok");
            } else {
                System.out.println("cancel");
            }
            disableForm();

        } else {
            System.out.println("Sign in failed");
        }
    }
    public void onSignUpBtnClick() throws Exception {
        ((AnchorPane)loginFormRoot.getParent()).getChildren().add(FXMLLoader.load(getClass().getResource("signup-form.fxml")));
        ((AnchorPane)loginFormRoot.getParent()).getChildren().remove(loginFormRoot);
    }
}