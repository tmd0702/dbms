package com.example.GraphicalUserInterface;

import Configuration.Config;
import Connector.Connector;
import Database.*;
import ImageManager.ImageManager;
import Utils.IdGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagementMain extends Application {
    private static Stage stage;
    private Connector connector;
    private Scene scene;
    private Config config;
    private IdGenerator idGenerator;
    private ImageManager imageManager;
    private ProcessorManager processorManager;
    private static ManagementMain managementMain;
    public ManagementMain() throws Exception {
        managementMain = this;
        config = new Config();
        connector = new Connector(config);
        idGenerator = new IdGenerator();
        processorManager = new ProcessorManager();
        imageManager = new ImageManager();
    }
    public Connector getConnector() {
        return this.connector;
    }
    public Config getConfig() {
        return this.config;
    }
    public IdGenerator getIdGenerator() {
        return this.idGenerator;
    }
    public Node getNodeById(String id) {
        return stage.getScene().lookup(id);
    }
    public static synchronized ManagementMain getInstance() {
        if (managementMain == null) {
            try {
                managementMain = new ManagementMain();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return managementMain;
    }
    public ImageManager getImageManager() {
        return this.imageManager;
    }
    public Scene getScene() {
        return this.scene;
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin-login-form.fxml"));
        scene = new Scene(fxmlLoader.load(), 920, 600);
        stage.setTitle("4HB Cinema Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public ProcessorManager getProcessorManager() {
        return this.processorManager;
    }

    public void changeScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        stage.getScene().setRoot(fxmlLoader.load());
    }
    public static void ShowErrors(){

    }
    public static void main(String[] args) {
        launch();
    }
}
