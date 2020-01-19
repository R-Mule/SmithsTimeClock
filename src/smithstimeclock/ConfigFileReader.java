package smithstimeclock;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**

 @author A.Smith
 */
public class ConfigFileReader {

    private static String printerName;//= "EPSON TM-T20II Receipt";
    private static String registerID;// = "D";
    private static String hostName;
    private static String userName;
    private static String password;
    private static String remoteDrivePath;
    private static String registerReportPath;
    private static String displayComPort;
    private static String cardReaderURL;
    private static String pharmacyName;
    private static String mailPassword;
    private static String jarPath;
    private static String timeDataPath;
    private static String gsmModemComPort;
    private static String gsmModemIP;
    private static String rxRefillPrinterName;

    private ConfigFileReader() //do not instantiate
    {

    }

    public static void loadConfiguration() throws FileNotFoundException, IOException {
        try
        {

            BufferedReader in = new BufferedReader(new FileReader("C:\\POS\\Config.txt"));

            String line;

            while ((line = in.readLine()) != null)
            {
                String[] tokens = line.split(":", 2);   //limits the split to 2 array elements ie only the first occurance so it will keep any colons in the value portion

                if (tokens.length < 2) //not all data is there just move along
                {
                    continue;
                }
                System.out.println(tokens[0] + " " + tokens[1]);
                if (tokens[0].contentEquals("Register ID"))
                {
                    registerID = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Printer Name"))
                {
                    printerName = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Database Hostname"))
                {
                    hostName = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Database Username"))
                {
                    userName = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Database Password"))
                {
                    password = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Remote Drive Path"))
                {
                    remoteDrivePath = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Register Report Path"))
                {
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("MMddyy");
                    registerReportPath = tokens[1].trim() + dateFormat.format(date);
                }
                else if (tokens[0].contentEquals("Display Com Port"))
                {
                    displayComPort = tokens[1].trim();
                    // System.out.println(displayComPort);
                }
                else if (tokens[0].contentEquals("Card Terminal Address"))
                {
                    cardReaderURL = tokens[1].trim();

                }
                else if (tokens[0].contentEquals("Pharmacy Name"))
                {
                    pharmacyName = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Mail Password"))
                {
                    mailPassword = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Jar Path"))
                {
                    jarPath = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Time Data Path"))
                {
                    timeDataPath = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("GSM Modem COM Port"))
                {
                    gsmModemComPort = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("GSM Modem IP"))
                {
                    gsmModemIP = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Rx Refill Printer Name"))
                {
                    rxRefillPrinterName = tokens[1].trim();
                }
            }//end while

        }
        catch (FileNotFoundException e)
        {
            throw e;
            //System.out.println("The file could not be found or opened");
        }
        catch (IOException e)
        {
            throw e;
            //System.out.println("Error reading the file");
        }

    }

    public static String getGSMModemIP() {
        return gsmModemIP;
    }

    public static String getGSMModemCOMPort() {
        return gsmModemComPort;
    }

    public static String getRxRefillPrinterName() {
        return rxRefillPrinterName;
    }
        
    public static String getTimeDataPath() {
        return timeDataPath;
    }

    public static String getJarPath() {
        return jarPath;
    }

    public static String getPharmacyName() {
        return pharmacyName;
    }

    public static String getCardReaderURL() {
        return cardReaderURL;
    }

    public static String getDisplayComPort() {
        return displayComPort;
    }

    public static String getRegisterReportPath() {
        return registerReportPath;
    }

    public static String getRegisterID() {
        return registerID;
    }

    public static String getPrinterName() {
        return printerName;
    }

    public static String getHostName() {
        return hostName;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public static String getRemoteDrivePath() {
        return remoteDrivePath;
    }

    public static String getMailPassword() {
        return mailPassword;
    }
}
