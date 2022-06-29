package com.springleaf.messaging;

import com.springleaf.object.entity.User;

import javax.mail.MessagingException;
import java.util.Map;

public interface MessageChannel {
    public void send (User target, Map<String, String> data) throws MessagingException;
}
