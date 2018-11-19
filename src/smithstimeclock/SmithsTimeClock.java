package smithstimeclock;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**

 @author R-Mule
 */
public class SmithsTimeClock {

    public static void main(String[] args) {
        //This insures only one instance can run at a time on this PC.
        try
        {
            ConfigFileReader.loadConfiguration();
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        Database.loadDatabase();
        MainFrame jf = new MainFrame();
        jf.setVisible(true);
        lockInstance(ConfigFileReader.getJarPath()+"SmithsTimeClock.jar");
    }
    
    private static boolean lockInstance(final String lockFile) {
    try {
        final File file = new File(lockFile);
        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        final FileLock fileLock = randomAccessFile.getChannel().tryLock();
        if (fileLock != null) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        fileLock.release();
                        randomAccessFile.close();
                        file.delete();
                    } catch (Exception e) {
                        //log.error("Unable to remove lock file: " + lockFile, e);
                    }
                }
            });
            return true;
        }
    } catch (Exception e) {
       // log.error("Unable to create and/or lock file: " + lockFile, e);
    }
    return false;
}

}
