package com.springleaf.context.user;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.object.entity.Login;
import com.springleaf.object.entity.User;
import io.ebean.Ebean;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@Slf4j
@RequestMapping(type = RequestType.GET, path = "/user/info")
public class GetUserInfo extends UserContext{
    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        User user = Ebean.find(User.class).where().eq("id", userRecord.getId()).findOne();
        Login login = Ebean.find(Login.class).where().eq("user_id", userRecord.getId()).findOne();
        if (user == null) {
            return error(ErrorCode.INTERNAL_ERROR);
        }
        login.setUser(null);
        user.setLogin(login);
        user.getLogin().setPassword(null);
        user.getLogin().setToken(null);
        user.setLast_change_password(null);
        result.put("user", user);
        return result();
    }
}
