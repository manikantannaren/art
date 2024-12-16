/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.boomi.mashery.ignite.client;

import com.boomi.mashery.common.CacheClientException;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author manikantans
 * @param <K>
 * @param <V>
 */
public interface LocalCache<K, V> {

    public String getCacheName();

    public V get(K key) throws CacheClientException;

    public Boolean put(K key, V value) throws CacheClientException;

    public Boolean putAll(Map<String, Object> entries) throws CacheClientException;

    public Set<K> keys() throws CacheClientException;

    public Boolean exists(K key) throws CacheClientException;
    
    public Boolean emptyCache() throws CacheClientException;
    
    public Boolean remove(K key) throws CacheClientException;
    
    public Boolean removeAll(Set<K> keys) throws CacheClientException;
    
}
