package com.example.GraphicalUserInterface;
import MovieManager.MovieManager;
import UserManager.Customer;
import UserManager.User;
import Utils.Utils;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import MovieManager.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.controlsfx.glyphfont.FontAwesome;

public class IndexViewController implements Initializable {
    private MovieManager movieManager;
    private ImageView currentFocusMovie;
    private Button currentFocusMovieBookingBtn;

    private static Main main;
    @FXML
    private Button scrollLeftBtn;
    @FXML
    private Button scrollRightBtn;
    @FXML
    private ScrollPane moviePreviewSectionScrollPane;
    @FXML ImageView backdropImageSection;
    @FXML
    private HBox currentlyPlayingList;
    @FXML
    private HBox comingSoonList;
    @FXML
    private HBox moviePreviewSection;

    public void initialize(URL url, ResourceBundle rb) {
        moviePreviewSectionInit();
        currentlyPlayingListInit();
        comingSoonListInit();
        backDropImageSectionInit();
        scrollBtnInit();
    }
    public void scrollBtnInit() {
//        scrollLeftBtn.setVisible(false);
//        scrollRightBtn.setVisible(false);
        scrollLeftBtn.setOpacity(0.2);
        scrollRightBtn.setOpacity(0.2);
        scrollBtnChangeStyleOnHover(scrollLeftBtn);
        scrollBtnChangeStyleOnHover(scrollRightBtn);
    }
    public void scrollAnimation(DoubleProperty property, double seconds, double targetHvalue) {
        Animation animation = new Timeline(
                new KeyFrame(Duration.seconds(seconds),
                        new KeyValue(property, targetHvalue)));
        animation.play();
    }
    @FXML
    public void scrollLeftBtnOnClick() {
        scrollLeftBtn.setOpacity(0.5);
        scrollAnimation(moviePreviewSectionScrollPane.hvalueProperty(), 0.5, moviePreviewSectionScrollPane.getHvalue() - 0.25);
    }
    @FXML
    public void scrollRightBtnOnClick() {
        scrollRightBtn.setOpacity(0.5);
        scrollAnimation(moviePreviewSectionScrollPane.hvalueProperty(), 0.5, moviePreviewSectionScrollPane.getHvalue() + 0.25);
    }
    public void scrollBtnChangeStyleOnHover(Button btn) {
        Animation fadeInAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.3),
                        new KeyValue(btn.opacityProperty(), 0.5)));
        Animation fadeOutAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.3),
                        new KeyValue(btn.opacityProperty(), 0.2)));
        btn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                fadeInAnimation.play();
                btn.setOpacity(0.5);
            }
        });
        btn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                fadeOutAnimation.play();
                btn.setOpacity(0.2);
            }
        });
    }
    public void movieListViewSectionInit(HBox listView, ArrayList<Movie> movieList) {
        double listSpacing = 10;
        int counter = 1;
        listView.setSpacing(listSpacing);
        for (Movie movie : movieList) {
            StackPane movieView = getMovieView(listView, listSpacing, movie);
            listView.getChildren().add(movieView);
            counter += 1;
            if (counter > 4) break;
        }
    }
    public static StackPane getMovieView(HBox listView, double listSpacing, Movie movie) {
        StackPane movieView = new StackPane();
//            movieView.setPadding(new Insets(20, 20, 20, 20));
//            movieView.setId(movie.getId() + "CurrentlyPlayingList");
        movieView.setPrefWidth((listView.getPrefWidth() - listSpacing * 3) / 4);
        movieView.setPrefHeight(listView.getPrefHeight());
        VBox movieInfoSection = new VBox();
        Label movieTitle = new Label(movie.getTitle());
        movieTitle.setStyle("-fx-font-weight: bold;-fx-font-size: 14px;-fx-text-fill:white;");
        Label movieReleaseDate = new Label(Utils.getDateStringWithFormat("dd MMMM", movie.getReleaseDate()));
        movieReleaseDate.setStyle("-fx-font-size:11px;-fx-text-fill:white;");
        movieInfoSection.getChildren().add(movieTitle);
        movieInfoSection.getChildren().add(movieReleaseDate);
        movieInfoSection.setPadding(new Insets(20, 20, 20, 20));
        ImageView poster = new ImageView(movie.getPosterImage());
        poster.setFitWidth((listView.getPrefWidth() - listSpacing * 3) / 4);
        poster.setFitHeight(listView.getPrefHeight());
        poster.setBlendMode(BlendMode.MULTIPLY);
        Rectangle blend = new Rectangle(poster.getFitWidth(), poster.getFitHeight(), new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop[] {new Stop(0, Color.WHITE), new Stop(1, Color.DARKGRAY)}));
        movieInfoSection.setTranslateY(listView.getPrefHeight() * 2/3);

        movieView.getChildren().add(blend);
        movieView.getChildren().add(poster);
        movieView.getChildren().add(movieInfoSection);
        movieView.setOnMouseClicked(new EventHandler<MouseEvent>()  {
            @Override
            public void handle(MouseEvent mouseEvent) {
                main.setMovieOnDetail(movie);
                try {
                    System.out.println("change scene to movie detail view");
                    main.changeView("movie-detail-view.fxml");
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        });
        return movieView;
    }
    public void backDropImageSectionInit() {
        backdropImageSection.setFitWidth(903);
//        backdropImageSection.setScaleX(1.3);

    }
    public void currentlyPlayingListInit() {
        movieListViewSectionInit(currentlyPlayingList, movieManager.getCurrentlyPlayingMovieList());
    }
    public void comingSoonListInit() {
        movieListViewSectionInit(comingSoonList, movieManager.getComingSoonMovieList());
    }

    public void moviePreviewSectionInit() {
        moviePreviewSection.setSpacing(30);
        moviePreviewSectionScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        moviePreviewSectionScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        int counter = 0;
        for (Movie movie : movieManager.getMovieList()) {
            if (movie.getPosterImage() == main.getProcessorManager().getMovieManagementProcessor().getMovieManager().getImageNotFound()) continue;
            counter += 1;
            if (counter == 15) break;
            // initialize booking button
            Button bookingBtn = new Button();
            bookingBtn.setId(movie.getId() + "BookingBtn");
            bookingBtn.setPrefWidth(50 * 1.8);
            bookingBtn.setPrefHeight(20 * 1.8);
            bookingBtn.setStyle("-fx-background-color: #AB0A10;-fx-text-fill: white;-fx-font-weight: bold;-fx-font-size:8px;");
            bookingBtn.setText("Book Now");
            bookingBtn.setOpacity(0);
            bookingBtn.setVisible(false);
            bookingBtn.setLayoutY(220);
            bookingBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (main.getSignedInUser() != null) {
                        try {
                            main.setMovieOnBooking(movie);
                            main.changeView("booking-form.fxml");
                        } catch (IOException e) {
                            System.out.println(e);
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
            });
            // initialize movie view
            AnchorPane movieView = new AnchorPane();
            movieView.setId(movie.getId());
            movieView.setPrefWidth(103);
            ImageView poster = new ImageView(movie.getPosterImage());
            poster.setFitHeight(148);
            poster.setFitWidth(103);
            poster.setLayoutY(poster.getFitHeight() - 15);
            //bookingBtn.setDisable(true);
            movieView.setStyle("-fx-background-radius:30%;");
            movieView.getChildren().add(poster);
            movieView.getChildren().add(bookingBtn);

            movieView.setStyle("-fx-background-color: transparent");//(addMovieBtn.getFill());
            changeStyleOnHover(movieView, poster, movie, bookingBtn, counter);
            moviePreviewSection.getChildren().add(movieView);
            if (counter == 1) {
                Event.fireEvent(poster, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                        0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                        true, true, true, true, true, true, null));
            }
        }

    }
    public IndexViewController() throws Exception {
        main = Main.getInstance();
        movieManager = main.getProcessorManager().getMovieManagementProcessor().getMovieManager();
    }
    @FXML
    public void onSeeMoreCPBtnClick() throws IOException {
        main.setNowShowingMoviesTabActive(true);
        main.changeView("movie-list-view.fxml");
    }
    @FXML
    public void onSeeMoreCSBtnClick() throws IOException {
        main.setComingSoonMoviesTabActive(true);
        main.changeView("movie-list-view.fxml");
    }
    public void opacityAnimation(DoubleProperty property, double seconds, double targetOpacity) {
        Animation animation = new Timeline(
                new KeyFrame(Duration.seconds(seconds),
                        new KeyValue(property, targetOpacity)));
        animation.play();
    }
    public void posterLayoutYAnimation(DoubleProperty property, double seconds, double targetLayoutY) {
        Animation animation = new Timeline(
                new KeyFrame(Duration.seconds(seconds),
                        new KeyValue(property, targetLayoutY)));
        animation.play();
    }
    public void posterScaleAnimation(DoubleProperty property, double seconds, double targetSize) {
        Animation animation = new Timeline(
                new KeyFrame(Duration.seconds(seconds),
                        new KeyValue(property, targetSize)));
        animation.play();
    }
    public void changeStyleOnHover(Node node, ImageView poster, Movie movie, Button bookingBtn, int index) {
        poster.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (currentFocusMovie instanceof ImageView) {
                    currentFocusMovieBookingBtn.setVisible(false);
                    currentFocusMovieBookingBtn.setLayoutX(0);
                    currentFocusMovieBookingBtn.setOpacity(0);
//                    opacityAnimation(currentFocusMovieBookingBtn.opacityProperty(), 0.1, 0);
                    posterScaleAnimation(currentFocusMovie.fitHeightProperty(), 0.1, 148);
                    posterScaleAnimation(currentFocusMovie.fitWidthProperty(), 0.1, 103);
                    posterLayoutYAnimation(currentFocusMovie.layoutYProperty(), 0.1, 148 - 15);

                }

                bookingBtn.setVisible(true);
                bookingBtn.setLayoutX(50);
                opacityAnimation(bookingBtn.opacityProperty(), 0.1, 1);
                backdropImageSection.setImage(movie.getBackdropImage());
                posterLayoutYAnimation(poster.layoutYProperty(), 0.1, 15);
                posterScaleAnimation(poster.fitHeightProperty(), 0.1, 148 * 1.8);
                posterScaleAnimation(poster.fitWidthProperty(), 0.1, 103 * 1.8);
                currentFocusMovie = poster;

                currentFocusMovieBookingBtn = bookingBtn;
                CompletableFuture.delayedExecutor(300, TimeUnit.MILLISECONDS).execute(() -> {
                    double tmp = (((double)index - 1 - (7 - (double)index - 1) * 0.3)*1/13);
                    scrollAnimation(moviePreviewSectionScrollPane.hvalueProperty(), 0.4, tmp);
                });

            }
        });
//        poster.setOnMouseExited(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                bookingBtn.setVisible(false);
//                posterScaleAnimation(poster.fitHeightProperty(), 0.1, 148);
//                posterScaleAnimation(poster.fitWidthProperty(), 0.1, 103);
//                posterLayoutYAnimation(poster.layoutYProperty(), 0.1, 148 - 15);
//
//            }
//        });
    }

}
