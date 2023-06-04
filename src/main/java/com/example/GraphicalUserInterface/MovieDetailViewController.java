package com.example.GraphicalUserInterface;

import MovieManager.Movie;
import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class MovieDetailViewController implements Initializable {
    @FXML
    private ImageView moviePosterSection;
    @FXML
    private VBox commentViewSection;
    private Movie movieOnDetail;
    private Main main;
    private boolean isRated;
    private double ratingScore;
    @FXML
    private AnchorPane movieDetailViewMainContainer;
    @FXML
    private TextArea commentField;
    @FXML
    private Label title;
    @FXML
    private ImageView logoImageView;
    @FXML
    private Line separator;
    @FXML
    private Label director;
    @FXML
    private HBox ratingField;
    @FXML
    private Label genre;
    @FXML
    private Label releaseDate;
    @FXML
    private Label duration;
    @FXML
    private Label language;
    @FXML
    private VBox movieDetailSection;
    @FXML
    private Label description;
    @FXML
    private Button bookMovieBtn;
    public MovieDetailViewController () {
        main = Main.getInstance();
        movieOnDetail = main.getMovieOnDetail();
        isRated = false;
        ratingScore = 0.0;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movieDetailSectionInit();
        ratingFieldInit();
        commentViewSectionInit();
    }
    public void movieDetailSectionInit() {
        movieDetailSection.setSpacing(10);
        System.out.println(movieOnDetail.getPosterPath());
        moviePosterSection.setImage(movieOnDetail.getPosterImage());
        title.setText(movieOnDetail.getTitle());
        genre.setText(genre.getText() + movieOnDetail.getGenres().toString().replace("[", "").replace("]", ""));
        releaseDate.setText(releaseDate.getText() + movieOnDetail.getReleaseDate().toString());
        duration.setText(duration.getText() + Integer.toString(movieOnDetail.getDuration()));
        VBox.setMargin(separator, new Insets(20, 0, 20, 0));
        description.setWrapText(true);
        description.setText(movieOnDetail.getOverview());
        description.setDisable(true);
        System.out.println("parent: " + bookMovieBtn.getParent());
        if (main.getProcessorManager().getMovieManagementProcessor().getMovieManager().getCurrentlyPlayingMovieList().contains(movieOnDetail)) {
            bookMovieBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (main.getSignedInUser() != null) {
                        try {
                            main.setMovieOnBooking(movieOnDetail);
                            main.changeView("booking-form.fxml");
                        } catch (IOException e) {
                            System.out.println(e);
                        }

                    }else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Failed");
                        alert.setContentText("Please sign in to comment!");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            Event.fireEvent(main.getNodeById("#signInBtn"), new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        } else {

                        }
                    }
                }
            });
        } else {
            movieDetailSection.getChildren().remove(bookMovieBtn);
        }

    }
    public void ratingFieldInit() {
        for (int i=0; i < ratingField.getChildren().size();++i) {
            StackPane starHolder = (StackPane) ratingField.getChildren().get(i);
            ratingStarMouseEventListener(starHolder.getChildren().get(1), i, "HALF");
            ratingStarMouseEventListener(starHolder.getChildren().get(0), i, "FULL");
        }
    }
    public void activateRatingStar(int starHolderIndex, String type) {
        for (int i=0; i <= starHolderIndex;++i) {
            StackPane starHolder = (StackPane) ratingField.getChildren().get(i);
            if (i < starHolderIndex) {
                starHolder.getChildren().get(0).setStyle("-fx-font-family: FontAwesome; -fx-font-size: 30.0; -fx-fill: #FDC500;");
            } else {
                if (type == "HALF") {
                    starHolder.getChildren().get(1).setStyle("-fx-font-family: FontAwesome; -fx-font-size: 25.0; -fx-fill: #FDC500;");
                } else if (type == "FULL") {
                    starHolder.getChildren().get(0).setStyle("-fx-font-family: FontAwesome; -fx-font-size: 30.0; -fx-fill: #FDC500;");
                }
            }
        }
    }
    public void resetRatingStarStyling() {
        for (int i=0; i < ratingField.getChildren().size();++i) {
            StackPane starHolder = (StackPane) ratingField.getChildren().get(i);
            for (Node star : starHolder.getChildren()) {
                star.setStyle("-fx-font-family: FontAwesome; -fx-font-size: 30.0;");
            }
        }
    }
    public void ratingStarMouseEventListener(Node star, int starHolderIndex, String type) {
        star.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!isRated) activateRatingStar(starHolderIndex, type);
            }
        });
        star.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isRated == false) resetRatingStarStyling();
            }
        });
        star.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isRated) {
                    isRated = false;
                } else {
                    isRated = true;
                    ratingScore = starHolderIndex + 1 - 0.5 * (type == "HALF" ? 1 : 0);
                }
            }
        });
    }
    public void commentViewSectionInit() {
        JSONObject jsonData = new JSONObject();
        jsonData.put("processor", main.getProcessorManager().getReviewManagementProcessor().getDefaultDatabaseTable());
        jsonData.put("query_condition", String.format("M.ID = '%s'", main.getMovieOnDetail().getId()));
        jsonData.put("sort_query", String.format("R.DATE DESC"));
        jsonData.put("count", false);
        jsonData.put("start_index", 0);
        jsonData.put("quantity", -1);

        ArrayList<ArrayList<String>> reviewFetcher = main.getConnector().HTTPSelectDataRequest(jsonData).getData();
//                main.getProcessorManager().getReviewManagementProcessor().getData(0, -1, String.format("M.ID = '%s'", main.getMovieOnDetail().getId()), "R.DATE DESC").getData();
        for (int i=2; i < reviewFetcher.size(); ++i) {
            StackPane userCategoryView = new StackPane(new Label(Utils.getRowValueByColumnName(i, "CATEGORY", reviewFetcher)));
            HBox ratingView = new HBox();
            for (int j=0; j<5; ++j) {
                FontAwesomeIconView fullStar = new FontAwesomeIconView(FontAwesomeIcon.STAR);
                fullStar.setStyle("-fx-font-family: FontAwesome; -fx-font-size: 20.0;");
                ratingView.getChildren().add(fullStar);
            }
            Label usernameView = new Label(Utils.getRowValueByColumnName(i, "USERNAME", reviewFetcher));
            HBox commentViewHeader = new HBox(userCategoryView, ratingView, usernameView);
            Line separator = new Line();
            TextField commentView = new TextField(Utils.getRowValueByColumnName(i, "COMMENT", reviewFetcher));
            VBox commentViewContainer = new VBox(commentViewHeader, commentView, separator);
            commentViewSection.getChildren().add(commentViewContainer);
        }
    }
    @FXML
    public void commentSubmitBtnOnClick() throws Exception {
        if (main.getSignedInUser() != null) {
            HashMap<String, String> reviewInfo = new HashMap<String, String>();
            reviewInfo.put("USER_ID", main.getSignedInUser().getId());
            reviewInfo.put("MOVIE_ID", main.getMovieOnDetail().getId());
            reviewInfo.put("RATING", String.valueOf(ratingScore));
            reviewInfo.put("COMMENT", String.valueOf(commentField.getText()));
            reviewInfo.put("DATE", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            Response response = main.getProcessorManager().getReviewManagementProcessor().insertData(reviewInfo, true);
            StatusCode status = response.getStatusCode();
            if (status == status.OK) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText(response.getMessage());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {

                } else {

                }
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Failed");
                alert.setContentText(response.getMessage());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {

                } else {

                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Failed");
            alert.setContentText("Please sign in to comment!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Event.fireEvent(main.getNodeById("#signInBtn"), new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                        0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                        true, true, true, true, true, true, null));
            } else {

            }
        }
    }
}
