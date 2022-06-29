package com.springleaf.context;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.auth.AuthType;
import com.springleaf.auth.JWTSToken;
import com.springleaf.auth.RefreshToken;
import com.springleaf.auth.TokenFactory;
import com.springleaf.common.$;
import com.springleaf.common.DefaultValues;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.common.ulti.CryptoUlti;
import io.ebean.Ebean;
import io.ebean.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import com.springleaf.object.entity.Login;
import com.springleaf.object.entity.User;
import org.checkerframework.checker.units.qual.A;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.text.ParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping(type = RequestType.POST, path = "/login")
public class LoginContext extends Context {

    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String REMEMBER_ME = "remember_me";
    private final String REFRESH_TOKEN = "refresh_token";
    private final String REQUEST_ID = "request_id";
    private final String AUTH_TYPE = "auth_type";

    @Override
    @Transactional
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        String auth_type = (String) map.get(AUTH_TYPE);
        if (auth_type.equals(AuthType.PAP.toString())) {
            String username = (String) map.get(USERNAME);
            String password = (String) map.get(PASSWORD);
            boolean isRememberMe = false;
            if (map.containsKey(REMEMBER_ME)) {
                isRememberMe = (boolean) map.get(REMEMBER_ME);
            }
            if ($.isMultipleEmpty(username, password)) {
                return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
            }
            Login login = Ebean.find(Login.class).select("*").where().eq("username", username).findOne();
            if (login == null) {
                return error(ErrorCode.ACCESS_DENIED);
            }
            String hPass = login.getPassword();
            if (!BCrypt.checkpw(password, hPass)) {
                return error(ErrorCode.ACCESS_DENIED);
            }
            User user = login.getUser();
            if (!user.getActive()) {
                return error(ErrorCode.ACCESS_DENIED);
            }
            TokenFactory tokenFactory = new JWTSToken();
            Map<String, Object> jwtPayload = new HashMap<>();
            jwtPayload.put("user", user);
            String token = tokenFactory.generate(jwtPayload, DefaultValues.SESSION_EXPIRE);
            result.put("access_token", token);

            // refresh token will be combination of username when login and request id
            // request id and username will be encrypted separately by using AES 256
            // all will be encode to base 64
            if (!$.isEmpty(isRememberMe)) {
                if (isRememberMe) {
                    TokenFactory refreshToken = new RefreshToken();
                    jwtPayload.clear();
                    jwtPayload.put("user", login);
                    String rfToken = refreshToken.generate(jwtPayload, DefaultValues.SESSION_EXPIRE);
                    String reqId = $.encode(CryptoUlti.encrypt(request_id.getBytes()));
                    login.setToken(rfToken + DefaultValues.SEPARATOR_STR + reqId);
                    login.save();
                    result.put("refresh_token", rfToken);
                }
            }
            else {
                login.setToken(null);
                login.save();
            }
            return result();
        } else if (auth_type.equals(AuthType.REFRESH_TOKEN.toString())) {
            String refresh_token = (String) map.get(REFRESH_TOKEN);
            String req_id = (String) map.get(REQUEST_ID);
            if ($.isMultipleEmpty(refresh_token, req_id)) {
                return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
            }
            TokenFactory refreshTokenFactory = new RefreshToken();
            Map<String, Object> rs = refreshTokenFactory.validate(refresh_token + DefaultValues.SEPARATOR_STR + req_id);
            if (rs == null) {
                return error(ErrorCode.ACCESS_DENIED);
            }
            User user = (User) rs.get("user");
            TokenFactory tokenFactory = new JWTSToken();
            Map<String, Object> jwtPayload = new HashMap<>();
            jwtPayload.put("user", user);
            String token = tokenFactory.generate(jwtPayload, DefaultValues.SESSION_EXPIRE);
            result.put("access_token", token);
            return result();
        }
        return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
    }
}
