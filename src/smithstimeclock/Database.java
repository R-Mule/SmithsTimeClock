package smithstimeclock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**

 @author R-Mule
 */
public class Database {

    private static String host;
    private static String userName;
    private static String password;
    //private ConfigFileReader reader;

    private Database() {
    }//end databaseCtor

    public static void loadDatabase() {
        host = ConfigFileReader.getHostName();
        userName = ConfigFileReader.getUserName();
        password = ConfigFileReader.getPassword();

    }

    public static String getRandomJoke() {
        ArrayList<String> jokes = new ArrayList<>();
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from jokes;");
            while (rs.next())
            {
                jokes.add(rs.getString(2));
            }//end while
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        int index = (int) (Math.random() * (jokes.size() - 1 - 0) + 0);
        return jokes.get(index);
    }

    public static ArrayList<String> getSubscribersAccountNamesByPhoneNumber(String phoneNumber) {
        ArrayList<String> accountNames = new ArrayList<>();
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from smssubscribers where phonenumber = '" + phoneNumber + "';");
            while (rs.next())
            {
                accountNames.add(rs.getString(2));
            }//end while
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return accountNames;
    }

    public static boolean isPhoneNumberSubscribed(String phoneNumber) {
        boolean foundOne = false;
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from smssubscribers where phonenumber = '" + phoneNumber + "';");
            while (rs.next())
            {
                System.out.println(rs.getString(2));
                foundOne = true;
            }//end while
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return foundOne;
    }

    public static void deleteSmsMsgFromQueueById(int pid) {
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);

            Statement stmt = con.createStatement();
            stmt.executeUpdate("delete from smsmsgqueue where pid = " + pid + ";");

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }//end 

    public static void deleteSmsMsgFromQueueByPhoneNumber(String phoneNumber) {
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);

            Statement stmt = con.createStatement();
            stmt.executeUpdate("delete from smsmsgqueue where phonenumber = '" + phoneNumber + "';");

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }//end 

    public static void deleteSmsSubscriberByPhoneNumber(String phoneNumber) {
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);

            Statement stmt = con.createStatement();
            stmt.executeUpdate("delete from smssubscribers where phonenumber = '" + phoneNumber + "';");

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }//end 

    public static void insertClockData(ClockData cd) {
        try
        {
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Connection con = DriverManager.getConnection(
                    host, userName, password);

            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO `timeclockdata` (`pid`,`employeeRFID`,`timedata`,`clockedIn`) VALUES (NULL, '" + cd.getRFID() + "','" + sdf.format(cd.getTime()) + "'," + cd.getClockedIn() + ");");

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }//end insertClockData

    public static String getEmployeeRFIDByPID(int pid) {
        String temp = "";
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select emprfid from employees where pid = '" + pid + "';");

            while (rs.next())
            {

                temp = rs.getString(1);
            }//end while

            con.close();
            return temp;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return temp;
    }

    public static ClockData getLastValidClockDataByRFID(String rfid) {
        ClockData data = null;
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from timeclockdata where employeeRFID = '" + rfid + "';");

            while (rs.next())
            {
                LocalDateTime temp = rs.getTimestamp(2).toLocalDateTime();
                if (data == null || temp.isAfter(data.getTime()))//get the most recent date.
                {
                    data = new ClockData(rs.getTimestamp(2).toLocalDateTime(), rs.getBoolean(3), rs.getString(4));
                }
            }//end while

            con.close();
            return data;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return data;
    }

    //returns the clock data arraylist in sorted chronological order
    public static ArrayList<ClockData> getClockDateForDateRange(LocalDateTime start, LocalDateTime end, String rfid) {
        ArrayList<ClockData> cdList = new ArrayList<>();
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            //System.out.println("select * from timeclockdata where employeeRFID = '" + rfid + "' and timedata >= '" + sdf.format(start) + "' and timedata <= '" + sdf.format(end) + "' order by timedata;");
            ResultSet rs = stmt.executeQuery("select * from timeclockdata where employeeRFID = '" + rfid + "' and timedata >= '" + sdf.format(start) + "' and timedata <= '" + sdf.format(end) + "' order by timedata;");

            while (rs.next())
            {
                cdList.add(new ClockData(rs.getTimestamp(2).toLocalDateTime(), rs.getBoolean(3), rs.getString(4)));

            }//end while

            con.close();
            return cdList;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        return cdList;
    }

    public static ArrayList<Integer> getEmployeesPIDs() {
        try
        {
            ArrayList<Integer> bigList = new ArrayList<>();
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select pid from employees order by empname;");
            while (rs.next())
            {
                if (rs.getInt(1) != 12 && rs.getInt(1) != 14 && rs.getInt(1) != 15 && rs.getInt(1) != 10 && rs.getInt(1) != 11)//manually exempt employees from clock in
                {
                    bigList.add(rs.getInt(1));
                }
            }//end while
            con.close();
            return bigList;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    public static String getEmployeeNameByPID(int pid) {
        String temp = "";
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select empname from employees where pid = '" + pid + "';");

            while (rs.next())
            {

                temp = rs.getString(1);
            }//end while

            con.close();
            return temp;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return temp;
    }//end getEmployees
}
