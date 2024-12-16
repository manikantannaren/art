/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.boomi.mashery.ignite.client;

import com.boomi.mashery.common.CacheClientException;
import com.boomi.mashery.common.IgniteContainerInitializer;
import com.boomi.mashery.common.Status;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import static com.boomi.mashery.ignite.client.TestUtils.*;

/**
 *
 * @author manikantans
 */
@Tags(
    @Tag("unittest")
)
@ExtendWith(IgniteContainerInitializer.class)
public class IgniteTcpClientNodeTest {
    private static Logger LOGGER = LoggerFactory.getLogger(IgniteTcpClientNodeTest.class);
            
    public IgniteTcpClientNodeTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of start method, of class IgniteClientNode.
     */
    
    @Test
    public void testStart(GenericContainer container) throws Exception {
        LOGGER.info("Running test for start");
        
        ClientNode instance = createClientNode(container);
//        instance.start();
        Status expResult = Status.RUNNING;
        Status result = instance.status();
        assertEquals(expResult, result);
    }

    /**
     * Test of status method, of class IgniteClientNode.
     */
    @Test
    public void testStatus_Nogood() throws CacheClientException {
        LOGGER.info("Running test for status with invalid config");
        IgniteTcpClientNode instance = new IgniteTcpClientNode(null);
        Status expResult = Status.NOT_CONNECTED;
        Status result = instance.status();
        assertEquals(expResult, result);
    }

    @Test
    public void testStatus() throws CacheClientException, UnknownHostException {
        LOGGER.info("Running test for status");
        IgniteTcpClientNode instance = new IgniteTcpClientNode(null);
        Status expResult = Status.NOT_CONNECTED;
        Status result = instance.status();
        assertEquals(expResult, result);
        List<String> ipaddresses = new ArrayList<>();
        String clientAddress = InetAddress.getLocalHost().getHostAddress();
        ipaddresses.add(clientAddress);
        IgniteClientDiscoveryConfiguration config = new IgniteClientDiscoveryConfiguration.DiscoveryConfigurationBuilder()
                .withClientDiscoveryType(ClientDiscoveryType.TCP)
                .withIPAddresses(ipaddresses).build();
        instance = new IgniteTcpClientNode(config);
        expResult = Status.NOT_CONNECTED;
        result = instance.status();
        assertEquals(expResult,result);
    }
    /**
     * Test of getCache method, of class IgniteClientNode.
     */
    @Test
    public void testGetCache(GenericContainer container) throws Exception {
        LOGGER.info("Running test for getCache");
        ClientNode instance = createClientNode(container);
        LocalCache<String, Object> result = instance.getCache(CACHE_NAME);
        assertEquals(CACHE_NAME,result.getCacheName());
    }
        
}
