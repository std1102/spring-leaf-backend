package com.springleaf.context.test;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.RequestType;
import com.springleaf.context.Context;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequestMapping(type = RequestType.GET, path = "/test")
public class Test extends Context {

    @Override
    protected Object _process(Map<String, Object> map) throws IOException {
        log.debug("hello");
        return null;
    }
}
