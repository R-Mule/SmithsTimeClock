package smithstimeclock;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**

 @author R-Mule
 */
public class MainFrame extends javax.swing.JFrame {

    protected ArrayList<Employee> employees = new ArrayList<>();
    DateTimeFormatter sdf = DateTimeFormatter.ofPattern("h:mm:ss a    EEEE, MMMM d, yyyy");
    private JLabel clock = new JLabel("", SwingConstants.LEFT);
    private JLabel versionHeader = new JLabel("Version 1.0.0", SwingConstants.LEFT);
    Timer timer;
    TopMenuBar menuBar;
    private JTextField textField = new JTextField(10);


      
    public MainFrame() {
        
        init();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
       // setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
       // setResizable(false);
       // this.set
        //addLabels();
        loadEmployees();
        menuBar = new TopMenuBar(this,employees);//Hollie's Menu Bar!
        this.setJMenuBar(menuBar);
        textField.setBounds(100, 950, 100, 20);
        //textField.isVisible(true);
        textField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (textField.getText().matches("[0-9][0-9][0-9],[0-9][0-9][0-9][0-9][0-9]"))//if it is a long
                {
                    String rfid = textField.getText();
                    Employee employee = null;
                    for (Employee emp : employees)
                    {
                        if (emp.getRFID().contentEquals(rfid))
                        {
                            employee = emp;
                        }
                    }
                    if (employee != null)
                    {//is this a valid RFID
                        // ClockData cd = Database.getLastValidClockDataByRFID(rfid);
                        ClockData cd;
                        if (employee.getClockData().getClockedIn())
                        {//They need clocked out
                            cd = new ClockData(LocalDateTime.now(), false, rfid);
                        }
                        else
                        {//they need clocked in
                            cd = new ClockData(LocalDateTime.now(), true, rfid);
                        }
                        Database.insertClockData(cd);
                        employee.setClockData(cd);
                        
                        textField.setText("");
                    }
                }

                textField.setText("");
            }
        });
        this.add(textField);
    }


    private void loadEmployees() {
        //For all employees from database
        int cntr = 75;
        for (int pid : Database.getEmployeesPIDs())
        {
            Employee temp = new Employee(pid);
            temp.setPosition(800, cntr + 50);
            temp.setLabel();
            this.add(temp.getLabel());
            this.add(temp.getStatusLabel());
            employees.add(temp);

            cntr += 50;
        }
    }//end loadEmployees

    private void init() {

        updateClock();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                updateClock();
            }
        });
        timer.start();
        
        //versionHeader
        versionHeader.setLocation(1800, 950);
        versionHeader.setSize(250, 50);
        versionHeader.setFont(new Font(versionHeader.getName(), Font.BOLD, 12));
        versionHeader.setVisible(true);
        this.add(versionHeader);
        clock.setVisible(true);
        clock.setLocation(600, 50);
        clock.setSize(1000, 50);
        clock.setFont(new Font(clock.getName(), Font.BOLD, 30));
        this.add(clock);
        this.setTitle("Smith's Time Clock - Developed by: Andrew & Hollie Smith");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1920, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1080, Short.MAX_VALUE)
        );

        pack();
    }

    private void updateClock() {
        
        clock.setText(sdf.format(LocalDateTime.now()));

    }
}
