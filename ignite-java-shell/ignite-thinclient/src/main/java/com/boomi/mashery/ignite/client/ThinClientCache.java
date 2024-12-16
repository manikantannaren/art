/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.client;

import com.boomi.mashery.common.CacheClientException;
import com.boomi.mashery.common.MsgCode;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.cache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientException;

/**
 *
 * @author manikantans
 */
class ThinClientCache implements LocalCache<String, Object> {

    private final ClientCache<String, Object> cache;

    ThinClientCache(ClientCache<String, Object> cache) {
        this.cache = cache;
    }

    @Override
    public String getCacheName() {
        return cache.getName();
    }

    @Override
    public Object get(String key) throws CacheClientException {
        if (StringUtils.isEmpty(key)) {
            throw new CacheClientException(MsgCode.E0006_ILLEGAL_ARGUMENT, "Key cannot be empty");
        }
        try {
            return cache.withKeepBinary().get(key);
        } catch (ClientException e) {
            throw new CacheClientException(MsgCode.E0007_UNSUCCESSFUL_OPERATION, "Could not get value for key " + key, e);
        }
    }

    @Override
    public Boolean put(String key, Object value) throws CacheClientException {
        try {
            cache.put(key, value);
        } catch (ClientException e) {
            throw new CacheClientException(MsgCode.E0007_UNSUCCESSFUL_OPERATION, "Could not insert value for key " + key, e);
        }
        return true;
    }

    @Override
    public Boolean putAll(Map<String, Object> entries) throws CacheClientException {
        try {
            cache.putAll(entries);
        } catch (ClientException e) {
            throw new CacheClientException(MsgCode.E0007_UNSUCCESSFUL_OPERATION, "Could not insert map of values", e);
        }
        return true;
    }

    @Override
    public Set<String> keys() throws CacheClientException {
        ScanQuery<String, Object> query = new ScanQuery<>();
        
        QueryCursor<Cache.Entry<String, Object>> cursor = cache.withKeepBinary().query(query);
        List<String> results = cursor.getAll()
                .stream()
                .map(Cache.Entry::getKey)
                .collect(Collectors.toList());
        return new TreeSet(results);
    }

    @Override
    public Boolean exists(String key) throws CacheClientException {
        try {
            return cache.containsKey(key);
        } catch (ClientException e) {
            throw new CacheClientException(MsgCode.E0007_UNSUCCESSFUL_OPERATION, "Could not check if key " + key + " exists", e);
        }
    }

    @Override
    public Boolean emptyCache() throws CacheClientException {
        try {
            cache.removeAll();
        } catch (ClientException e) {
            throw new CacheClientException(MsgCode.E0007_UNSUCCESSFUL_OPERATION, "Could not clear cache", e);
        }
        return true;
    }

    @Override
    public Boolean remove(String key) throws CacheClientException {
        try {
            return cache.remove(key);
        } catch (ClientException e) {
            throw new CacheClientException(MsgCode.E0007_UNSUCCESSFUL_OPERATION, "Could not remove key from cache", e);
        }
    }

    @Override
    public Boolean removeAll(Set<String> keys) throws CacheClientException {
        try {
            cache.removeAll(keys);
        } catch (ClientException e) {
            throw new CacheClientException(MsgCode.E0007_UNSUCCESSFUL_OPERATION, "Could not remove provided keys from cache", e);
        }
        return true;
    }

}
