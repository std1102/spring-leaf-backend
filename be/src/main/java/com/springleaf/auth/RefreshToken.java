package com.springleaf.auth;

import com.springleaf.common.$;
import com.springleaf.common.DefaultValues;
import com.springleaf.common.ulti.CryptoUlti;
import com.springleaf.exception.AccessDeniedException;
import io.ebean.Ebean;
import com.springleaf.object.entity.Login;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RefreshToken implements TokenFactory{

    @Override
    public String generate(Map<String, Object> payload, long expire) {
        Login user = (Login) payload.get("user");
        String username = user.getUsername();
        String encrypt1 = $.encode(CryptoUlti.encrypt(username.getBytes()));
        return encrypt1;
    }

    @Override
    public Map<String, Object> validate(String token) {
        String[] components = token.split(DefaultValues.SEPARATOR_STR);
        if (components.length != 2) {
            return null;
        }
        String encrypt1 = components[0];
        String requestId = components[1];
        String username = new String(Objects.requireNonNull(CryptoUlti.decrypt($.decode(encrypt1))));
        String combination = username + requestId;
        Login login = Ebean.find(Login.class)
                .select("*")
                .where()
                .eq("username", username)
                .findOne();
        if (login == null) {
            return null;
        }
        String[] _tokens = login.getToken().split(DefaultValues.SEPARATOR_STR);
        String plain1 = new String(Objects.requireNonNull(CryptoUlti.decrypt($.decode(_tokens[0]))));
        String plain2 = new String(Objects.requireNonNull(CryptoUlti.decrypt($.decode(_tokens[1]))));
        if (!(plain1.equals(username) && plain2.equals(requestId))) {
            return null;
        }
        Map<String, Object> rs = new HashMap<>();
        rs.put("user", login.getUser());
        return rs;
    }
}
