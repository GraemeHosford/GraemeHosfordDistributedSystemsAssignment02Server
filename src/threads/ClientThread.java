package threads;

import controllers.ServerMonitor;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class ClientThread extends Thread {

    private Socket clientSocket;

    private static final int GET_FILE_NAMES = 1;
    private static final int CHECK_FOR_CHANGE = 2;
    private static final int DOWNLOAD_FILE = 3;
    private static final int UPLOAD_FILE = 4;

    private static boolean songsChanged;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ClientThread(Socket clientSocket, ObjectInputStream ois, ObjectOutputStream oos) {
        this.clientSocket = clientSocket;
        this.ois = ois;
        this.oos = oos;
    }

    @Override
    public void run() {
        super.run();

        ServerMonitor monitor = ServerMonitor.getInstance();

        try {

            FileChangeChecker fileChangeChecker = new FileChangeChecker(() -> songsChanged = true);
            fileChangeChecker.start();

            while (true) {

                int option = (int) ois.readObject();

                System.out.println(option);

                switch (option) {
                    case GET_FILE_NAMES:
                        String[] names = monitor.getSharedNames();
                        oos.writeObject(names);
                        break;
                    case CHECK_FOR_CHANGE:
                        oos.writeObject(songsChanged);
                        songsChanged = false;
                        break;
                    case DOWNLOAD_FILE:
                        String filename = (String) ois.readObject();
                        byte[] file = monitor.getFile(filename);
                        oos.writeObject(file);
                        break;
                    case UPLOAD_FILE:
                        String fileName = (String) ois.readObject();
                        byte[] uploadedFile = (byte[]) ois.readObject();
                        File newFile = new File("shared" + File.separator + fileName);
                        Files.write(newFile.toPath(), uploadedFile, StandardOpenOption.CREATE_NEW);
                        break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
