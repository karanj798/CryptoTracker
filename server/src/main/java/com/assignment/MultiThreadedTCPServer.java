package com.assignment;

import com.assignment.utils.Request;
import com.assignment.utils.Serialize;
import org.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class handles incoming requests from the Client.
 *
 * @author Karan
 * @version v1.0
 */
public class MultiThreadedTCPServer implements Runnable {
    public ServerSocket serverSocket;

    /**
     * Initializes {@code ServerSocket} with the one specified by the user.
     *
     * @param serverSocket an instance of {@code ServerSocket} to use for serving requests.
     */
    public MultiThreadedTCPServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * This method gets the list of crypto currencies.
     *
     * @return list of crypto currencies as {@code String}.
     */
    public String getCryptoList() {
        String hostName = "api.pro.coinbase.com";
        String route = "/currencies";

        Controller.log("Fetching https://" + hostName + route);

        // Perform a HTTP GET Request to Coinbase's API
        Request request = new Request(hostName, route);
        String content = request.get();

        // Serialize the JSON payload into models
        Serialize serialize = new Serialize(content);
        String cryptoList = serialize.parseCryptoCurrencies().toString();

        Controller.log("Fetched: " + cryptoList);

        return cryptoList;
    }

    /**
     * This method gets the list of prices of a specific crypto-currency.
     *
     * @param cryptoName      name of crypto-currency.
     * @param foreignCurrency name of foreign currency.
     * @param timeType        type of the period.
     * @return list of prices of a crypto currency as {@code String}.
     */
    public String getPriceList(String cryptoName, String foreignCurrency, String timeType) {
        String hostName = "api.coinbase.com";
        String route = "/v2/prices/" + cryptoName + "-" + foreignCurrency + "/historic?period=" + timeType;

        Controller.log("Fetching https://" + hostName + route);

        // Perform a HTTP GET Request to Coinbase's API
        Request request = new Request(hostName, route);
        String content = request.get();

        // Serialize the JSON payload into models
        Serialize serialize = new Serialize(content);
        String priceList = serialize.parseDataPoints();

        Controller.log("Fetched: " + priceList);

        return priceList;
    }

    /**
     * This method reads the watchlist.tx file.
     *
     * @return {@code List<String>} consisting names of crypto currencies.
     */
    public List<String> readFile() {
        List<String> profiles = new ArrayList<>();
        System.out.println();
        try {
            // Reading watchlist.txt file
            File file = new File(System.getProperty("user.dir") + "/src/main/resources/com/assignment/watchlist.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                profiles.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return profiles;
    }

    /**
     * This method reads the watchlist.tx file from a specified profile folder.
     * @param profileName name of the profile.
     * @return {@code List<String>} consisting names of crypto currencies.
     */
    public List<String> readFile(String profileName) {
        List<String> profiles = new ArrayList<>();
        System.out.println();
        try {
            // Reading watchlist.txt file
            File file = new File(System.getProperty("user.dir") + "/src/main/resources/com/assignment/profiles/" + profileName + "/watchlist.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                profiles.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return profiles;
    }

    /**
     * This method writes to watchlist.txt in append mode.
     *
     * @param content name of crypto currency.
     */
    public void writeFile(String content) {
        Controller.log("Wrote: " + content);
        try {
            // Updating watchlist.txt file
            File file = new File(System.getProperty("user.dir") + "/src/main/resources/com/assignment/watchlist.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.write(content);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mkdir(String folderName) {
        Controller.log("Creating: " + folderName);
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/com/assignment/profiles/" + folderName);
        boolean dirCreated = false;
        if (!file.exists()) {
            try {
                dirCreated = file.mkdir();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        if (dirCreated) {
            Controller.log("Created: " + folderName);
            File watchListFile = new File(System.getProperty("user.dir") + "/src/main/resources/com/assignment/watchlist.txt");
            try {
                FileUtils.copyFileToDirectory(watchListFile, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Controller.log("Copied: " + folderName);
        }
    }

    /**
     * This function lists the names of all the folders present in the given path.
     * @return {@code String} containing list of files.
     */
    public String listdirs() {
        return Arrays.toString(new File(System.getProperty("user.dir") + "/src/main/resources/com/assignment/profiles/").list());
    }

    /**
     * This method handles client's requests concurrently.
     */
    @Override
    public void run() {
        try {
            Socket client = this.serverSocket.accept();
            Controller.log("Client IP: " + client.getInetAddress() + " Client Port: " + client.getPort());

            PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // First Response
            pw.println(this.listdirs());
            String selectedProfileName = in.readLine();
            pw.println(readFile(selectedProfileName).toString().replace("[", "").replace("]", "").replace(" ", ""));


            // Second Response
            pw.println(this.getCryptoList());


            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // Handle infinite requests from a client.

                JSONObject jsonObject = new JSONObject(inputLine);
                Controller.log("Client sent: " + jsonObject.toString());

                if (jsonObject.getString("msg").equalsIgnoreCase("submit")) {
                    // Send plot data for graphing.
                    String httpPayload = this.getPriceList(
                            jsonObject.getString("cryptoName"),
                            jsonObject.getString("currencyName"),
                            jsonObject.getString("periodType"));
                    pw.println(httpPayload);
                }

                if (jsonObject.getString("msg").equalsIgnoreCase("addWatchlist")) {
                    // Write the payload to file.
                    writeFile(jsonObject.getString("watchlist"));
                }

                if (jsonObject.getString("msg").equalsIgnoreCase("backUpProfile")) {
                    // Copy the current watchlist.txt and make a new folder
                    mkdir(jsonObject.getString("profileName"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
