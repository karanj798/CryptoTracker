package com.assignment.utils;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

/**
 * This class uses Sockets to perform HTTPS request to coinbase's API. Just
 * like ServerSocket class, this class waits for the HTTPS to be finished and then
 * returns the output.
 *
 * @author Karan
 * @version v1.0
 */
public class Request {
    private final String hostName;
    private final String route;
    private final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36";
    private final int port = 443; // Port 443 is used because SSL traffic is defaulted to that port.

    /**
     * Initializes the instance variables to the user specified hostName and route.
     *
     * @param hostName DNS of the website's API.
     * @param route    Path to API Endpoint.
     */
    public Request(String hostName, String route) {
        this.hostName = hostName;
        this.route = route;
    }

    /**
     * This method performs a HTTPS GET request to user specified {@code hostName} and {@code path}.
     *
     * @return the {@code JSON} payload retrieved from the HTTPS request.
     */
    public String get() {
        // Establish connection to coinbase API
        try {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket coinBaseClient = (SSLSocket) factory.createSocket(this.hostName, this.port);
            coinBaseClient.startHandshake();

            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(coinBaseClient.getOutputStream())));
            setRequestHeaders(pw);

            BufferedReader in = new BufferedReader(new InputStreamReader(coinBaseClient.getInputStream()));
            return readResponse(in);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * This method sets the HTTP headers by writing to {@code SSLSocket}'s output stream.
     *
     * @param pw the output stream of SSLSocket.
     */
    private void setRequestHeaders(PrintWriter pw) {
        // Write HTTP Headers
        pw.print("GET " + this.route + " HTTP/1.1\r\n");
        pw.print("Host: " + this.hostName + "\r\n\r\n");
        pw.print("User-Agent: " + this.userAgent + "\r\n\r\n");
        pw.print("Accept: */*");
        pw.flush();
        if (pw.checkError()) {
            System.out.println("SSLSocketClient:  java.io.PrintWriter error");
        }
    }

    /**
     * This method filters the response from HTTPS request.
     *
     * @param in the input stream of SSLSocket.
     * @return JSON Payload.
     * @throws IOException if an I/O error occurs when reading from input stream.
     */
    private String readResponse(BufferedReader in) throws IOException {
        String content;
        while ((content = in.readLine()) != null) {
            // Check for [ or { since that is what JSON data starts with.
            if (content.startsWith("[") || content.startsWith("{")) {
                return content;
            }
        }
        return "";
    }
}
