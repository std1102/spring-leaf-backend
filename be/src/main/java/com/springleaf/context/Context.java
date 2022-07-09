package com.springleaf.context;

import com.springleaf.common.$;
import com.springleaf.common.ErrorCode;
import com.springleaf.exception.AccessDeniedException;
import com.springleaf.exception.TokenExpiredException;
import io.ebean.DuplicateKeyException;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class Context {
    protected Map<String, Object> param = new HashMap<>();
    protected final Map<String, Object> result = new HashMap<>();
    protected long begin = System.currentTimeMillis();
    protected String request_id;

    protected static final String REQUESTID = "request_id";
    protected static final String ERROR = "error";
    protected static final String ERROR_DESC = "error_desc";
    protected static final String ACTION = "action";


    protected Context() {
        request_id = $.genUUID();
    }

    protected abstract Object _process(Map<String, Object> map) throws IOException, ParseException, MessagingException;

    protected void authorize() throws AccessDeniedException, TokenExpiredException {
    }

    public Object process(Map<String, Object> map) {
        result.put(REQUESTID, request_id);
        if (map != null)
            param.putAll(map);
        try {
            try {
                authorize();
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof AccessDeniedException) {
                    return error(ErrorCode.ACCESS_DENIED);
                }
                if (e instanceof TokenExpiredException) {
                    return error(ErrorCode.TOKEN_EXPIRED);
                }
                return error(ErrorCode.INTERNAL_ERROR);
            }
            Object obj = _process(map);
            log();
            return obj;
        } catch (Exception e) {
            // TODO handle error
            log.error(e.getMessage());
            log();
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                return error(ErrorCode.USERNAME_DUPLICATED);
            }
            return error(ErrorCode.INTERNAL_ERROR);
        }
    }

    protected void log () {
        long current = System.currentTimeMillis();
        log.debug("Request take " + (current - begin) + " ms to handle");
    }
    protected Object result() {
        if (!result.containsKey(ERROR)) {
            result.put(REQUESTID, request_id);
            result.put(ERROR, ErrorCode.OK.code);
            result.put(ERROR_DESC, ErrorCode.OK.desc);
        }
        result.put(REQUESTID, request_id);
        return result;
    }

    protected Object error(ErrorCode code) {
        try {
            if (code != ErrorCode.OK)
                rollback();
        } catch (Exception e) {
            log.warn("Unable to rollback request " + request_id);
            e.printStackTrace();
        }
        result.clear();
        result.put(REQUESTID, request_id);
        result.put(ERROR, code.code);
        result.put(ERROR_DESC, code.desc);
        return result;
    }

    protected void rollback() {
    }
}
