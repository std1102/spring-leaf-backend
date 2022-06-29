package com.springleaf.database.redis;

import com.springleaf.common.DefaultValues;
import com.springleaf.database.Database;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public class RedisDatBase implements Database {

    private static Jedis redis;
    private static JedisPool jedisPool;

    @Override
    public void setup() {
        jedisPool = new JedisPool(buildPoolConfig(), DefaultValues.REDIS_HOST, DefaultValues.REDIS_PORT);
    }

    public static Jedis getRedis() {
        if (redis == null) {
            try {
                redis = jedisPool.getResource();
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        else return redis;
        return null;
    }

    private static JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }
}
