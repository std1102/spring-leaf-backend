package com.springleaf.database;

import java.util.Map;

public interface DataCache {

    public boolean put(String key, String value, long timeAlive);

    public boolean putCategory(String category, String key, String value, long timeAlive);

    public boolean putMap(String category, Map<String, String> map, long timeAlive);

    public String get(String key);

    public Map<String, String> getMap(String category);

    public Map<String, String> getAllCategory(String category);

    public boolean delete(String keyOrCategory);

    public boolean delete(String category, String key);

    public long size();

    default String getAsCategory(String category, String key) {
        Map<String, String> mapCategory = getAllCategory(category);
        return mapCategory.get(key);
    }

}
