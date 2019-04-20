/**
 * @author: Yi
 * @className: ConnectWithServer.java
 * @packageName: banking.client
 * @date: 2018-11-20    14:50
 * @description: This class is used for connecting with server by sending  and receiving instruction.
 */

package banking.client;

import banking.data.Instruction;

import java.io.*;
import java.net.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

class ConnectWithServer implements Instruction {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    private Logger clientConnectorLogger;
    private FileHandler connectWithServerHandler;

    /**
     * This constructor initializes client socket and bufferedReader and printWriter.
     */
    ConnectWithServer() {
        try {
            clientConnectorLogger = Logger.getLogger("Connect With Server Logger");
            connectWithServerHandler = new FileHandler("../data/ConnectWithServer");
            clientConnectorLogger.addHandler(connectWithServerHandler);
            socket = new Socket("127.0.0.1", 2333);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (Exception ex) {
            // logs the exceptions
            StackTraceElement[] elements = ex.getStackTrace();
            for (StackTraceElement element : elements) {
                clientConnectorLogger.warning("Connect With Server Exception: " + element);
            }
        }
    }


    /**
     * This method sends the message to the server.
     *
     * @param message: the message to be sent
     */
    void sendMessage(String message) {
        printWriter.println(message);
    }


    /**
     * This method receives the message from the server.
     *
     * @return the message received from the server
     */
    String receiveMessage() {
        String receivedMessage = ERROR;
        try {
            receivedMessage = bufferedReader.readLine();
        } catch (Exception ex) {
            // logs the exceptions
            StackTraceElement[] elements = ex.getStackTrace();
            for (StackTraceElement element : elements) {
                clientConnectorLogger.warning("Connect With Server Exception: " + element);
            }
        }
        return receivedMessage;
    }
}
