

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import javax.mail.Authenticator;
import javax.mail.Message;

 
public class email {
 
    public static void sendEmailWithAttachments(String host, String port,
            final String userName, final String password, String to,
            String subject, String message, String[] attachFiles)
            throws AddressException, MessagingException {
        // sets SMTP server properties
        Properties properties = new Properties();
        //user
        properties.put("mail.user", userName);
        //password
        properties.put("mail.password", password);
        //port
        properties.put("mail.smtp.port", port);
        //host
        properties.put("mail.smtp.host", host);
        //atuthenticator
        properties.put("mail.smtp.auth", "true");
        
        properties.put("mail.smtp.starttls.enable", "true");
        
       
 
        // new authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        //get properties for the session
        Session session = Session.getInstance(properties, auth);
 
        // creates new email
        Message msg = new MimeMessage(session);
 //set email properties
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] addrs = { new InternetAddress(to) };
        msg.setRecipients(Message.RecipientType.TO, addrs);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
 
       
        MimeBodyPart BodyPart = new MimeBodyPart();
        BodyPart.setContent(message, "text/html");
 
        //multipart
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(BodyPart);
 
        // adds attachments
        if (attachFiles != null && attachFiles.length > 0) {
            for (String filePath : attachFiles) {
                MimeBodyPart attachPart = new MimeBodyPart();
 
                try {
                    attachPart.attachFile(filePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
 
                multipart.addBodyPart(attachPart);
            }
        }
 
        // sets the multi-part as e-mail's content
        msg.setContent(multipart);
 
        // sends the e-mail
        Transport.send(msg);
 
    }
 
    /**
     * Test sending e-mail with attachments
     */
    public email() {
        // SMTP info
        String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "tiaguinhofsx@gmail.com";
        String password = "freesport1";
 
        // message info
        String mailTo = "tiagocf_13@hotmail.com";
        String subject = "Potential Intruder";
        String message = "A potential intruder has been detected, open the detected frame attached and find out.";
 
        // attach detection
        String[] attachFiles = new String[1];
        attachFiles[0] = "c:/Users/Tiago/Desktop/projectPrograming/testCam/MyFile.png";
        
 
        try {
            sendEmailWithAttachments(host, port, mailFrom, password, mailTo,
                subject, message, attachFiles);
            System.out.println("Email sent.");
        } catch (Exception ex) {
            System.out.println("Could not send email.");
            ex.printStackTrace();
        }
    }
}