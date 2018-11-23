package smithstimeclock;


import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**

 @author R-Mule
 */
public class SmithsTimeClock {
    private static final int PORT = 9999;
    private static ServerSocket socket; 
    
    public static void main(String[] args) {
        //This insures only one instance can run at a time on this PC.
        checkIfRunning();
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
        
        //lockInstance(ConfigFileReader.getJarPath()+"SmithsTimeClock.jar");THIS SON OF A BITCH
    }
    
   

private static void checkIfRunning() {
  try {
    //Bind to localhost adapter with a zero connection queue 
    socket = new ServerSocket(PORT,0,InetAddress.getByAddress(new byte[] {127,0,0,1}));
  }
  catch (BindException e) {
    System.err.println("Already running.");
    System.exit(1);
  }
  catch (IOException e) {
    System.err.println("Unexpected error.");
    e.printStackTrace();
    System.exit(2);
  }
}

}
