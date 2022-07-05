package com.springleaf.database.imcache;

import com.springleaf.common.datastructure.SelfExpiringHashMap;
import com.springleaf.common.datastructure.SelfExpiringMap;
import com.springleaf.database.DataCache;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class InternalCache implements DataCache {

    private static final InternalCache singleton = new InternalCache();

    public static InternalCache getInstance() {
        return singleton;
    }

    private InternalCache() {
        db.set(new SelfExpiringHashMap());
    }

    private AtomicReference<SelfExpiringMap<String, Object>> db = new AtomicReference<>();

    @Override
    public boolean put(String key, String value, long timeAlive) {
        db.get().put(key, value, timeAlive);
        return true;
    }

    @Override
    public boolean putCategory(String category, String key, String value, long timeAlive) {
        Object mapObj = db.get().get(category);
        if (mapObj == null) {
            SelfExpiringMap<String, String> categoryMap = new SelfExpiringHashMap<>();
            categoryMap.put(key, value, timeAlive);
            db.get().put(category, categoryMap);
            return true;
        } else if (mapObj instanceof SelfExpiringMap) {
            SelfExpiringMap<String, String> categoryMap = (SelfExpiringMap<String, String>) mapObj;
            categoryMap.put(key, value, timeAlive);
            return true;
        } else {
            log.warn("Object class is " + mapObj.getClass());
            return false;
        }
    }

    @Override
    public boolean putMap(String category, Map<String, String> map, long timeAlive) {
        Object obj = db.get().get(category);
        if (obj == null) {
            db.get().put(category, map, timeAlive);
            return true;
        } else if (obj instanceof Map) {
            map.putAll((Map<? extends String, ? extends String>) obj);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String get(String key) {
        Object obj = db.get().get(key);
        if (obj == null) {
            return null;
        } else if (obj instanceof Map) {
            return null;
        } else {
            return obj.toString();
        }
    }

    @Override
    public Map<String, String> getMap(String category) {
        Object obj = db.get().get(category);
        if (obj == null) {
            return new HashMap<>();
        } else if (obj instanceof Map) {
            return (Map<String, String>) obj;
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, String> getAllCategory(String category) {
        return getMap(category);
    }



    @Override
    public boolean delete(String keyOrCategory) {
        db.get().remove(keyOrCategory);
        return true;
    }

    @Override
    public boolean delete(String category, String key) {
        Object obj = db.get().get(category);
        if (obj == null) {
            return true;
        } else if (obj instanceof Map) {
            Map<String, String> map = (Map<String, String>) obj;
            map.remove(key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public long size() {
        return db.get().size();
    }
}