package com.example.GraphicalUserInterface;

import MovieManager.Movie;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SearchResultsViewController implements Initializable {
    private Main main;
    private ArrayList<HBox> movieContainerList;
    private HashMap<String, Double> searchResults;
    private String filterLanguage, filterGenre, filterSortMethod;
    @FXML
    private ChoiceBox sortingChoiceBox;
    @FXML
    private ChoiceBox genreChoiceBox;
    @FXML
    private Button filterSearchResultsBtn;
    @FXML
    private ChoiceBox languageChoiceBox;
    @FXML
    private VBox resultsContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchResultsInit();
        choiceBoxesInit();
    }
    public void choiceBoxesInit() {
        genreChoiceBox.setItems(FXCollections.observableArrayList(main.getProcessorManager().getFiltererProcessor().getGenres()));
        languageChoiceBox.setItems(FXCollections.observableArrayList(main.getProcessorManager().getFiltererProcessor().getLanguages()));
        ArrayList<String> sortingMethods = new ArrayList<String>();
        sortingMethods.add("Relevant");
//        sortingMethods.add("By view count");
//        sortingMethods.add("By rate");
        sortingChoiceBox.setItems(FXCollections.observableArrayList(sortingMethods));
        sortingChoiceBox.setValue("Relevant");
        genreChoiceBox.setValue("All genres");
        languageChoiceBox.setValue("All languages");
    }
    public SearchResultsViewController() {
        main = Main.getInstance();
        movieContainerList = new ArrayList<HBox>();
        searchResults = new HashMap<String, Double>();
        filterLanguage = "";
        filterGenre = "";
        filterSortMethod = "Relevant";
    }
    @FXML
    public void filterSearchResultsBtnOnClick() {
        filterGenre = genreChoiceBox.getValue() == "All genres"? "" : (String)genreChoiceBox.getValue();
        filterLanguage = languageChoiceBox.getValue() == "All languages"? "" : (String)languageChoiceBox.getValue();
        filterSortMethod = (String)sortingChoiceBox.getValue();
        displaySearchResults();
    }
    public void displaySearchResults() {
        resultsContainer.getChildren().clear();
        System.out.println(searchResults.keySet());
        for (String key : searchResults.keySet()) {
            Movie movie = main.getProcessorManager().getMovieManagementProcessor().getMovieManager().getMovieById(key.toString());
            if (movie != null && movie.getGenres().toString().contains(filterGenre) && movie.getLanguage().contains(filterLanguage)) {
                HBox movieContainer = new HBox();
                movieContainer.setPrefHeight(220);
                movieContainer.setPrefWidth(resultsContainer.getPrefWidth());
                movieContainer.setStyle("-fx-background-color: black;");
                ImageView poster = new ImageView();
                poster.setPreserveRatio(true);
                poster.setFitWidth(resultsContainer.getPrefWidth() / 3.2);
                poster.setFitHeight(movieContainer.getPrefHeight());
//                System.out.println(movie.getTitle() + " " + movie.getPosterPath());
                if (movie.getPosterImage().getProgress() == 1 && !movie.getPosterImage().isError()) {
                    poster.setImage(movie.getPosterImage());
                } else {
                    poster.setImage(main.getProcessorManager().getMovieManagementProcessor().getMovieManager().getImageNotFound());
                }
                VBox movieContentInfo = new VBox();
                movieContentInfo.setPadding(new Insets(20, 20, 20, 20));
                movieContentInfo.setPrefHeight(movieContainer.getPrefHeight());
                Label title = new Label(movie.getTitle());
                Label overview = new Label("Overview: " + movie.getOverview());
                Label releaseDate = new Label("Release date: " + movie.getReleaseDate().toString());
                Label duration = new Label("Duration: " + movie.getDuration() + " minutes");
                title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
                overview.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                releaseDate.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                duration.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

                movieContentInfo.getChildren().add(title);
                movieContentInfo.getChildren().add(releaseDate);
                movieContentInfo.getChildren().add(duration);
                movieContentInfo.getChildren().add(overview);
                movieContainer.getChildren().add(poster);
                movieContainer.getChildren().add(movieContentInfo);
                poster.setOnMouseClicked(new EventHandler<MouseEvent>()  {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        main.setMovieOnDetail(movie);
                        //main.setMovieOnDetail(new Movie("MOV1000", "Toy Story", "Led by Woody, Andy's toys live happily in his room until Andy's birthday brings Buzz Lightyear onto the scene. Afraid of losing his place in Andy's heart, Woody plots against Buzz. But when circumstances separate Buzz and Woody from their owner, the duo eventually learns to put aside their differences.", "ASDS", 100, 90, new Date(), "https://image.tmdb.org/t/p/original/7G9915LfUQ2lVfwMEEhDsn3kT4B.jpg", "https://image.tmdb.org/t/p/original/9FBwqcd9IRruEDUrTdcaafOMKUq.jpg"));
                        try {
                            main.changeView("movie-detail-view.fxml");
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                    }
                });
                movieContainerList.add(movieContainer);
                resultsContainer.getChildren().add(movieContainer);
            }
        }
    }
    public void searchResultsInit() {
//        searchResults = main.getSearchEngine().getSearchResults(((TextField) main.getNodeById("#inputField")).getText(), "search_engine");//"semantic_searching");
        JSONObject jsonData = new JSONObject();
        jsonData.put("input", ((TextField) main.getNodeById("#inputField")).getText());
        searchResults = main.getConnector().HTTPSearchEngineRequest(jsonData);
        System.out.println("results " + searchResults);
        resultsContainer.setSpacing(20);
        displaySearchResults();
//        resultsContainer.setPrefHeight(pageContainer.getPrefHeight());
    }
}
