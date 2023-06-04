package com.example.GraphicalUserInterface;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserProfileViewController implements Initializable {
    private Main main;
    private Button tabPanelOnClick;
    @FXML
    private AnchorPane userProfileViewMainContainer;
    @FXML
    private VBox userProfileViewTabPanelContainer;
    @FXML
    private Button dashboardTabPanel, profileTabPanel, pointTabPanel, paymentHistoryTabPanel;
    public UserProfileViewController() {
        main = Main.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userProfileViewTabPanelsInit();
        Event.fireEvent(dashboardTabPanel, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                true, true, true, true, true, true, null));
    }
    public void userProfileViewTabPanelsInit() {
        for (Node tabPanel : userProfileViewTabPanelContainer.getChildren()) {
            tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent;");
            tabPanelMouseEventListener((Button) tabPanel);
        }
    }
    public void tabPanelMouseEventListener(Button tabPanel) {
        tabPanel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tabPanel != tabPanelOnClick) tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #293263;");
            }
        });
        tabPanel.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tabPanel != tabPanelOnClick) tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent;");
            }
        });
        tabPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tabPanelOnClick != null) {
                    tabPanelOnClick.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent;");
                }
                tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: rgba(0, 0, 255, 0.3);");
                tabPanelOnClick = tabPanel;
                try {
                    loadScreen();
                } catch(IOException e) {
                    System.out.println(e);
                }
            }
        });
    }
    public void loadScreen() throws IOException {
        if (userProfileViewMainContainer.getChildren().size() > 1) {
            userProfileViewMainContainer.getChildren().remove(1);
        }
        if (tabPanelOnClick == paymentHistoryTabPanel) {
            userProfileViewMainContainer.getChildren().add(FXMLLoader.load(getClass().getResource("payment-history-view.fxml")));
        } else if (tabPanelOnClick == dashboardTabPanel) {
            userProfileViewMainContainer.getChildren().add(FXMLLoader.load(getClass().getResource("dashboard-view.fxml")));
        } else if (tabPanelOnClick == profileTabPanel) {
            userProfileViewMainContainer.getChildren().add(FXMLLoader.load(getClass().getResource("profile-view.fxml")));
        } else if (tabPanelOnClick == pointTabPanel) {
            userProfileViewMainContainer.getChildren().add(FXMLLoader.load(getClass().getResource("point-view.fxml")));
        }
    }
}
