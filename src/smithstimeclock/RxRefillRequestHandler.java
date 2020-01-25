package smithstimeclock;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JTextPane;
import javax.swing.Timer;

/**

 @author R-Mule
 */
public class RxRefillRequestHandler {

    protected String printerName;
    protected static ArrayList<PatientRxRefillRequest> patientRequests;
    
    //protected PrinterService printerService = new PrinterService();

    public RxRefillRequestHandler() {
        printerName = ConfigFileReader.getRxRefillPrinterName();
        patientRequests = new ArrayList<>();
    }

    public boolean receiveRxRequest(String phoneNumber, String rxNumber) {
        PatientRxRefillRequest tempRequest = null;
        for (PatientRxRefillRequest request : patientRequests)
        {
            if (request.phoneNumber.contentEquals(phoneNumber))
            {
                for (String rxTemp : request.rxNumbers)
                {
                    if (rxTemp.contentEquals(rxNumber))
                    {
                        return false;
                    }
                }
                request.requestTimer.restart();
                request.addRx(rxNumber);
                tempRequest = request;
            }

        }

        if (tempRequest == null)
        {//300000ms  = 5 minutes. 1000ms = 1 sec.
            Timer requestTimer = new Timer(300000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    timerTriggered(phoneNumber);
                }
            });

            ArrayList<String> accountNames = Database.getSubscribersAccountNamesByPhoneNumber(phoneNumber);//Get account names from database here.
            patientRequests.add(new PatientRxRefillRequest(phoneNumber, accountNames, rxNumber, requestTimer));
            requestTimer.start();
        }
        return true;
    }

    public void timerTriggered(String phoneNumber) {
        PatientRxRefillRequest foundRequest = null;
        for (PatientRxRefillRequest request : patientRequests)
        {
            if(phoneNumber.contentEquals(request.phoneNumber))
            {
                foundRequest = request;
                break;
            }
        }

        patientRequests.remove(foundRequest);
        String msgToPrint;
        String accountNames = "";
        String rxNumbers = "";
        for (String accountName : foundRequest.accountNames)
        {
            accountNames += accountName + "\n";
        }
        for (String rxNumber : foundRequest.rxNumbers)
        {
            rxNumbers += rxNumber + "\n";
        }
        msgToPrint = "Phone Number: " + foundRequest.phoneNumber + "\nPossible Account Names:\n" + accountNames + "\nRx Numbers Received:\n" + rxNumbers + "\nPlease remember to contact the phone number given if an Rx number is incorrect or cannot be filled for some reason.\n\n\n\n\n\n\n\n\n\n\n\n\n\n";

        JTextPane jtp = new JTextPane();

        jtp.setBackground(Color.white);
        jtp.setText(msgToPrint);
        boolean show = false;
        try
        {
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(
                    null, null);
            PrintService service = findPrintService(printerName, printService);
            jtp.print(null, null, show, service, null, show);
        }
        catch (java.awt.print.PrinterException ex)
        {
            ex.printStackTrace();
        }
        //Call printer on this ticket now please, just send the entire patient request..
        System.out.println("Hello Timer Trigger");
        foundRequest.requestTimer.stop();
    }

    private static PrintService findPrintService(String printerName,
            PrintService[] services) {
        for (PrintService service : services)
        {
            if (service.getName().equalsIgnoreCase(printerName))
            {
                return service;
            }
        }

        return null;
    }
}
