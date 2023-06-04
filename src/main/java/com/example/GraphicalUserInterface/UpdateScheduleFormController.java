package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class UpdateScheduleFormController implements Initializable {
    @FXML
    private ComboBox showTimeStartTimeField;
    @FXML
    private ComboBox movieTitleField;
    @FXML
    private TextField idField;
    @FXML
    private DatePicker showDateField;
    @FXML
    private ComboBox screenRoomNameField, cinemaNameField;
    @FXML
    private VBox updateScheduleForm;
    private ManagementMain main;
    private ArrayList<ArrayList<String>> cinemaInfo, screenRoomInfo, movieInfo, showTimeInfo;
    private ArrayList<String> cinemaNames, screenRoomNames, movieTitles, startTimes;
    public UpdateScheduleFormController() {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idFieldInit();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), ae -> {
            cinemaNameFieldInit();
            showDateFieldInit();
            screenRoomNameFieldInit();
            movieTitleFieldInit();
            startTimeFieldInit();
        }));
        timeline.play();

    }
    public void idFieldInit() {
        idField.setDisable(true);
    }
    public void disableUpdateForm() {
        ((AnchorPane)updateScheduleForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)updateScheduleForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveUpdateBtnOnClick() throws IOException {
        handleUpdateRecordRequest();
        disableUpdateForm();
    }
    @FXML
    public void cancelUpdateBtnOnClick() {
        disableUpdateForm();
    }

    public void showDateFieldInit() {
        showDateField.valueProperty().addListener((obs, oldItem, newItem) -> {
            movieTitleField.setValue(null);
            if (newItem == null) {
                movieTitleField.setDisable(true);
            } else {
                if (screenRoomNameField.getValue() != null) {
                    movieTitleFieldInit();
                }
            }
        });
    }
    public void startTimeFieldInit() {
        showTimeStartTimeField.setDisable(false);
        showTimeInfo = main.getProcessorManager().getShowTimeManagementProcessor().select("ST.*", 0, -1, String.format("NOT EXISTS (SELECT S.SHOW_TIME_ID FROM SCHEDULES S WHERE S.SHOW_DATE = '%s' AND S.SCREEN_ROOM_ID = '%s' AND S.SHOW_TIME_ID = ST.ID)", showDateField.getValue(), getScreenRoomObjectIDFromComboBox(screenRoomNameField.getValue())), "ST.START_TIME ASC", "SHOW_TIMES ST").getData();
        startTimes = Utils.getDataValuesByColumnName(showTimeInfo, "START_TIME");
        ArrayList<String> removeObjectList = new ArrayList<String>();
        for (String startTime : startTimes) {
            if (!main.getProcessorManager().getScheduleManagementProcessor().isMovieSchedulingAvailable(getMovieObjectIDFromComBoBox(movieTitleField.getValue()), startTime, getScreenRoomObjectIDFromComboBox(screenRoomNameField.getValue()), getCinemaObjectIDFromComboBox(cinemaNameField.getValue()), DateTimeFormatter.ofPattern("yyyy-MM-dd").format(showDateField.getValue()))) {
                removeObjectList.add(startTime);
            }
        }
        for (String object : removeObjectList) {
            startTimes.remove(object);
        }
        showTimeStartTimeField.setItems(FXCollections.observableArrayList(startTimes));
    }
    public void movieTitleFieldInit() {
        movieTitleField.setDisable(false);
        movieInfo = main.getProcessorManager().getMovieManagementProcessor().select("M.*", 0, -1, String.format("NOT EXISTS (SELECT S.MOVIE_ID FROM SCHEDULES S WHERE S.SHOW_DATE = '%s' AND S.SCREEN_ROOM_ID = '%s' AND S.MOVIE_ID = M.ID HAVING COUNT(*) >= %s)", showDateField.getValue(), getScreenRoomObjectIDFromComboBox(screenRoomNameField.getValue()), main.getConfig().get("MAXIMUM_MOVIE_SHOW_TIMES_IN_ONE_SCREEN_ROOM")), "M.TITLE ASC", "MOVIES M").getData();
        movieTitles = Utils.getDataValuesByColumnName(movieInfo, "TITLE");
        movieTitleField.setItems(FXCollections.observableArrayList(movieTitles));
        movieTitleField.valueProperty().addListener((obs, oldItem, newItem) -> {
            showTimeStartTimeField.setValue(null);
            if (newItem == null) {
                showTimeStartTimeField.setDisable(true);
            } else {
                startTimeFieldInit();
            }
        });
    }
    public void screenRoomNameFieldInit() {
        System.out.println(cinemaNameField.getValue() + " cinema name");
        screenRoomNameField.setDisable(false);
        screenRoomInfo = main.getProcessorManager().getScreenRoomManagementProcessor().getData(0, -1, String.format("CINEMA_ID = '%s'", getCinemaObjectIDFromComboBox(cinemaNameField.getValue())), "NAME ASC").getData();
        screenRoomNames = Utils.getDataValuesByColumnName(screenRoomInfo, "NAME");
        screenRoomNameField.setItems(FXCollections.observableArrayList(screenRoomNames));
        screenRoomNameField.valueProperty().addListener((obs, oldItem, newItem) -> {
            movieTitleField.setValue(null);
            if (newItem == null) {
                movieTitleField.setDisable(true);
            } else {
                if (showDateField.getValue() != null) {
                    movieTitleFieldInit();
                }
            }
        });
    }
    public void cinemaNameFieldInit() {
        cinemaInfo = main.getProcessorManager().getCinemaManagementProcessor().getData(0, -1, "", "NAME ASC").getData();
        cinemaNames = Utils.getDataValuesByColumnName(cinemaInfo, "NAME");;
        cinemaNameField.setItems(FXCollections.observableArrayList(cinemaNames));
        cinemaNameField.valueProperty().addListener((obs, oldItem, newItem) -> {
            screenRoomNameField.setValue(null);
            if (newItem == null) {
                screenRoomNameField.setDisable(true);
            } else {
                screenRoomNameFieldInit();
            }
        });
    }
    public String getCinemaObjectIDFromComboBox(Object value) {
        String id = null;

        for (int i=0; i<cinemaNames.size();++i) {
            if (cinemaNames.get(i).equals(value)) {
                id = Utils.getRowValueByColumnName(2 + i, "ID", cinemaInfo);
                break;
            }
        }
        return id;
    }
    public String getScreenRoomObjectIDFromComboBox(Object value) {
        String id = null;
        for (int i=0; i<screenRoomNames.size();++i) {
            if (screenRoomNames.get(i).equals(value)) {
                id = Utils.getRowValueByColumnName(2 + i, "ID", screenRoomInfo);
                break;
            }
        }
        return id;
    }
    public String getShowTimeObjectIDFromComBoBox(Object value) {
        String id = null;
        for (int i=0; i<startTimes.size();++i) {
            if (startTimes.get(i) == value) {
                id = Utils.getRowValueByColumnName(2 + i, "ID", showTimeInfo);
                break;
            }
        }
        return id;
    }
    public String getMovieObjectIDFromComBoBox(Object value) {
        String id = null;
        for (int i=0; i<movieTitles.size();++i) {
            if (movieTitles.get(i).equals(value)) {
                id = Utils.getRowValueByColumnName(2 + i, "ID", movieInfo);
                break;
            }
        }
        return id;
    }
    public void handleUpdateRecordRequest() {
        HashMap<String, String> scheduleInfo = new HashMap<String, String>();
        scheduleInfo.put("SHOW_TIME_ID", getShowTimeObjectIDFromComBoBox(showTimeStartTimeField.getValue()));
        scheduleInfo.put("SHOW_DATE", showDateField.getValue().toString());
        scheduleInfo.put("MOVIE_ID", getMovieObjectIDFromComBoBox(movieTitleField.getValue()));
        scheduleInfo.put("SCREEN_ROOM_ID", getScreenRoomObjectIDFromComboBox(screenRoomNameField.getValue()));
        JSONObject jsonData = new JSONObject();
        jsonData.put("column_value_dict", scheduleInfo);
        jsonData.put("query_condition", String.format("ID = '%s'", idField.getText()));
        jsonData.put("is_commit", true);
        jsonData.put("processor", main.getProcessorManager().getScheduleManagementProcessor().getDefaultDatabaseTable());
        Response response = main.getConnector().HTTPUpdateDataRequest(jsonData);
//        Response response = main.getProcessorManager().getScheduleManagementProcessor().updateData(scheduleInfo, String.format("ID = '%s'", idField.getText()), true);
        StatusCode status = response.getStatusCode();
        if (status == StatusCode.OK) {
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Success");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText("The record has been updated successfully");
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
