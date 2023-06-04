package com.example.GraphicalUserInterface;

import UserManager.Customer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainOutlineController implements Initializable {
    private Main main;
    @FXML
    private VBox userNavigator, mainOutlineContentView;
    @FXML
    private TextField inputField;
    @FXML
    private AnchorPane mainOutlineContainer, mainOutlineRootContainer;
    @FXML
    private Button signUpBtn, signInBtn;
    @FXML
    private Group userProfileBtn;
    @FXML
    private Button viewUserProfileBtn, signOutBtn;
    @FXML
    private ImageView logoImageView;
    @FXML
    private Label userFullNameDisplayField;
    public MainOutlineController() {
        this.main = Main.getInstance();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNavigatorInit();
        logoImageViewInit();
        indexViewMainContainerInit();
        if (main.getSignedInUser() != null) {
            signInBtn.setVisible(false);
            signUpBtn.setVisible(false);
            userProfileBtn.setVisible(true);
            userFullNameDisplayField.setText(main.getSignedInUser().getFirstName() + " " + main.getSignedInUser().getLastName());
        }
    }
    public void indexViewMainContainerInit() {
        try {
            mainOutlineContainer.getChildren().add(FXMLLoader.load(getClass().getResource("index-view.fxml")));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void userNavigatorInit() {
        userNavigator.setVisible(false);
        userNavigatorButtonChangeStyleOnHover(viewUserProfileBtn);
        userNavigatorButtonChangeStyleOnHover(signOutBtn);

    }
    public void userNavigatorButtonChangeStyleOnHover(Button button) {
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0.5, 0.0, 0.0); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: #828282;");
            }
        });
        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0.5, 0.0, 0.0); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: #333333;");
            }
        });
    }
    public void logoImageViewInit() {
        String imageSource = "https://docs.google.com/uc?id=1F2pXOLfvuynr9JcURTR5Syg7N1YdPJXK";
        Image logo = new Image(imageSource);
        if (logo.isError()) {
            System.out.println("Error loading image from " + imageSource);
        } else {
            logoImageView.setImage(logo);
        }
    }
    @FXML
    public void viewUserProfileBtnOnClick() throws Exception {
        main.changeView("user-profile-view.fxml");
    }
    @FXML
    public void userProfileBtnOnClick() throws Exception {
        userNavigator.setVisible(!userNavigator.isVisible());
    }
    @FXML
    public void logoImageViewOnClick() throws IOException {
        main.changeView("index-view.fxml");
    }
    @FXML
    public void onSearchFieldEnterKeyPress() throws IOException {
        main.setQueryOnSearching(inputField.getText());
        main.changeView("search-results-view.fxml");
    }
    @FXML
    public void onSignInBtnClick() throws IOException {
        mainOutlineContentView.setDisable(true);
        mainOutlineRootContainer.getChildren().add(FXMLLoader.load(getClass().getResource("login-form.fxml")));
    }
    @FXML
    public void onSignUpBtnClick() throws IOException {
        mainOutlineContentView.setDisable(true);
        mainOutlineRootContainer.getChildren().add(FXMLLoader.load(getClass().getResource("signup-form.fxml")));
    }
    public void modifyHeaderUI() throws Exception {
        main.getNodeById("#signInBtn").setVisible(true);
        main.getNodeById("#signUpBtn").setVisible(true);
        main.getNodeById("#userProfileBtn").setVisible(false);
        main.getNodeById("#userNavigator").setVisible(false);
        main.changeView("index-view.fxml");
    }
    @FXML
    public void signOutBtnOnClick() throws Exception {
        main.setSignedInUser(new Customer());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Sign out success");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("ok");
            modifyHeaderUI();
        } else {
            System.out.println("cancel");
        }
    }
}
