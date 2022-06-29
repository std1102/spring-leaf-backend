package com.springleaf.context.user;

import com.springleaf.auth.JWTSToken;
import com.springleaf.auth.TokenFactory;
import com.google.gson.Gson;
import com.springleaf.common.$;
import com.springleaf.common.DefaultValues;
import com.springleaf.common.ErrorCode;
import com.springleaf.context.Context;
import com.springleaf.exception.AccessDeniedException;
import com.springleaf.exception.TokenExpiredException;
import io.ebean.Ebean;
import lombok.extern.slf4j.Slf4j;
import com.springleaf.object.entity.Role;
import com.springleaf.object.entity.User;
import com.springleaf.object.entity.types.RoleType;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@Slf4j
public class UserContext extends Context {

    private final String AUTH = "auth";

    protected User userRecord;

    @Override
    protected void authorize() throws AccessDeniedException, TokenExpiredException {
        String token = (String) param.get(AUTH);
        if ($.isEmpty(token)) {
            throw new AccessDeniedException();
        }
        TokenFactory tokenFactory = new JWTSToken();
        Map<String, Object> credential = tokenFactory.validate(token);
        if (credential == null) {
            throw new AccessDeniedException();
        }
        String userJson = credential.get("user").toString();
        userRecord = $.parse(userJson, User.class);
        if (userRecord == null) {
            throw new AccessDeniedException();
        }
        if (!userRecord.getActive()) {
            throw new AccessDeniedException();
        }
        userRecord = Ebean.find(User.class).where().eq("id", userRecord.getId()).findOne();
        boolean checker = false;
        for (Role role : userRecord.getRoles()) {
            if (role.getType() == RoleType.USER) {
                checker = true;
            }
        }
        long t = (long) credential.get("begin");
        Long lastChangePss = userRecord.getLast_change_password();
        if (lastChangePss != null && lastChangePss > t) {
            throw new AccessDeniedException();
        }
        if (!checker) {
            throw new AccessDeniedException();
        }
    }


    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        return null;
    }
}
