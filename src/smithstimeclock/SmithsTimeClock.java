package smithstimeclock;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**

 @author R-Mule
 */
public class SmithsTimeClock {


    private static final int PORT = 9999;
    private static ServerSocket socket;
    private static MainFrame jf;
    static DBListener dbl;
    public static JLabel smsStatusIndicator;

    public static void main(String[] args) {
        //This insures only one instance can run at a time on this PC.
        checkIfRunning();
        smsStatusIndicator = new JLabel("•");
        smsStatusIndicator.setForeground(Color.GREEN);
        smsStatusIndicator.setLocation(225, 875);
        smsStatusIndicator.setSize(50, 50);
        smsStatusIndicator.setFont(new Font("Button Font", 0, 60));
        smsStatusIndicator.setVisible(true);

        Thread UDPReceiver = new Thread() {
            public void run() {
                try
                {
                    UDPReceiver receiver = new UDPReceiver(dbl);
                }
                catch (Exception e)
                {
                    System.err.println(e);
                }
            }
        };

        Thread SerialService = new Thread() {
            public void run() {
                try
                {
                    try
                    {
                        dbl = new DBListener(smsStatusIndicator);
                        UDPReceiver.start();
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                catch (Exception e)
                {
                    System.err.println(e);
                }
            }
        };

        Thread timeClockSystem = new Thread() {
            public void run() {
                try
                {
                    ConfigFileReader.loadConfiguration();
                }
                catch (IOException ex)
                {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                Database.loadDatabase();
                jf = new MainFrame(smsStatusIndicator);
                jf.setVisible(true);
                SerialService.start();
               // while(dbl == null)
               // {
                    
               // }
               // jf.setDbl(dbl);
            }
        };

        timeClockSystem.start();

        //lockInstance(ConfigFileReader.getJarPath()+"SmithsTimeClock.jar");THIS SON OF A BITCH
    }

    private static void checkIfRunning() {
        try
        {
            //Bind to localhost adapter with a zero connection queue 
            socket = new ServerSocket(PORT, 0, InetAddress.getByAddress(new byte[]
            {
                127, 0, 0, 1
            }));
        }
        catch (BindException e)
        {
            System.err.println("Already running.");
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("Unexpected error.");
            e.printStackTrace();
            System.exit(2);
        }
    }

}
