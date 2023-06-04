package com.example.GraphicalUserInterface;

import MovieManager.Movie;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class   MovieListViewController implements Initializable {
    private Main main;
    private ArrayList<String> tabs;
    @FXML
    private StackPane tabContainer;
    @FXML
    private Button nowShowingTabBtn;
    @FXML
    private Button comingSoonTabBtn;
    @FXML
    private VBox comingSoonMoviesTab;
    @FXML
    private VBox nowShowingMoviesTab;
    @FXML
    private ImageView logoImageView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nowShowingMoviesTabInit();
        comingSoonMoviesTabInit();
        reRenderPage();
    }
    public MovieListViewController() {
        main = Main.getInstance();
        tabs = new ArrayList<String>();
        tabs.add("nowShowingTab");
        tabs.add("comingSoonTab");
    }
    public void reRenderPage() {
        nowShowingMoviesTab.setVisible(this.main.getNowShowingMoviesTabActive());
        comingSoonMoviesTab.setVisible(this.main.getComingSoonMoviesTabActive());
        if (this.main.getComingSoonMoviesTabActive()) {
            activateTabBtn(comingSoonTabBtn);
        } else {
            deactivateTabBtn(comingSoonTabBtn);
        }
        if (this.main.getNowShowingMoviesTabActive()) {
            activateTabBtn(nowShowingTabBtn);
        } else {
            deactivateTabBtn(nowShowingTabBtn);
        }
    }
    public void activateTabBtn(Button btn) {
        btn.setStyle("-fx-background-color: #740505; -fx-text-fill: white; -fx-font-weight: bold;");
    }
    public void deactivateTabBtn(Button btn) {
        btn.setStyle("-fx-background-color: #323231; -fx-text-fill: white; -fx-font-weight: bold;");
    }
    public void nowShowingMoviesTabInit() {
        double listSpacing = 10;
        VBox moviesContainer = new VBox();
        moviesContainer.setVisible(false);
        moviesContainer.setSpacing(20);
        moviesContainer.setId("nowShowingMoviesTab");
        nowShowingMoviesTab = moviesContainer;
        moviesContainer.setPrefWidth(903);
        moviesContainer.setPadding(new Insets(0, 63, 0, 63));
        tabContainer.getChildren().add(moviesContainer);
        ArrayList<Movie> movieList = main.getProcessorManager().getMovieManagementProcessor().getMovieManager().getCurrentlyPlayingMovieList();
        HBox listView = new HBox();
        for (int i=0;i<movieList.size();++i) {
            Movie movie = movieList.get(i);

            if (i % 4 == 0) {
                listView = new HBox();
                listView.setPrefHeight(259);
                listView.setPrefWidth(777);
//                HBox.setMargin(listView, new Insets(0, 63, 0, 63));
                listView.setSpacing(listSpacing);
                moviesContainer.getChildren().add(listView);
            }
            listView.getChildren().add(IndexViewController.getMovieView(listView, listSpacing, movie));
        }
    }
    public void comingSoonMoviesTabInit() {
        double listSpacing = 10;
        VBox moviesContainer = new VBox();
        moviesContainer.setVisible(false);
        moviesContainer.setSpacing(20);
        moviesContainer.setId("comingSoonMoviesTab");
        comingSoonMoviesTab = moviesContainer;
        moviesContainer.setPrefWidth(903);
        moviesContainer.setPadding(new Insets(0, 63, 0, 63));
        tabContainer.getChildren().add(moviesContainer);
        ArrayList<Movie> movieList = main.getProcessorManager().getMovieManagementProcessor().getMovieManager().getComingSoonMovieList();
        HBox listView = new HBox();
        for (int i=0;i<movieList.size();++i) {
            Movie movie = movieList.get(i);

            if (i % 4 == 0) {
                listView = new HBox();
                listView.setPrefHeight(259);
                listView.setPrefWidth(777);
                listView.setSpacing(listSpacing);
                moviesContainer.getChildren().add(listView);
            }
            listView.getChildren().add(IndexViewController.getMovieView(listView, listSpacing, movie));
        }
    }
    @FXML
    public void comingSoonBtnOnClick() {
        main.setComingSoonMoviesTabActive(true);
        main.setNowShowingMoviesTabActive(false);
        reRenderPage();
    }
    @FXML
    public void nowShowingBtnOnClick() {
        main.setNowShowingMoviesTabActive(true);
        main.setComingSoonMoviesTabActive(false);
        reRenderPage();
    }

}
