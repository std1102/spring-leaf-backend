package com.springleaf.context.user;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.RequestType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@RequestMapping(type = RequestType.GET, path = "/test")
@Slf4j
public class UserTest extends UserContext {
    @Override
    protected Object _process(Map<String, Object> map) throws IOException {
        return "HELLO WORLD";
    }
}
