package smithstimeclock;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**

 @author R-Mule
 */
public class COMHandler implements SerialPortEventListener {

    static boolean canSend = true;
    SerialPort serialPort;
    OutputStream out;
    InputStream in;
    final char ENTER = 13;
    private byte[] buffer = new byte[1024];
    public LocalDateTime latestMessageTime;
    public String latestMessage;

    public COMHandler() {
        super();
    }

    public void closeSerial() {
        serialPort.close();
    }

    public void serialEvent(SerialPortEvent arg0) {
        int data;
        try
        {
            int len = 0;
            while ((data = in.read()) > -1)
            {
                if (data == '\n')
                {
                    break;
                }
                buffer[len++] = (byte) data;
            }
            String input = new String(buffer, 0, len);
            //System.out.println(input);
            latestMessageTime = LocalDateTime.now();
            latestMessage = input.trim();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void connect(String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned())
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

            if (commPort instanceof SerialPort)
            {
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    public boolean sendMessage(String number, String msg) {
        try
        {

            boolean failed = true;
            int cntr = 0;
            while (failed)
            {
                if (cntr == 10)
                {
                    return false;//It failed!
                }
                cntr++;
                LocalDateTime timeSent = LocalDateTime.now();
                out.write(("AT*SMSM2M =\"" + number + " " + msg + "\"").getBytes());
                out.write(ENTER);
                try
                {
                    Thread.sleep(2000);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(COMHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                //if ()
                //  {
                //    System.out.println("Did not receive response");
                //do something
                //}
                System.out.println("Latest Message: " + latestMessage);
                System.out.println("Time: " + latestMessageTime);
                if (latestMessage.contentEquals("OK") && latestMessageTime.isAfter(timeSent))
                {
                    failed = false;
                }
            }
            return true;
        }
        catch (IOException ex)
        {
            Logger.getLogger(COMHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
