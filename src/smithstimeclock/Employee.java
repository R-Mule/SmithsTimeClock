package smithstimeclock;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**

 @author R-Mule
 */
public class Employee {

    private ClockData cd;
    private String name;
    private int databasePID;
    private String rfid;
    private JLabel myLabel;
    private JLabel statusLabel;
    private int xPos;
    private int yPos;
    private final int STATUS_OFFSET = 80;
    
    public Employee(int pid) {
        this.databasePID = pid;
        name = Database.getEmployeeNameByPID(pid);
        rfid = Database.getEmployeeRFIDByPID(pid);

        myLabel = new JLabel(name, SwingConstants.LEFT);
        statusLabel = new JLabel("", SwingConstants.CENTER);
        cd = Database.getLastValidClockDataByRFID(rfid);//maybe get last known clock data from database here??
        if (cd == null)
        {
            cd = new ClockData();
            //cd.setClockedIn(false);//No time found, never clocked before so default here.
        }
        //System.out.println("My name is : " + Database.getEmployeeNameByPID(pid));
        //gather my most recent data from the database and load it. 

        loadStatus();
    }//end Employee ctor

    
    public String getName(){
        return name;
    }
    
    public void setClockData(ClockData cd) {//This is called when an employee either clocks in or out and handles the ClockData and the GUI end.
        this.cd = cd;
        loadStatus();
    }

    public ClockData getClockData(){
        return cd;
    }
    
    private void loadStatus() {
        if (cd != null)
        {
            if (cd.getClockedIn())
            {
                statusLabel.setText("IN");
                statusLabel.setBackground(Color.GREEN);
            }
            else
            {
                statusLabel.setText("OUT");
                statusLabel.setBackground(Color.RED);
            }

            statusLabel.setSize(70, 50);
            statusLabel.setOpaque(true);
            statusLabel.setLocation(xPos - STATUS_OFFSET, yPos);

            statusLabel.setFont(new Font(myLabel.getName(), Font.BOLD, 30));
            statusLabel.setVisible(true);
        }
    }

    public void setPosition(int x, int y) {
        this.xPos = x;
        this.yPos = y;
        positionUpdated();
    }

    private void positionUpdated() {
        myLabel.setLocation(xPos, yPos);
        statusLabel.setLocation(xPos - STATUS_OFFSET, yPos);
    }

    public void setLabel() {
        myLabel.setLocation(xPos, yPos);
        myLabel.setSize(400, 50);
        myLabel.setFont(new Font(myLabel.getName(), Font.BOLD, 30));
        myLabel.setVisible(true);
    }

    public JLabel getLabel() {
        return myLabel;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public String getRFID() {
        return rfid;
    }
    
    public int getDatabasePID(){
        return databasePID;
    }
}//end Employee
