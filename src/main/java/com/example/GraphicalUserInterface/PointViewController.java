package com.example.GraphicalUserInterface;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PointViewController implements Initializable {
    private Main main;
    @FXML
    private Group scoreBarContainer;
    @FXML
    private Label totalSpendingLabel, totalPointLabel;
    @FXML
    private AnchorPane pointViewMainContainer;
    @FXML
    private AnchorPane scoreBar, scoreBarFill;
    public PointViewController() {
        main = Main.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pointStatusInit();
        pointViewLabelsInit();
    }
    public void pointStatusInit() {
        ArrayList<ArrayList<String>> userCategoryFetcher = main.getProcessorManager().getUserCategoryManagementProcessor().getData(0, -1, "", "USER_CATEGORY.POINT_LOWERBOUND ASC").getData();
        System.out.println(userCategoryFetcher);
        int userCategoryFetcherLength = userCategoryFetcher.size() - 2;
        for (int i=2; i < userCategoryFetcher.size();++i) {
            HBox userCategoryLabelContainer = new HBox();
            userCategoryLabelContainer.setAlignment(Pos.CENTER);
            Label userCategoryLabel = new Label(Utils.Utils.getRowValueByColumnName(i, "USER_CATEGORY.CATEGORY", userCategoryFetcher));
            userCategoryLabelContainer.setPrefWidth(70);
            userCategoryLabelContainer.setPrefHeight(70);
            userCategoryLabelContainer.setLayoutX(Math.min(scoreBar.getPrefWidth() + scoreBarContainer.getLayoutX() + userCategoryLabelContainer.getPrefWidth() / 2, scoreBarContainer.getLayoutX() - userCategoryLabelContainer.getPrefWidth() / 2 + (scoreBar.getPrefWidth() + userCategoryLabelContainer.getPrefWidth() - userCategoryLabelContainer.getPrefWidth()) / (userCategoryFetcherLength - 1) * (i - 2)));
            userCategoryLabelContainer.setLayoutY(scoreBarContainer.getLayoutY() - 90);
            userCategoryLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
            userCategoryLabelContainer.setStyle("-fx-background-color: transparent; -fx-border-radius: 50%; -fx-border-width: 4px; -fx-border-color: white;");
            userCategoryLabelContainer.getChildren().add(userCategoryLabel);
            pointViewMainContainer.getChildren().add(createTriangle(userCategoryLabelContainer.getLayoutX() + userCategoryLabelContainer.getPrefWidth() / 2, userCategoryLabelContainer.getLayoutY() + 68));

            Label userCategoryPointLabel = new Label(Utils.Utils.getRowValueByColumnName(i, "USER_CATEGORY.POINT_LOWERBOUND", userCategoryFetcher));
            userCategoryPointLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 16px;");
            userCategoryPointLabel.setLayoutY(userCategoryLabelContainer.getLayoutY() + 120);
            pointViewMainContainer.getChildren().add(userCategoryPointLabel);
            userCategoryPointLabel.setLayoutX(userCategoryLabelContainer.getLayoutX() + userCategoryLabelContainer.getPrefWidth() / 2 - Utils.Utils.getRowValueByColumnName(i, "USER_CATEGORY.POINT_LOWERBOUND", userCategoryFetcher).length() * 4.5);
            pointViewMainContainer.getChildren().add(userCategoryLabelContainer);
        }
        scoreBarFill.setPrefWidth(scoreBar.getPrefWidth() * (double)(main.getSignedInUser().getScore()) / 4000);
    }
    public Polygon createTriangle(double layoutX, double layoutY) {
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(layoutX - 10, layoutY, layoutX + 10, layoutY, layoutX, layoutY + 14);
        triangle.setStyle("-fx-fill: white;");
        return triangle;
    }
    public void pointViewLabelsInit() {
        totalSpendingLabel.setText(totalSpendingLabel.getText() + main.getSignedInUser().getScore() * 1000 + " VND");
        totalPointLabel.setText(totalPointLabel.getText() + main.getSignedInUser().getScore());
    }
}
