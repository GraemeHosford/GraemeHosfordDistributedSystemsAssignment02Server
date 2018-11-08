package threads;

import controllers.ServerMonitor;
import interfaces.SongListChangedListener;

public class FileChangeChecker extends Thread {

    private SongListChangedListener songlistChangedListener;

    public FileChangeChecker(SongListChangedListener songlistChangedListener) {
        this.songlistChangedListener = songlistChangedListener;
    }

    @Override
    public void run() {
        super.run();

        ServerMonitor monitor = ServerMonitor.getInstance();

        // Checks the shared folder for any changes then notifies Main.java to change the shared song list using a listener
        try {

            while(true) {
                if (monitor.checkForChange()) {
                    if(songlistChangedListener != null) {
                        // Notifies Main.java to update song list
                        songlistChangedListener.onSonglistChnaged();
                    }
                }

                // Thread sleeps for 10 seconds
                Thread.sleep(10000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
