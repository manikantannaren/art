/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.boomi.mashery.ignite.client;

import com.boomi.mashery.common.CacheClientException;
import static com.boomi.mashery.common.MsgCode.E0001_GENERIC_ERROR;
import static com.boomi.mashery.common.MsgCode.E0003_INVALID_CONFIG;
import com.boomi.mashery.common.Status;
import java.util.Set;

/**
 *
 * @author manikantans
 */
public interface ClientNode {

    public static ClientNode instance(IgniteClientDiscoveryConfiguration config) throws CacheClientException {
        switch(config.type()){
            case K8S -> throw new CacheClientException(E0001_GENERIC_ERROR, "Not yet implemented");
            case TCP -> {
                return new IgniteTcpClientNode(config);
            }
            default -> throw new CacheClientException(E0003_INVALID_CONFIG, "Unknown discovery type");
        }
    }

    void start() throws CacheClientException;

    Status status();

    LocalCache<String, Object> getCache(String cacheName) throws CacheClientException;
    
    Set<String> cacheNames();
    
    IgniteClientDiscoveryConfiguration connectionInfo();

    public ClusterInfo clusterInfo();

    void destroyCache(String cacheName) throws CacheClientException;

    public void close();
    
}
