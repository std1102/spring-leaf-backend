package com.springleaf.messaging;

import com.google.gson.Gson;
import com.springleaf.common.Config;
import com.springleaf.common.DefaultValues;
import lombok.extern.slf4j.Slf4j;
import com.springleaf.object.entity.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class MailChannel implements MessageChannel{

    private final Properties properties = new Properties();
    private String username;
    private String password;

    public MailChannel() {
        username = Config.getProperty(Config.MAIL_ACCOUNT);
        password = Config.getProperty(Config.MAIL_PASSWORD);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", DefaultValues.MAIL_SMTP_HOST);
        properties.put("mail.smtp.port", DefaultValues.MAIL_SMTP_PORT);
    }

    @Override
    public void send(User target, Map<String, String> data) throws MessagingException {
        String targetEmail = target.getEmail();
        log.debug("Send mail to " + targetEmail + ": " + new Gson().toJson(data));
        if ("".equals(username)) {
            throw new MessagingException("Missing mail configuration");
        }
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(targetEmail));
            message.setSubject(data.get("subject"));
            message.setText(data.get("content"));
            Transport.send(message);
            log.debug("Mail sent to " + targetEmail);
        } catch (javax.mail.MessagingException e) {
            throw new MessagingException("Unable to send mail to " + targetEmail, e);
        }
    }
}
