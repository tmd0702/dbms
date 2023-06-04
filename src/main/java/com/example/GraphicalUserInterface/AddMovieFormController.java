package com.example.GraphicalUserInterface;

import MovieManager.Movie;
import Utils.Response;
import Utils.StatusCode;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddMovieFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private ComboBox movieStatusField;
    @FXML
    private TextField titleField, overviewField, languageField, durationField, posterPathField, viewCountField, revenueField, taglineField, voteCountField, voteAverageField, backdropPathField;
    @FXML
    private DatePicker releaseDateField;
    @FXML
    private VBox addMovieForm;

    public AddMovieFormController() throws Exception {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movieStatusFieldInit();
    }
    public void movieStatusFieldInit() {
        String movieStatus[] = {"Planned", "Released"};
        movieStatusField.setItems(FXCollections.observableArrayList(movieStatus));
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
        ((AnchorPane)addMovieForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)addMovieForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveInsertBtnOnClick() throws Exception {
        System.out.println("save");
        handleInsertRecordRequest();
        disableInsertForm();
    }
    public void handleInsertRecordRequest() throws Exception {
        HashMap<String, String> movieInfo = new HashMap<String, String>();
        String id = main.getIdGenerator().generateId("MOVIES");
        movieInfo.put("ID", id);
        movieInfo.put("TITLE", titleField.getText());
        movieInfo.put("OVERVIEW", overviewField.getText());
        movieInfo.put("LANGUAGE", languageField.getText());
        movieInfo.put("DURATION", durationField.getText());
        movieInfo.put("RELEASE_DATE", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(releaseDateField.getValue()));
        movieInfo.put("POSTER_PATH", posterPathField.getText());
        movieInfo.put("BACKDROP_PATH", backdropPathField.getText());
        movieInfo.put("VIEW_COUNT", viewCountField.getText());
        movieInfo.put("REVENUE", revenueField.getText());
        movieInfo.put("TAGLINE", taglineField.getText());
        movieInfo.put("VOTE_COUNT", voteCountField.getText());
        movieInfo.put("VOTE_AVERAGE", voteAverageField.getText());
        movieInfo.put("STATUS", movieStatusField.getValue().toString());
        JSONObject jsonData = new JSONObject();
        jsonData.put("column_value_dict", movieInfo);
        jsonData.put("is_commit", true);
        jsonData.put("processor", main.getProcessorManager().getMovieManagementProcessor().getDefaultDatabaseTable());
        Response response = main.getConnector().HTTPInsertDataRequest(jsonData);
//        Response response = main.getProcessorManager().getMovieManagementProcessor().insertData(movieInfo, true);
        StatusCode status = response.getStatusCode();
        if (status == StatusCode.OK) {
            main.getProcessorManager().getMovieManagementProcessor().scheduleMovie(new Movie(id, titleField.getText(), overviewField.getText(), movieStatusField.getValue().toString(), Integer.parseInt(durationField.getText()), Integer.parseInt(viewCountField.getText()), new Date(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(releaseDateField.getValue())), posterPathField.getText(), backdropPathField.getText(), languageField.getText()));
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
