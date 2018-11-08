package interfaces;

public interface ServerMonitorInterface {

    String[] getSharedNames();

    boolean checkForChange();

    byte[] getFile(String filename);

}
