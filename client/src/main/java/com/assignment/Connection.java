package com.assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class is used for communicating with the Server.
 * @author Karan
 * @version v1.0
 */
public class Connection {

    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;

    /**
     * Creates a {@code Socket} on the specified host name and port number.
     * @throws IOException if {@code Socket} is unable to establish the connection.
     */
    public Connection() throws IOException {
        socket = new Socket("127.0.0.1", 7896);
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        pw = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Closes the connection.
     * @throws IOException if {@code Socket} connection is not established.
     */
    public void closeConnection() throws IOException {
        this.socket.close();
    }

    /**
     * Reads the input stream of the {@code Socket}.
     * @return {@code String} representation of the data read from the stream.
     * @throws IOException
     */
    public String getPayload() throws IOException {
        return br.readLine();
    }

    /**
     * Write the {@code String} to output stream of {@code Socket}.
     * @param data {@code String} which is printed to the output stream of {@code Socket}.
     */
    public void sendPayload(String data) {
        pw.println(data);
    }
}
