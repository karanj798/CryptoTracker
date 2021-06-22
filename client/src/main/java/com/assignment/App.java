package com.assignment;

import com.assignment.models.Currency;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class runs the JavaFX Application.
 * @author Karan
 * @version v1.0
 */
public class App extends Application {
    private static StackedAreaChart<String, Double> areaChart;

    /**
     * Updates the {@code ComboBox} list.
     * @param stocksList list of crypto currency.
     * @param currencyList list of foreign currency.
     */
    public static void updateCryptoList(ComboBox<String> stocksList, List<Currency> currencyList) {
        stocksList.setItems(FXCollections.observableArrayList(
                currencyList.stream()
                        .map(Currency::getId)
                        .collect(Collectors.toList())
        ));
        stocksList.getSelectionModel().selectFirst();
    }

    /**
     * Updates the chart.
     * @param jsonArray list of price points.
     * @param cryptoName name of the crypto currency.
     */
    public static void updateChart(JSONArray jsonArray, String cryptoName) {
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName(cryptoName);

        for (int i = jsonArray.length() - 1; i >= 0; i--) {
            JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
            String timeStamp = jsonObject.getString("time");
            double price = Double.parseDouble(jsonObject.getString("price"));
            try {
                series.getData().add(new XYChart.Data<>(dateTimeFormatter.parse(timeStamp).toString(), price));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        areaChart.getData().add(series);
    }

    /**
     * Updates the watch list view.
     * @param watchListView reference to {@code ListView} instance for updating entries.
     * @param watchList {@code List<String>} containing names of crypto currencies.
     */
    public static void updateWatchListView(ListView watchListView, List<String> watchList) {
        watchListView.getItems().addAll(watchList);
    }

    /**
     * Removes all the data points from {@code AreaChart}.
     */
    public static void clearChart() {
        areaChart.getData().clear();
    }

    /**
     * Displays a {@code Alert} dialog.
     */
    public static void displayInvalidConnectionDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Server is not running!");
        alert.setContentText("Please check if server is running.");
        alert.showAndWait();
        System.exit(1);
    }

    /**
     *  Displays a {@code ChoiceDialog} dialog for selecting profile names.
     * @param profiles {@code List<String>} of profiles to choose from.
     * @return selected profile name.
     */
    public static String displayProfilesDialog(List<String> profiles) {
        ChoiceDialog dialog = new ChoiceDialog(profiles.get(0), profiles);
        dialog.setTitle("Profile Selection");
        dialog.setHeaderText("Select a profile");
        Optional<String> result = dialog.showAndWait();
        String selected = "cancelled.";
        if (result.isPresent()) {
            selected = result.get();
        }
        return selected;
    }

    /**
     * Displays a {@code ChoiceDialog} dialog with a {@code TextField}.
     * @return name of the profile that was entered.
     */
    public static String displayProfileDialog() {
        TextInputDialog dialog = new TextInputDialog("Enter profile");
        dialog.setTitle("Profile Entry");
        dialog.setHeaderText("No profile entry was found.");

        Optional<String> result = dialog.showAndWait();
        String entered = "";
        if (result.isPresent()) {
            entered = result.get();
        }
        return entered;
    }

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
        // UI Components
        Scene scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        stage.setTitle("Client");

        SplitPane mainPane = (SplitPane) scene.lookup("#mainPane");
        GridPane gridPane = (GridPane) mainPane.getItems().get(0);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Period");
        xAxis.setAnimated(false);

        Axis yAxis = new NumberAxis();
        yAxis.setLabel("Growth ($)");

        areaChart = new StackedAreaChart<String, Double>(xAxis, yAxis);
        areaChart.setTitle("Crypto Trends");
        areaChart.setPrefSize(1280, 720);

        gridPane.getChildren().add(areaChart);

        stage.setMaximized(true);
        stage.show();
    }
}