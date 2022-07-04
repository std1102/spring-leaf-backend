package com.springleaf.context.user;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.context.Context;
import com.springleaf.database.DataCache;
import com.springleaf.database.DataSourceHandle;
import com.springleaf.object.entity.User;
import com.springleaf.object.entity.types.UserStatus;
import io.ebean.Ebean;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@RequestMapping(path = "/verifymail", type = RequestType.GET)
public class MailVerify extends Context {
    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException, MessagingException {
        String reqid = (String) map.get("id");
        if ($.isEmpty(reqid)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        DataCache cache = DataSourceHandle.getDataCache();
        String userID = cache.get(reqid);
        if ($.isEmpty(userID)) {
            return error(ErrorCode.LINK_EXPIRE);
        }
        User user = Ebean.find(User.class).where().eq("id", userID).findOne();
        user.setStatus(UserStatus.INITED);
        user.save();
        return result();
    }
}
