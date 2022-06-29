package com.springleaf.common.datastructure;

import java.util.Map;

public interface SelfExpiringMap<K, V> extends Map<K, V> {
    public boolean renewKey(K key);

    public V put(K key, V value, long lifeTimeMillis);
}
