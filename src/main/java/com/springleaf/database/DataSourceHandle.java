package com.springleaf.database;

import com.springleaf.common.DefaultValues;
import com.springleaf.database.imcache.InternalCache;
import com.springleaf.database.redis.RedisCache;
import com.springleaf.database.redis.RedisDatBase;
import com.springleaf.database.sql.SQLDatabase;

public class DataSourceHandle {
    public static void init() {
        // MySQL
        new SQLDatabase().setup();
        // Binary
//        new MinoDatabase().setup();
        // Cache
        new RedisDatBase().setup();
    }

    public static DataCache getDataCache() {
        if (DefaultValues.USE_IM_CACHE) {
            return InternalCache.getInstance();
        }
        else return new RedisCache();
    }

}
