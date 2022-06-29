package com.springleaf.common;

import com.springleaf.common.ulti.TextFileUltis;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Data
public class Config {
    public static final String SYSTEM_PORT = "system.port";
    public static final String SYSTEM_LOG_FILE = "system.log.file";
    public static final String DATA_SOURCE_USERNAME = "datasource.db.username";
    public static final String DATA_SOURCE_PASSWORD = "datasource.db.password";
    public static final String DATA_SOURCE_URL = "datasource.db.databaseUrl";
    public static final String DATA_SOURCE_DRIVER = "datasource.db.databaseDriver";
    public static final String DATA_SOURCE_MAXCONNECTION = "datasource.db.maxConnections";
    public static final String ENABLE_CORS = "rest.cors";
    public static final String MINIO_USERNAME = "minio.username";
    public static final String MINIO_PASSWORD = "minio.password";
    public static final String MINIO_HOST =  "minio.host";
    public static final String MAIL_ACCOUNT = "mail.account";
    public static final String MAIL_PASSWORD = "mail.password";

    private static Properties properties;

    public static void init() {
        properties = new Properties();
        try {
            Class<Config> thisClass = Config.class;
            Field[] fields = thisClass.getFields();
            Map<String, String> map = TextFileUltis.findString(DefaultValues.CONFIG_FILE);
            for (Field field : fields) {
                if (map.containsKey(field.get(thisClass).toString())) {
                    properties.setProperty(field.get(thisClass).toString(), map.get(field.get(thisClass)));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty (String key) {
        if ($.isEmpty(key)) {
            log.debug("Invalid parameter ti get property");
            return null;
        }
        return properties.getProperty(key) ==  null ? null : properties.getProperty(key);
    }

    public static boolean isWindowsOS() {
        String os = "";
        os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            return true;
        }
        else return false;
    }
}
