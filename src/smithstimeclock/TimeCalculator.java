package smithstimeclock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;


/**

 @author R-Mule
 */
public class TimeCalculator {

    private LocalDateTime start;
    private LocalDateTime end;
    private String rfid;

    TimeCalculator(LocalDateTime start, LocalDateTime end, String rfid) {
        this.start = start;
        this.end = end;
        this.rfid = rfid;
    }

    //This should eventually return HH:MM:SS maybe in a different format actually  maybe it should just return a number in seconds.
    private long getTotalSeconds() {
        ArrayList<ClockData> cdList = Database.getClockDateForDateRange(start, end, rfid);
        //sort ins from outs and map the whole thing then do the math
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        long totalSeconds = 0;
        boolean startTimeFound = false;//this stops us from starting on a clockout, probably would never happen, but still.
        for (ClockData cd : cdList)
        {
            if (cd.getClockedIn())
            {
                startTime = cd.getTime();
                startTimeFound = true;
            }
            else if (startTimeFound)
            {
                endTime = cd.getTime();
                Duration duration = Duration.between(startTime, endTime);
                
                totalSeconds +=duration.toMillis()/1000;  // endTime - startTime;//getDateDiff(startTime, endTime, TimeUnit.SECONDS);
                startTimeFound = false;
                startTime = null;//Reset.
                endTime = null;
            }
        }
        return totalSeconds;

    }
    
    public String getFormattedTotalTimeWorked(){
        long seconds = getTotalSeconds();
        long hours = seconds/60/60;
        
        seconds = seconds - hours*60*60;
        
        long minutes = seconds/60;
        seconds = seconds - minutes*60;
        
        String hoursFinal = Long.toString(hours);
        String minutesFinal= Long.toString(minutes);
        String secondsFinal= Long.toString(seconds);
        if(hours<=9){
            hoursFinal = 0+hoursFinal;
        }
        if(minutes<=9){
            minutesFinal = 0+minutesFinal;
        }
        if(seconds<=9){
        secondsFinal = 0+secondsFinal;
        }
        
        return hoursFinal+":"+minutesFinal+":"+secondsFinal;
    }

}
