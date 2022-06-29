package com.springleaf.database.redis;

import com.springleaf.database.DataCache;

import java.util.Map;

public class RedisCache implements DataCache {

    @Override
    public boolean put(String key, String value, long timeAlive) {
        return false;
    }

    @Override
    public boolean putCategory(String category, String key, String value, long timeAlive) {
        return false;
    }

    @Override
    public boolean putMap(String category, Map<String, String> map, long timeAlive) {
        return false;
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public Map<String, String> getMap(String category) {
        return null;
    }

    @Override
    public Map<String, String> getAllCategory(String category) {
        return null;
    }

    @Override
    public boolean delete(String keyOrCategory) {
        return false;
    }

    @Override
    public boolean delete(String category, String key) {
        return false;
    }

    @Override
    public long size() {
        return 0;
    }
}
