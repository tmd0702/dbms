package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddScheduleFormController implements Initializable {
    @FXML
    private ComboBox showTimeStartTimeField;
    @FXML
    private DatePicker showDateField;
    @FXML
    private ComboBox movieTitleField;
    @FXML
    private ComboBox screenRoomNameField, cinemaNameField;
    @FXML
    private VBox addScheduleForm;
    private ManagementMain main;
    private ArrayList<ArrayList<String>> cinemaInfo, screenRoomInfo, movieInfo, showTimeInfo;
    private ArrayList<String> cinemaNames, screenRoomNames, movieTitles, startTimes;
    public AddScheduleFormController() throws Exception {
        main = ManagementMain.getInstance();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        screenRoomNameField.setDisable(true);
        movieTitleField.setDisable(true);
        showTimeStartTimeField.setDisable(true);
        showDateFieldInit();
        cinemaNameFieldInit();
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
        screenRoomNameField.setDisable(false);
        screenRoomInfo = main.getProcessorManager().getScreenRoomManagementProcessor().getData(0, -1, String.format("CINEMA_ID = '%s'", getCinemaObjectIDFromComboBox(cinemaNameField.getValue())), "SCREEN_ROOMS.NAME ASC").getData();
        screenRoomNames = Utils.getDataValuesByColumnName(screenRoomInfo, "SCREEN_ROOMS.NAME");
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
        cinemaInfo = main.getProcessorManager().getCinemaManagementProcessor().getData(0, -1, "", "CINEMAS.NAME ASC").getData();
        cinemaNames = Utils.getDataValuesByColumnName(cinemaInfo, "CINEMAS.NAME");;
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
    @FXML
    public void cancelInsertBtnOnClick() {
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
        ((AnchorPane)addScheduleForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)addScheduleForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveInsertBtnOnClick() throws IOException {
        handleInsertRecordRequest();
        disableInsertForm();
    }
    public String getScreenRoomObjectIDFromComboBox(Object value) {
        String id = null;
        for (int i=0; i<screenRoomNames.size();++i) {
            if (screenRoomNames.get(i).equals(value)) {
                id = screenRoomInfo.get(2 + i).get(0);
                break;
            }
        }
        return id;
    }
    public String getShowTimeObjectIDFromComBoBox(Object value) {
        String id = null;
        for (int i=0; i<startTimes.size();++i) {
            if (startTimes.get(i) == value) {
                id = Utils.getRowValueByColumnName(2 + i, "SHOW_TIMES.ID", showTimeInfo);
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
    public String getCinemaObjectIDFromComboBox(Object value) {
        String id = null;
        for (int i=0; i<cinemaNames.size();++i) {
            if (cinemaNames.get(i).equals(value)) {
                id = cinemaInfo.get(2 + i).get(0);
                break;
            }
        }
        return id;
    }
    public void handleInsertRecordRequest() {
        HashMap<String, String> scheduleInfo = new HashMap<String, String>();
        scheduleInfo.put("ID", main.getIdGenerator().generateId(main.getProcessorManager().getScheduleManagementProcessor().getDefaultDatabaseTable()));
        scheduleInfo.put("SHOW_TIME_ID", getShowTimeObjectIDFromComBoBox(showTimeStartTimeField.getValue()));
        scheduleInfo.put("SHOW_DATE", showDateField.getValue().toString());
        scheduleInfo.put("MOVIE_ID", getMovieObjectIDFromComBoBox(movieTitleField.getValue()));
        scheduleInfo.put("SCREEN_ROOM_ID", getScreenRoomObjectIDFromComboBox(screenRoomNameField.getValue()));
        JSONObject jsonData = new JSONObject();
        jsonData.put("column_value_dict", scheduleInfo);
        jsonData.put("is_commit", true);
        jsonData.put("processor", main.getProcessorManager().getScheduleManagementProcessor().getDefaultDatabaseTable());
        Response response = main.getConnector().HTTPInsertDataRequest(jsonData);
//        Response response = main.getProcessorManager().getScheduleManagementProcessor().insertData(scheduleInfo, true);
        StatusCode signupStatus = response.getStatusCode();
        if (signupStatus == StatusCode.OK) {
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
