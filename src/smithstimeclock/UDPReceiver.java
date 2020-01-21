package smithstimeclock;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**

 @author R-Mule
 */
public class UDPReceiver {

    DBListener dbl;
    RxRefillRequestHandler refillReqHandler = new RxRefillRequestHandler();

    public UDPReceiver(DBListener dbl) {
        this.dbl = dbl;
        init();
    }

    private void init() {
        int port = 17342;

        // Create a socket to listen on the port.
        DatagramSocket dsocket;
        try
        {
            dsocket = new DatagramSocket(port);

            // Create a buffer to read datagrams into. If a
            // packet is larger than this buffer, the
            // excess will simply be discarded!
            byte[] buffer = new byte[2048];

            // Create a packet to receive data into the buffer
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // Now loop forever, waiting to receive packets and printing them.
            while (true)
            {
                // Wait to receive a datagram
                dsocket.receive(packet);

                // Convert the contents to a string, and display them
                String msg = new String(buffer, 0, packet.getLength());
                // System.out.println(packet.getAddress().getHostName() + ": "
                //           + msg);

                // Reset the length of the packet before reusing it.
                packet.setLength(buffer.length);
                if (msg.contains("<<<") && msg.contains(">>>"))
                {
                    String phoneNumber = msg.substring(msg.indexOf("<<<") + 3, msg.indexOf(','));
                    String tempMsg = msg.substring(msg.lastIndexOf(',') + 1);
                    tempMsg = tempMsg.substring(0, tempMsg.indexOf('>'));
                    String msgContent = getContent(tempMsg);
                    //System.out.println(msgContent);
                    if (packet.getAddress().getHostName().contentEquals(ConfigFileReader.getGSMModemIP()))
                    {
                        if (msgContent.toUpperCase().trim().contains("HOURS"))
                        {
                            dbl.sendForcedMessage(phoneNumber, "Smith's Super-Aid Normal Business Hours: Mon->Fri: 9:00AM to 7:00PM. Sat: 9:00AM to 2:00PM. Sun: Closed. Please call (540) 726-2993 for Holiday Hours.");
                        }
                        else if (msgContent.toUpperCase().trim().contains("HELP"))
                        {
                            dbl.sendForcedMessage(phoneNumber, "Text STOP to unsubscribe. Text CONTACT for contact information. Text HOURS for non-holiday business hours. Text Rx # for refill.");
                        }
                        else if (msgContent.toUpperCase().trim().contains("CONTACT"))
                        {
                            dbl.sendForcedMessage(phoneNumber, "Smith's Super-Aid Contact Information: Main Line: (540) 726-2993. Office: (540) 726-2113. Durable Medical Equipment (540) 726-7486. Fax: (540) 726-7331");
                        }
                        else if (msgContent.toUpperCase().trim().contains("FUNNY"))
                        {
                            dbl.sendForcedMessage(phoneNumber, Database.getRandomJoke());
                        }
                        else if (Database.isPhoneNumberSubscribed(phoneNumber))
                        {
                            if (msgContent.toUpperCase().trim().contentEquals("STOP"))
                            {
                                Database.deleteSmsSubscriberByPhoneNumber(phoneNumber);
                                Database.deleteSmsMsgFromQueueByPhoneNumber(phoneNumber);
                                dbl.sendForcedMessage(phoneNumber, "You have been successfully unsubscribed.");
                            }

                            else if (msgContent.matches("[1-9][0-9][0-9][0-9][0-9][0-9][0-9]"))//If it is a legit rx number best we know.
                            {

                                if (refillReqHandler.receiveRxRequest(phoneNumber, msgContent))
                                {
                                    dbl.sendForcedMessage(phoneNumber, "Rx Refill Request Received For: " + msgContent);
                                }
                                else
                                {
                                    dbl.sendForcedMessage(phoneNumber, "You have already messaged Rx Number: " + msgContent);
                                }

                            }
                            else
                            {
                                dbl.sendForcedMessage(phoneNumber, "Invalid Message. Please enter \"STOP\" to unsubscribe or send single Rx number per text to request refill.");
                            }
                        }
                        else
                        {
                            dbl.sendForcedMessage(phoneNumber, "This phone number is not subscribed. Please contact (540) 726-2993 for information on subscribing.");
                        }
                    }//end proper IP check
                }//End syntax check
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(UDPReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getContent(String hex) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2)
        {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));

        }
        return output.toString();
    }
}
