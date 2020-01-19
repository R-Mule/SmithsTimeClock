package smithstimeclock;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**

 @author R-Mule
 */
public class DBListener {

    private String host;
    private String userName;
    private String password;
    private COMHandler ch;
    private JLabel smsStatusIndicator;

    public DBListener(JLabel smsStatusIndicator) {
        host = ConfigFileReader.getHostName();
        userName = ConfigFileReader.getUserName();
        password = ConfigFileReader.getPassword();
        this.smsStatusIndicator = smsStatusIndicator;
        ch = new COMHandler();
        try
        {
            ch.connect(ConfigFileReader.getGSMModemCOMPort());
        }
        catch (Exception ex)
        {
            this.smsStatusIndicator.setForeground(Color.RED);
            Logger.getLogger(DBListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        beginChecking();
    }

    public void beginChecking() {
        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                //System.out.println(new Date());
                checkForMsgsToSend();
            }
        }, 0, 20, TimeUnit.SECONDS);
    }

    public void checkForMsgsToSend() {
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from smsMsgQueue;");

            while (rs.next())
            {
                boolean success = ch.sendMessage(rs.getString(2), rs.getString(3));
                if (success)
                {
                    this.smsStatusIndicator.setForeground(Color.GREEN);
                    Database.deleteSmsMsgFromQueueById(rs.getInt(1));//System.out.println("Remove from Database");//removeFromDatabase;
                }
                else
                {
                    this.smsStatusIndicator.setForeground(Color.RED);
                }
            }//end while

            con.close();
            //return temp;
        }
        catch (Exception e)
        {
            this.smsStatusIndicator.setForeground(Color.RED);
            System.out.println(e);
        }
    }
    
    public void sendForcedMessage(String number, String message)
    {
         boolean success = ch.sendMessage(number, message);
                if (!success)
                {
                    sendForcedMessage(number, message);
                }
    }
}
