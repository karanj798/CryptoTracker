package com.assignment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class runs the JavaFX Application.
 *
 * @author Karan
 * @version v1.0
 */
public class App extends Application {

    private static Scene scene;

    /**
     * Loads fxml file.
     * @param fxml name of fxml file.
     * @return loader.
     * @throws IOException if the specified file is not found.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Switches the button to disable and non-disabled.
     * @param enabledButton {@code Button}
     * @param disabledButton {@code Button}
     */
    public static void updateButtons(Button enabledButton, Button disabledButton) {
        enabledButton.setDisable(true);
        disabledButton.setDisable(false);
    }

    /**
     * Updates the TextArea by utilizing the appendText method.
     * @param content {@code String} representation of text which is going to be appended.
     */
    public static void updateServerLog(String content) {
        TextArea serverLogTextArea = (TextArea) scene.lookup("#serverLogTextArea");
        serverLogTextArea.appendText(content + "\n");
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Overriding start method.
     * @param stage {@code JavaFX} element of the window.
     * @throws IOException if the specified fxml file is not found.
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        stage.setTitle("Server");
        stage.show();
    }
}