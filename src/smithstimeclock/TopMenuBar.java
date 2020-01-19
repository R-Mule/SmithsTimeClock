package smithstimeclock;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

/**

 @author hfull This menu bar spans the top of the screen and should have menu
 items that drop down.
 */
public class TopMenuBar extends JMenuBar {

    //Class Variables
    JMenu reports, system;
    JMenuItem allEmployees, testSmsOut, testPrint;
    MainFrame mf;
    ArrayList<Employee> employees;
    DBListener dbl;

    //ctor
    public TopMenuBar(MainFrame mf, ArrayList<Employee> employees) {
        this.employees = employees;
        this.mf = mf;
        reports = new JMenu("Reports");
        reports.setMnemonic(KeyEvent.VK_R);
        reports.setVisible(true);
        system = new JMenu("System");
        system.setMnemonic(KeyEvent.VK_S);
        system.setVisible(true);

//Add  menu items
        allEmployees = new JMenuItem();
        allEmployees.setText("All Employees");
        allEmployees.setVisible(true);
        reports.add(allEmployees);

        //testSmsOut = new JMenuItem();
        //testSmsOut.setText("Test SMS Out");
        //testSmsOut.setVisible(true);
       //system.add(testSmsOut);

        testPrint = new JMenuItem();
        testPrint.setText("Test Printer");
        testPrint.setVisible(true);
        system.add(testPrint);

        this.add(reports);
        this.add(system);

 
//Add menu action listeners
        allEmployees.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allEmployeesActionPerformed(evt);
            }
        });
        
        testPrint.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                       JTextPane jtp = new JTextPane();
        String msgToPrint = "This has been a successful test page print... obviously... Toss a coin to your Witcher\n"
                + "O’ Valley of Plenty\n"
                + "O’ Valley of Plenty\n"
                + "O’\n"
                + "Toss a coin to Your Witcher\n"
                + "O’ Valley of Plenty\n"
                + "At the edge of the world\n"
                + "Fight the mighty horde\n"
                + "That bashes and breaks you\n"
                + "And brings you to mourn\n"
                + "He thrust every elf\n"
                + "Far back on the shelf\n"
                + "High up on the mountain\n"
                + "From whence it came\n"
                + "He wiped out your pest\n"
                + "Got kicked in his chest\n"
                + "He’s a friend of humanity\n"
                + "So give him the rest\n"
                + "That’s my epic tale\n"
                + "Our champion prevailed\n"
                + "Defeated the villain\n"
                + "Now pour him some ale\n"
                + "Toss a coin to your Witcher\n"
                + "O’ Valley of Plenty\n"
                + "O’ Valley of Plenty\n"
                + "O’\n"
                + "Toss a coin to your Witcher\n"
                + "And friend of humanity\n"
                + "Toss a coin to your Witcher\n"
                + "O’ Valley of Plenty\n"
                + "O’ Valley of Plenty\n"
                + "O’\n"
                + "And friend of humanity\n"
                + "Toss a coin to your Witcher\n"
                + "O’ Valley of Plenty\n"
                + "O’ Valley of Plenty\n"
                + "O’\n"
                + "Toss a coin to your Witcher\n"
                + "A friend of humanity";
        jtp.setBackground(Color.white);
        jtp.setText(msgToPrint);
        boolean show = false;
        try
        {
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(
                    null, null);
            PrintService service = findPrintService(ConfigFileReader.getRxRefillPrinterName(), printService);
            jtp.print(null, null, show, service, null, show);
        }
        catch (java.awt.print.PrinterException ex)
        {
            ex.printStackTrace();
        }
            }
        });
    }//end ctor

    public void setDbl(DBListener dbl) {
        this.dbl = dbl;
    }

    //members/functions
//Addition menu item functions
    private void allEmployeesActionPerformed(java.awt.event.ActionEvent evt) {
        DateRangeSelector drs = new DateRangeSelector();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ArrayList<LocalDate> daysChosen = new ArrayList<>();

        if (drs.validDates())//Time to generate the report! We have valid date ranges!
        {

            //Date data is all loaded into arrays now.
            LocalDate temp = drs.getStartDate().toLocalDate();
            do
            {
                daysChosen.add(temp);
                temp = temp.plusDays(1);
            }
            while (temp.isBefore(drs.getEndDate().toLocalDate().plusDays(1)));//because we need to go one past to hit last day.
            //days chosen are added.
            String reportContent = "";
            for (Employee employee : employees)
            {

                ArrayList<ClockData> cdList = Database.getClockDateForDateRange(drs.getStartDate(), drs.getEndDate(), employee.getRFID());
                for (LocalDate dayChosen : daysChosen)
                {
                    ArrayList<ClockData> singleDayEmpList = new ArrayList<>();
                    boolean foundDay = false;
                    boolean isFirstDay = true;
                    for (ClockData cd : cdList)
                    {
                        if (cd.getTime().toLocalDate().equals(dayChosen))
                        {
                            foundDay = true;
                            if (isFirstDay && !cd.getClockedIn())
                            {//if its the first thing of the day and you're not clocked in....whoops you must have forgot to clock out!            
                            }
                            else
                            {//go ahead and add it.
                                singleDayEmpList.add(cd);
                            }
                            isFirstDay = false;
                            //System.out.println(cd.getClockedIn()+" "+cd.getTime()+" "+employee.getName());//Need to do ALL the strings for this day now,
                            //singleDayEmpList.add(cd);
                        }
                    }
                    if (!foundDay)
                    {
                        reportContent += employee.getDatabasePID() + "  " + dayChosen.format(dateFormat) + "  00:00:00  00:00:00  00:00:00\n";
                        //System.out.println(employee.getDatabasePID() + "  " + dayChosen.format(dateFormat) + "  00:00:00  00:00:00 00:00:00+\n");
                    }
                    else
                    {
                        reportContent += getDailyReportString(employee, singleDayEmpList, dateFormat, timeFormat);
                        //System.out.print(getDailyReportString(employee, singleDayEmpList, dateFormat, timeFormat));
                    }
                }
            }
            if (!reportContent.isEmpty())
            {
                FileWriter fw = new FileWriter();
                String fileName = drs.getStartDate().format(dateFormat) + "_" + drs.getEndDate().format(dateFormat) + "_TimeData.txt";
                String path = ConfigFileReader.getTimeDataPath();
                if (fw.writeFile(path + fileName, reportContent))
                {//if we wrote the file successfuly. Email it!
                    MailSender ms = new MailSender();
                    try
                    {
                        ms.sendMail("Time Report", "File Attached", path + fileName, fileName);
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Mail Sent!");
                    }
                    catch (Exception ex)
                    {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Mail has failed to send.");
                        Logger.getLogger(TopMenuBar.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Failed to write the file. Notify Drew.");
                }
            }
            else
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "I did not find data. This is bad. Report to Drew.");
                //print error no content to report for date range TODO
            }

            System.out.println();
        }

    }

    private String getDailyReportString(Employee employee, ArrayList<ClockData> cd, DateTimeFormatter dateFormat, DateTimeFormatter timeFormat) {

        String totalDayData = "";
        int size = cd.size();
        for (int i = 0; i < size; i += 2)
        {
            if (size % 2 == 1 && i >= size - 1)
            {
                totalDayData += employee.getDatabasePID() + "  " + cd.get(i).getTime().format(dateFormat) + "  " + cd.get(i).getTime().format(timeFormat) + "  00:00:00  00:00:00";
            }
            else
            {
                TimeCalculator tc = new TimeCalculator(cd.get(i).getTime(), cd.get(i + 1).getTime(), employee.getRFID());
                String temp = tc.getFormattedTotalTimeWorked();
                totalDayData += employee.getDatabasePID() + "  " + cd.get(i).getTime().format(dateFormat) + "  " + cd.get(i).getTime().format(timeFormat) + "  " + cd.get(i + 1).getTime().format(timeFormat) + "  " + temp;
            }
            if (i < size)
            {
                totalDayData += "\n";
            }
        }
        return totalDayData;
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
