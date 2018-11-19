package smithstimeclock;

import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 i will clean this up later..

 @author R-Mule
 */
public class MailSender {

    private String USERNAME;
    private String PASSWORD;

    public MailSender() {
        USERNAME = "asmithpbe";
        PASSWORD = ConfigFileReader.getMailPassword();

    }

    public void sendMail(String mailSubject,
            String mailText, String attachmentPath, String attachmentFileName) throws Exception {

        Properties config = createConfiguration();

        // Creates a mail session. We need to supply username and
        // password for Gmail authentication.
        Session session = Session.getInstance(config, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        USERNAME,
                        PASSWORD
                );
            }
        });

        // Creates email message
        Message message = new MimeMessage(session);
        message.setSentDate(new Date());
        message.setFrom(new InternetAddress("asmithpbe@gmail.com"));
        message.setRecipient(Message.RecipientType.TO,
                new InternetAddress("superaid1@wvva.net"));
        message.setSubject(mailSubject);
        message.setText(mailText);

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();

        DataSource source = new FileDataSource(attachmentPath);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(attachmentFileName);
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);

        // Send a message
        Transport.send(message, USERNAME, PASSWORD);
    }

    private Properties createConfiguration() {
        return new Properties() {
            {
                put("mail.smtp.auth", "true");
                put("mail.smtp.host", "smtp.gmail.com");
                put("mail.smtp.port", "587");
                put("mail.smtp.starttls.enable", "true");
                put("mail.smtp.timeout", "300000");//5 minute timeout, as per smtp recomendations
                put("mail.smtp.connectiontimeout", "300000");
            }
        };
    }
}
