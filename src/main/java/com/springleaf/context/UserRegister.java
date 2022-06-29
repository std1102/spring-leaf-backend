package com.springleaf.context;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.DefaultValues;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.context.Context;
import io.ebean.Ebean;
import io.ebean.annotation.Transactional;
import com.springleaf.messaging.MailChannel;
import com.springleaf.messaging.MessageChannel;
import com.springleaf.object.entity.Login;
import com.springleaf.object.entity.Role;
import com.springleaf.object.entity.User;
import com.springleaf.object.entity.types.UserStatus;
import org.mindrot.jbcrypt.BCrypt;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping(path = "/user/register", type = RequestType.POST)
public class UserRegister extends Context {

    private String EMAIL = "email";
    private String FIRST_NAME = "first_name";
    private String LAST_NAME = "last_name";
    private String DOB = "dob";
    private String USERNAME = "username";
    private String PASSWORD = "password";
    @Override
    @Transactional
    protected Object _process(Map<String, Object> map) throws ParseException {
        String email = (String) map.get(EMAIL);
        String first_name = (String) map.get(FIRST_NAME);
        String dob = (String) map.get(DOB);
        String username = (String) map.get(USERNAME);
        String password = (String) map.get(PASSWORD);
        String last_name = (String) map.get(LAST_NAME);

        // validate
        if ($.isMultipleEmpty(email, first_name, last_name, dob, username, password)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        if (!DefaultValues.MAIL_PATTERN.matcher(email).matches()) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        if (!DefaultValues.DATE_UPLOAD_PATTERN.matcher(dob).matches()) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }

        User user = new User();
        user.setActive(true);
        Date actualDob = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
        user.setDob(actualDob);
        user.setEmail(email);
        user.setFirst_name(first_name);
        user.setLast_name(last_name);
        user.setStatus(UserStatus.INIT);

        Role role = new Role();
        role.setId(1);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

        Login login = new Login();
        login.setUsername(username);
        String hPass = BCrypt.hashpw(password, BCrypt.gensalt(DefaultValues.SALT));
        login.setPassword(hPass);

        user.setLogin(login);
        user.save();

        // TODO mail
        Thread thread = new Thread(() -> {
            Map<String, String> mailContent = new HashMap<>();
            mailContent.put("subject", "ahihi");
            mailContent.put("content", "hihi ahihi con me may");
            MessageChannel cmail = new MailChannel();
            try {
                cmail.send(user, mailContent);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        result.put("username", username);
        return result();

    }
}
