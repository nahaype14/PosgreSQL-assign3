package Logining;

import DB.User;

import java.util.Properties;
import java.util.Date;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.Transport;

public class SendMail {
    private static final String from = "tmtmtm1423@gmail.com";
    private static final String password = "gcqfaywnuxuzflca";

    public SendMail(String code, String email) {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.ssl.enable", true);
        props.put("mail.smtp.auth", true);
        props.put("mail.debug", true);

        Session session = Session.getInstance(props);

        try {
            MimeMessage msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(from));

            InternetAddress[] address = {new InternetAddress(email)};
            msg.setRecipients(Message.RecipientType.TO, address);

            msg.setSubject("Email code check");
            msg.setSentDate(new Date());

            msg.setText(code);

            Transport tr = session.getTransport();
            //Transport.send(msg);
            tr.connect(from, password);
            tr.sendMessage(msg, msg.getAllRecipients());
            tr.close();

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
