package com.springleaf.auth;

import com.springleaf.exception.AccessDeniedException;
import com.springleaf.object.entity.User;

import java.util.Map;

public interface TokenFactory {
    public static final String USER = "user";
    public static final String EXPIRE = "expire";
    public static final String BEGIN = "begin";
    public static final String DEVICE = "device";
    public static final String SA_LOGIN = "sa";
    public static final String INIT_PROCESS = "init";

    public String generate(Map<String, Object> payload, long expire);

    public Map<String, Object> validate(String token);
}
