/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smithstimeclock;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.IOException;
import java.time.LocalDateTime;

/**

 @author R-Mule
 */
public class COMHandler2 implements SerialPortDataListener {

    SerialPort comPort;
    String stringBuffer;
    final char ENTER = 13;

    COMHandler2() {
        comPort = SerialPort.getCommPort(ConfigFileReader.getGSMModemCOMPort());
        comPort.setComPortParameters(115200, 8, 1,0);
        comPort.openPort();
        
        System.out.println("COM port open: " + comPort.getDescriptivePortName());
        comPort.addDataListener(this);
        System.out.println("Event Listener open.");

    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        //System.out.println("In event listener.");
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
        {
            return;
        }
        //System.out.println("Past event type check.");
        byte[] newData = new byte[comPort.bytesAvailable()];
        int numRead = comPort.readBytes(newData, newData.length);
        stringBuffer = new String(newData, 0, numRead);
        //System.out.println("Read " + numRead + " bytes.");
        System.out.println(stringBuffer);

    }

    public boolean sendMessage(String number, String msg) {
      //  try
      //  {

            boolean failed = true;
            int cntr = 0;
            // while (failed)
            // {
            if (cntr == 10)
            {
                return false;//It failed!
            }
            cntr++;
            LocalDateTime timeSent = LocalDateTime.now();
            byte[] buffer = ("AT*SMSM2M =\"" + number + " " + msg + "\"").getBytes();
            comPort.writeBytes(buffer, buffer.length);
            comPort.writeBytes("\n".getBytes(), "\n".getBytes().length);
            try
            {
                Thread.sleep(2000);
            }
            catch (InterruptedException ex)
            {
               // Logger.getLogger(COMHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            //if ()
            //  {
            //    System.out.println("Did not receive response");
            //do something
            //}
            // System.out.println("Latest Message: " + latestMessage);
            //  System.out.println("Time: " + latestMessageTime);
            // if (latestMessage.contentEquals("OK") && latestMessageTime.isAfter(timeSent))
            // {
            //      failed = false;
            //  }
            // }
            return true;
             }
       //     catch (IOException ex)
      //  {
            //Logger.getLogger(COMHandler.class.getName()).log(Level.SEVERE, null, ex);
      //  }
         //   return false;
        //}
    }
