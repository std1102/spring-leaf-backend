package com.springleaf.context.user;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.DefaultValues;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import io.ebean.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@Slf4j
@RequestMapping(path = "/user/change-password", type = RequestType.POST)
public class ChangePassword extends UserContext{

    private String OLD_PASSWORD = "old_password";
    private String NEW_PASSWORD = "new_password";

    @Override
    @Transactional
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        String old_password = (String) map.get(OLD_PASSWORD);
        String new_password = (String) map.get(NEW_PASSWORD);
        if ($.isMultipleEmpty(old_password, new_password)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        if (old_password.equals(new_password)) {
            return error(ErrorCode.SAME_PASSWORD);
        }
        String currentPass = userRecord.getLogin().getPassword();
        if (!BCrypt.checkpw(old_password, currentPass)) {
            return error(ErrorCode.OLD_PASSWORD_NOT_MATCH);
        }
        new_password = BCrypt.hashpw(new_password, BCrypt.gensalt(DefaultValues.SALT));
        userRecord.getLogin().setPassword(new_password);
        long t = System.currentTimeMillis();
        log.debug("Password changed at " + System.currentTimeMillis());
        userRecord.setLast_change_password(t);
        userRecord.save();
        return result();
    }
}
