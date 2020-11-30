package tr.com.ogedik.notification.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.com.ogedik.commons.rest.request.model.MailServerProperties;
import tr.com.ogedik.scrumier.proxy.clients.ConfigurationProxy;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/*
 * @author enes.erciyes
 */
@Service
public class AuthenticatedEmailService {
  @Autowired private ConfigurationProxy configurationProxy;

  public Boolean sendEmail(String toEmail, String subject, String body) {
    MailServerProperties properties = configurationProxy.getMailServerConfig();

    System.out.println("TLSEmail Start");
    Properties props = new Properties();
    props.put("mail.smtp.host", properties.getHost()); //SMTP Host
    props.put("mail.smtp.port", properties.getPort()); //TLS Port
    props.put("mail.smtp.auth", "true"); //enable authentication
    props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

    Authenticator auth = new Authenticator() {
      //override the getPasswordAuthentication method
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(properties.getUsername(), properties.getPassword());
      }
    };
    Session session = Session.getInstance(props, auth);
    try{
      MimeMessage message = new MimeMessage(session);
      message.addHeader("Content-type", "text/HTML; charset=UTF-8");
      message.addHeader("format", "flowed");
      message.addHeader("Content-Transfer-Encoding", "8bit");

      message.setFrom(new InternetAddress("info@scrumier.com", "NoReply-Scrumier"));
      message.setReplyTo(InternetAddress.parse("no_reply@scrumier.com", false));
      message.setSubject(subject, "UTF-8");
      message.setSentDate(new Date());
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

      message.setContent(
              body,
              "text/html");

      // Send message
      Transport.send(message);

    }catch (MessagingException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return true;
  }
}
