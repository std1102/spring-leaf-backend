package com.springleaf.context.user;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.RequestType;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@RequestMapping(type = RequestType.GET, path = "/user/info")
public class GetUserInfo extends UserContext{
    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        result.put("user", userRecord);
        return result();
    }
}
