package main;

import threads.ClientThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 9090;

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            while(true) {
                Socket clientSocket = serverSocket.accept();

                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());

                ClientThread clientThread = new ClientThread(clientSocket, ois, oos);
                clientThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
