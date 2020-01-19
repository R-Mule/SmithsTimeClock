package smithstimeclock;

import java.time.LocalDateTime;
/**

 @author R-Mule
 */
public class ClockData {

    private LocalDateTime clockTime;
    private boolean clockedIn;
    private String rfid;

    public ClockData(LocalDateTime clockTime, boolean clockedIn, String rfid) {
        this.clockTime = clockTime;
        this.clockedIn = clockedIn;
        this.rfid = rfid;
    }

    public ClockData() {
        clockedIn = false;
    }

    public LocalDateTime getTime() {
        return clockTime;
    }

    public boolean getClockedIn() {
        return clockedIn;
    }

    public String getRFID() {
        return rfid;
    }

    public void setClockedIn(boolean clockedIn) {
        this.clockedIn = clockedIn;
    }
}
