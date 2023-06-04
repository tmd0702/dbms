package com.example.GraphicalUserInterface;
import Configuration.Config;
import Connector.Connector;
import Database.*;

import MovieManager.Movie;
import SearchEngine.SearchEngine;
import UserManager.User;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.*;


import java.io.IOException;

public class Main extends Application {
    private static Main main;
    private static Stage stage;
    private Connector connector;
    private User signedInUser;
    private SearchEngine searchEngine;
    private boolean nowShowingMoviesTabActive, comingSoonMoviesTabActive;
    private Movie movieOnDetail;
    private Movie movieOnBooking;
    private ProcessorManager processorManager;
    private String queryOnSearching;

    private Config config;
    public Main() throws Exception {
        super();
        main = this;
        this.processorManager = new ProcessorManager();
        this.config = new Config();
        connector = new Connector(this.config);
        this.queryOnSearching = "";
        nowShowingMoviesTabActive = false;
        comingSoonMoviesTabActive = false;
        searchEngine = new SearchEngine();
    }
    public Connector getConnector() {
        return this.connector;
    }
    public ProcessorManager getProcessorManager() {
        return this.processorManager;
    }
    public Stage getStage() {return this.stage;}

    public Node getNodeById(String id) {
        return stage.getScene().lookup(id);
    }


    public Config getConfig() {
        return this.config;
    }
    public void setMovieOnBooking(Movie movie){
        movieOnBooking = movie;
    }
    public Movie getMovieOnBooking(){
        return movieOnBooking;
    }

    public void setQueryOnSearching(String queryOnSearching) {
        this.queryOnSearching = queryOnSearching;
    }
    public SearchEngine getSearchEngine() {return this.searchEngine;}
    public String getQueryOnSearching() {
        return this.queryOnSearching;
    }
    public void setNowShowingMoviesTabActive(boolean isActive) {
        this.nowShowingMoviesTabActive = isActive;}
    public void setComingSoonMoviesTabActive(boolean isActive) {
        this.comingSoonMoviesTabActive = isActive;
    }
    public boolean getNowShowingMoviesTabActive() {
        return this.nowShowingMoviesTabActive;
    }
    public boolean getComingSoonMoviesTabActive() {
        return this.comingSoonMoviesTabActive;
    }
    public void setMovieOnDetail(Movie movie) {
        movieOnDetail = movie;
    }

    public void setSignedInUser(User user) {
        this.signedInUser = user;
    }
    public User getSignedInUser() {
        return this.signedInUser;
    }
    public  Movie getMovieOnDetail() {
        return movieOnDetail;
    }
    public static synchronized Main getInstance() {
        if (main == null) {
            try {
                main = new Main();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return main;
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        processorManager.getMovieManagementProcessor().getMovies();
        stage = primaryStage;
        setMovieOnBooking(new Movie());
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-outline.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 920, 656);
        scene.getStylesheets().add(getClass().getResource("assets/css/index-style.css").toExternalForm());
        stage.setTitle("4HB Cinema Management");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public void changeView(String fxml) throws IOException {
        ((ScrollPane) getNodeById("#mainOutlineScrollPane")).setVvalue(0.0);
        ((AnchorPane) getNodeById("#mainOutlineContainer")).getChildren().add(FXMLLoader.load(getClass().getResource(fxml)));
        ((AnchorPane) getNodeById("#mainOutlineContainer")).getChildren().remove(0);
    }
    public void changeScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        stage.getScene().setRoot(fxmlLoader.load());
    }
    public Popup popup(String fxml) throws IOException {
        Popup popup = new Popup();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        popup.getContent().add(fxmlLoader.load());
        if (!popup.isShowing())
            popup.show(stage);
        return popup;
    }
    public void hide(Popup popup) {
        popup.hide();
    }
    public static void main(String[] args) {

        launch();

    }
}