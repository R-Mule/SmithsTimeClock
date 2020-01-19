
package smithstimeclock;

import java.util.ArrayList;
import javax.swing.Timer;

/**

 @author R-Mule
 */
public class PatientRxRefillRequest {
    protected String phoneNumber;
    protected ArrayList<String> accountNames = new ArrayList<>();
    protected ArrayList<String> rxNumbers = new ArrayList<>();
    protected Timer requestTimer;
    
    public PatientRxRefillRequest(String phoneNumber, ArrayList<String> accountNames, String rxNumber, Timer timer)
    {
        this.phoneNumber = phoneNumber;
        this.accountNames = accountNames;
        rxNumbers.add(rxNumber);
        this.requestTimer = timer;
    }
    
    public void addRx(String rxNumber)
    {
        this.rxNumbers.add(rxNumber);
    }
}
