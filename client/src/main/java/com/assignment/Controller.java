package com.assignment;

import com.assignment.utils.Serialize;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * This class updates the model and view of Client App.
 *
 * @author Karan
 * @version v1.0
 */
public class Controller implements Initializable {

    // FXML Components
    public Button submitButton;
    public Button clearButton;
    public ComboBox cryptoList;
    public ComboBox currencyList;
    public ComboBox periodList;
    public Button watchListButton;
    public ListView watchListView;
    public Button backUpProfileButton;

    // Internal module for Socket Connection
    private Connection con = null;

    /**
     * Overriding method from Initializable interface.
     * @param url path to primary.fxml file.
     * @param resourceBundle used for locale specific objects.
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            con = new Connection();

            // Get list of profiles
            String response = con.getPayload().replace("[", "").replace("]", "");
            String selectedProfileName = "";
            if (!response.equals("")) {
                if (!response.contains(",")) {
                    selectedProfileName = App.displayProfilesDialog(new ArrayList<String>(Arrays.asList(response)));
                } else {
                    selectedProfileName = App.displayProfilesDialog(Arrays.asList(response.split(", ")));
                }
            }
            con.sendPayload(selectedProfileName);
            response = con.getPayload();
            App.updateWatchListView(watchListView, Arrays.asList(response.split(",")));

            // Data for graph
            response = con.getPayload();
            Serialize serialize = new Serialize(response);
            serialize.parseCurrencies();
            App.updateCryptoList(cryptoList, serialize.getCurrencies());

        } catch (IOException e) {
            App.displayInvalidConnectionDialog();
            e.printStackTrace();
        }
    }

    /**
     * This method performs actions when user clicks on specific buttons.
     *
     * @param event User's action event indicating which button was clicked.
     */
    public void handleEvent(ActionEvent event) {
        if (event.getSource().equals(submitButton)) {
            String cryptoName = cryptoList.getValue().toString();
            String currencyName = currencyList.getValue().toString();
            String periodType = periodList.getValue().toString().toLowerCase();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "submit");
            jsonObject.put("cryptoName", cryptoName);
            jsonObject.put("currencyName", currencyName);
            jsonObject.put("periodType", periodType);

            con.sendPayload(jsonObject.toString());

            try {
                String priceList = con.getPayload();
                JSONArray jsonArray = new JSONArray(priceList);
                App.updateChart(jsonArray, cryptoName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (event.getSource().equals(clearButton)) {
            App.clearChart();
        }

        if (event.getSource().equals(watchListButton)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "addWatchlist");
            jsonObject.put("watchlist", cryptoList.getValue().toString());

            con.sendPayload(jsonObject.toString());

            watchListView.getItems().add(cryptoList.getValue().toString());
        }

        if (event.getSource().equals(backUpProfileButton)) {
            String profileName = App.displayProfileDialog();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "backUpProfile");
            jsonObject.put("profileName", profileName);

            con.sendPayload(jsonObject.toString());
        }
    }

    /**
     * This method handles clicks on ListView.
     * @param event User's {@code MouseEvent} action.
     */
    public void handleMouseEvent(MouseEvent event) {
        String cryptoName = watchListView.getSelectionModel().getSelectedItem().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "submit");
        jsonObject.put("cryptoName", cryptoName);
        jsonObject.put("currencyName", "USD");
        jsonObject.put("periodType", "day");

        con.sendPayload(jsonObject.toString());

        try {
            String priceList = con.getPayload();
            JSONArray jsonArray = new JSONArray(priceList);
            App.updateChart(jsonArray, cryptoName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
