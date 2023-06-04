package com.example.GraphicalUserInterface;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardViewController implements Initializable {
    private Main main;
    @FXML
    ToggleGroup gender;
    @FXML
    private Label greetingLabel, memberStatusLabel, totalSpendingLabel, totalPointLabel, nameLabel, emailLabel, phoneLabel;
    public DashboardViewController() {
        main = Main.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dashboardLabelsInit();
    }
    public void dashboardLabelsInit() {
        greetingLabel.setText(greetingLabel.getText().replace("<USERNAME>", main.getSignedInUser().getUsername()));
        memberStatusLabel.setText(memberStatusLabel.getText() + main.getSignedInUser().getUserCategory());
        totalSpendingLabel.setText(totalSpendingLabel.getText() + main.getSignedInUser().getScore() * 1000 + " VND");
        totalPointLabel.setText(totalPointLabel.getText() + main.getSignedInUser().getScore());
        nameLabel.setText(nameLabel.getText() + main.getSignedInUser().getFirstName() + " " + main.getSignedInUser().getLastName());
        emailLabel.setText(emailLabel.getText() + main.getSignedInUser().getEmail());
        phoneLabel.setText(phoneLabel.getText() + main.getSignedInUser().getPhone());
    }
    @FXML
    public void editProfileBtnOnClick() {
        Event.fireEvent(main.getNodeById("#profileTabPanel"), new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                true, true, true, true, true, true, null));
    }
    @FXML
    public void viewPointBtnOnClick() {
        Event.fireEvent(main.getNodeById("#pointTabPanel"), new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                true, true, true, true, true, true, null));
    }
}
