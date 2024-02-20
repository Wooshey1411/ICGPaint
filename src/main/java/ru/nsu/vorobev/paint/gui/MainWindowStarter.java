package ru.nsu.vorobev.paint.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindowStarter extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindowStarter.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setMinHeight(480);
        stage.setMinWidth(640);
        stage.setTitle("Paint 2D");
        stage.setScene(scene);
        stage.show();
    }

    public static void init(String[] args) {
        launch();
    }
}