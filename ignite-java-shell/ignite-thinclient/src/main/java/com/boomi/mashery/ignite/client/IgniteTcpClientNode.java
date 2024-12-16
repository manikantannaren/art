/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.client;

import com.boomi.mashery.common.CacheClientException;
import com.boomi.mashery.common.MsgCode;
import com.boomi.mashery.common.Status;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientCluster;
import org.apache.ignite.client.ClientException;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.ClientConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author manikantans
 */
final class IgniteTcpClientNode implements ClientNode {

    public static final Logger LOGGER = LoggerFactory.getLogger(IgniteTcpClientNode.class);

    private final IgniteClientDiscoveryConfiguration configuration;
//    private Ignite ignite;
    private final AtomicBoolean connecting = new AtomicBoolean(Boolean.FALSE);
    private final Map<String, LocalCache<String, Object>> cachedCaches = new WeakHashMap<>();
    private IgniteClient igniteClient;

    public IgniteTcpClientNode(IgniteClientDiscoveryConfiguration configuration) throws CacheClientException {
        this.configuration = configuration;
    }

    @Override
    public void start() throws CacheClientException {
        if (configuration == null) {
            throw new CacheClientException(MsgCode.E0003_INVALID_CONFIG, "Have you initialized the config?");
        }
        synchronized (connecting) {
            if (!connecting.get() && igniteClient == null) {
                connecting.set(Boolean.TRUE);
                LOGGER.info("Starting client node");
                try {
//                    IgniteConfiguration config = configuration.getIgniteConfiguration();
//                    ignite = Ignition.start(config);
                    ClientConfiguration config = configuration.getThinClientConfiguration();

                    igniteClient = Ignition.startClient(config);
                    LOGGER.info("Client node started");
                    connecting.set(Boolean.FALSE);
                } catch (IgniteException | ClientException e) {
                    throw new CacheClientException(MsgCode.E0002_CONN_ERROR, "Could not discover or connect to cluster", e);
                }
            }
        }
    }

    @Override
    public Status status() {
        if (igniteClient == null) {
            return Status.NOT_CONNECTED;
        }
        if (connecting.get()) {
            return Status.CONNECTING;
        }
        try {
            LOGGER.debug("Defined caches {}", cacheNames());
            return Status.RUNNING;
        } catch (ClientException e) {
            LOGGER.error("Not connected to ignite cluster", e);
            return Status.NOT_CONNECTED;
        }
    }

    @Override
    public LocalCache<String, Object> getCache(String cacheName) throws CacheClientException {
        if (status() != Status.RUNNING) {
            throw new CacheClientException(MsgCode.E0005_NOT_INITIALIZED, "");
        }
        if (!cachedCaches.containsKey(cacheName)) {
            try {
                ClientCache<String, Object> cache = igniteClient.getOrCreateCache(cacheName);
                cachedCaches.put(cacheName, new ThinClientCache(cache));
            } catch (ClientException e) {
                throw new CacheClientException(MsgCode.E0007_UNSUCCESSFUL_OPERATION, "Could not create or fetch cache by name " + cacheName);
            }
        }
        return cachedCaches.get(cacheName);
    }

    @Override
    public Set<String> cacheNames() {
        Collection<String> names = igniteClient.cacheNames();
        return new TreeSet<>(names);
    }

    @Override
    public IgniteClientDiscoveryConfiguration connectionInfo() {
        return configuration;
    }

    @Override
    public void destroyCache(String cacheName) throws CacheClientException {
        try {
            igniteClient.destroyCache(cacheName);
        } catch (ClientException e) {
            throw new CacheClientException(MsgCode.E0007_UNSUCCESSFUL_OPERATION, "Could not destroy cache : " + cacheName);
        }
    }

    @Override
    public ClusterInfo clusterInfo() {
        ClientCluster cluster = igniteClient.cluster();
        Collection<ClusterNode> nodes = cluster.nodes();
        
        ClusterInfo info = TcpClusterInfo.builder()
                .withConfiguration(connectionInfo())
                .withCaches(cacheNames())
                .withNodes(mapNodes(nodes))
                .build();
        return info;
    }

    @Override
    public void close() {
        igniteClient.close();
        cachedCaches.clear();
    }

    private Collection<TCPClusterNode> mapNodes(Collection<ClusterNode> nodes) {
        return nodes.stream().map(o->new TCPClusterNode(o)).collect(Collectors.toSet());
    }

}
