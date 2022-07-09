package com.springleaf.database.sql;

import com.springleaf.common.Config;
import com.springleaf.database.Database;
import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class SQLDatabase implements Database {

    private static EbeanServer server;

    @Override
    public void setup() {
        synchronized (SQLDatabase.class) {
            try {
                log.debug("Setting up MySQL database");
                ServerConfig dbcfg = new ServerConfig();
                Properties prop = new Properties();
                prop.put(Config.DATA_SOURCE_DRIVER, Config.getProperty(Config.DATA_SOURCE_DRIVER));
                prop.put(Config.DATA_SOURCE_URL, Config.getProperty(Config.DATA_SOURCE_URL));
                prop.put(Config.DATA_SOURCE_MAXCONNECTION, Config.getProperty(Config.DATA_SOURCE_MAXCONNECTION));
                prop.put(Config.DATA_SOURCE_USERNAME, Config.getProperty(Config.DATA_SOURCE_USERNAME));
                prop.put(Config.DATA_SOURCE_PASSWORD, Config.getProperty(Config.DATA_SOURCE_PASSWORD));
                prop.put("ebean.search.packages", "com.springleaf.object.entity");
                dbcfg.loadFromProperties(prop);
//                dbcfg.setDdlGenerate(true);
//                dbcfg.setDdlRun(true);
                server = EbeanServerFactory.create(dbcfg);
            }
            catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
