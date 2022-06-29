package com.springleaf.context.user;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.DefaultValues;
import com.springleaf.common.RequestType;
import com.springleaf.common.ulti.CryptoUlti;
import io.ebean.Ebean;
import io.ebean.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import com.springleaf.object.entity.User;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequestMapping(type = RequestType.GET, path = "/user/logout")
public class LogoutContext extends UserContext{

    private String REQUEST_ID = "request_id";

    @Override
    @Transactional
    // TODO must expire token
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        String req_id = (String) map.get(REQUEST_ID);
        if ($.isEmpty(req_id)) {
            return result();
        }
        userRecord = Ebean.find(User.class).where().eq("id", userRecord.getId()).findOne();
        String token = userRecord.getLogin().getToken();
        if (token == null) {
            return result();
        }
        String[] components = token.split(DefaultValues.SEPARATOR_STR);
        String plain2 = new String(Objects.requireNonNull(CryptoUlti.decrypt($.decode(components[1]))));
        log.debug(plain2);
        log.debug(req_id);
        if (!plain2.equals(req_id)) {
            return result();
        }
        else {
            log.debug("Refresh token goign to be deleted");
            userRecord.getLogin().setToken(null);
            userRecord.save();
        }
        return result();
    }
}
