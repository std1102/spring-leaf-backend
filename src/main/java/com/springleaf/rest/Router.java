package com.springleaf.rest;

import javax.servlet.MultipartConfigElement;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import com.springleaf.annotation.RequestMapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.springleaf.common.$;
import com.springleaf.common.Config;
import com.springleaf.common.RequestType;
import com.springleaf.context.Context;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.framework.qual.SubtypeOf;
import org.reflections.Reflections;
import spark.Request;
import spark.Spark;
import static spark.Spark.*;

@Slf4j
public class Router {
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static void init () throws InstantiationException, IllegalAccessException {
        System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
        System.setProperty("org.eclipse.jetty.LEVEL", "OFF");
        port(Integer.parseInt(Config.getProperty(Config.SYSTEM_PORT)));
        Spark.init();
        if (Boolean.getBoolean(Config.getProperty(Config.ENABLE_CORS))) {
            options("/*",             //enable cors
                    (request, response) -> {
                        response.header("Access-Control-Allow-Origin", "*");
                        String accessControlRequestHeaders = request
                                .headers("Access-Control-Request-Headers");
                        if (accessControlRequestHeaders != null) {
                            response.header("Access-Control-Allow-Headers",
                                    accessControlRequestHeaders);
                        }
                        String accessControlRequestMethod = request
                                .headers("Access-Control-Request-Method");
                        if (accessControlRequestMethod != null) {
                            response.header("Access-Control-Allow-Methods",
                                    accessControlRequestMethod);
                        }

                        return "OK";
                    });
        }
        initRoute();
    }
    public static void initRoute() throws InstantiationException, IllegalAccessException {
        AtomicReference<Map<String, Object>> data = new AtomicReference<>();
        path("/spring-leaf", () -> {
            before("/*", (request, response) -> {
                // TODO not set response type to json when upload and download
                prepareParam(data, request);
                response.type("application/json");
            });
            Reflections reflections = new Reflections("com.springleaf.context");
            Set<Class<? extends Context>> classes = reflections.getSubTypesOf(Context.class);
            for (Class<? extends Context> clazz : classes) {
                if (clazz.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestInfo = clazz.getAnnotation(RequestMapping.class);
                    RequestType type = requestInfo.type();
                    switch (type) {
                        case POST:
                            post(requestInfo.path(), ((request, response) -> {
                                return GSON.toJson(clazz.newInstance().process(data.get()));
                            }));
                            break;
                        case GET:
                            get(requestInfo.path(), ((request, response) -> {
                                return GSON.toJson(clazz.newInstance().process(data.get()));
                            }));
                            break;
                        case DELETE:
                            delete(requestInfo.path(), ((request, response) -> {
                                return GSON.toJson(clazz.newInstance().process(data.get()));
                            }));
                            break;
                        case PUT:
                            put(requestInfo.path(), ((request, response) -> {
                                return GSON.toJson(clazz.newInstance().process(data.get()));
                            }));
                            break;
                        default:
                            log.debug("Cannot find avaiable request method");
                    }
                }
            }
        });
    }

    private static void prepareParam(AtomicReference<Map<String, Object>> data, Request request) {
        String token = request.headers("auth");
        Map<String, Object> map;
        if (request.requestMethod().equals("GET")) {
            map = new HashMap<>();
            for (String str : request.queryParams()) {
                log.debug(request.queryParams(str));
                map.put(str, request.queryParams(str));
            }
        }
        else {
            map = $.parseJSONData(request.body());
            if (map == null) {
                map = new HashMap<>();
            }
        }
        if (!$.isEmpty(token)) {
            map.put("auth", token);
        }
        data.set(map);
    }


}
