/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.client;

import com.boomi.mashery.common.CacheClientException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;

/**
 *
 * @author manikantans
 */
public class TestUtils {
    public static final String CACHE_NAME = "somecache";
    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);

    public static ClientNode createClientNode(GenericContainer container) throws CacheClientException {
        //        String clientAddress = InetAddress.getLocalHost().getHostAddress()+":"+container.getMappedPort(47500);
        List<String> ipaddresses = new ArrayList<>();

        String clientAddress = "localhost:" + container.getMappedPort(10800);
        ipaddresses.add(clientAddress);
        LOGGER.debug("TCP discovery addresses {}", ipaddresses);
        IgniteClientDiscoveryConfiguration config = new IgniteClientDiscoveryConfiguration.DiscoveryConfigurationBuilder()
                .withClientDiscoveryType(ClientDiscoveryType.TCP)
                .withIPAddresses(ipaddresses).build();
        ClientNode instance = new IgniteTcpClientNode(config);
        instance.start();
        return instance;
    }

    public static LocalCache<String,Object> getThinClientCache(GenericContainer container) throws CacheClientException{
        ClientNode node = createClientNode(container);
        return node.getCache(CACHE_NAME);
    }
}
