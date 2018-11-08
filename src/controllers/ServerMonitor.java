package controllers;

import interfaces.ServerMonitorInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerMonitor implements ServerMonitorInterface {

    private static ServerMonitor instance;
    private static File sharedFolder;

    private static ArrayList<String> songs;

    private ServerMonitor(){}

    public static ServerMonitor getInstance() {
        if(instance == null) {
            instance = new ServerMonitor();
            sharedFolder = new File("shared");

            sharedFolder.mkdir();

            songs = new ArrayList<>();
            songs.addAll(Arrays.asList(sharedFolder.list()));
        }

        return instance;
    }

    @Override
    public String[] getSharedNames() {
        return sharedFolder.list((dir, name) -> name.endsWith(".mp3"));
    }

    /**
     * Checks the shared folder for any changes to files
     * @return true if folder contents have changed, false if not
     */
    @Override
    public boolean checkForChange() {

        ArrayList<String> newSongList = new ArrayList<>(Arrays.asList(sharedFolder.list()));

        if (newSongList.size() != songs.size()) {
            return true;
        } else {

            for (int i = 0; i < songs.size(); i++) {
                if (!songs.get(i).equals(newSongList.get(i))) {
                    return true;
                }
            }

        }

        return false;
    }

    @Override
    public byte[] getFile(String filename) {
        File file = new File(sharedFolder.getName() + File.separator + filename);

        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
