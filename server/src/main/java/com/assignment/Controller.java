package com.assignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * This class updates the model and view of Server App.
 *
 * @author Karan
 * @version v1.0
 */
public class Controller {

    public Button startServerButton;
    public Button stopServerButton;
    public TextArea serverLogTextArea;
    private ServerSocket server;

    /**
     * This method updates TextArea by appending user specified parameter.
     *
     * @param log {@code String} that is appended to {@code TextArea}.
     */
    public static void log(String log) {
        App.updateServerLog(log);
    }

    /**
     * This method performs actions when user clicks on specific buttons.
     *
     * @param event User's action event indicating which button was clicked.
     */
    @FXML
    public void handleEvent(ActionEvent event) {
        if (event.getSource().equals(startServerButton)) {
            // Start server
            log("Starting multithreaded server...\nLaunching threads");

            App.updateButtons(startServerButton, stopServerButton);

            try {
                // Start socket server @ 7896
                server = new ServerSocket(7896);
                for (int i = 0; i < 5; i++) {
                    Runnable mTCPServer = new MultiThreadedTCPServer(server);
                    Thread thread = new Thread(mTCPServer);
                    thread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (event.getSource().equals(stopServerButton)) {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            App.updateButtons(stopServerButton, startServerButton);
            log("Server closed.");
        }
    }
}