/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.boomi.mashery.ignite.client;

import com.boomi.mashery.common.IgniteContainerInitializer;
import java.net.InetAddress;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;

/**
 *
 * @author manikantans
 */
@Tags(
    @Tag("unittest")
)
@ExtendWith(IgniteContainerInitializer.class)
public class IgniteClientDiscoveryConfigurationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(IgniteClientDiscoveryConfigurationTest.class);
    public IgniteClientDiscoveryConfigurationTest() {
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
     * Test of getIgniteConfiguration method, of class IgniteClientDiscoveryConfiguration.
     * @param container
     * @throws java.lang.Exception
     */
    @Test
    public void testGetIgniteConfiguration(GenericContainer container) throws Exception {
        LOGGER.info("RUNNING TEST getIgniteConfiguration");
        Integer mappedDiscoveryPort = container.getMappedPort(47500);
        String discoverAddress = InetAddress.getLoopbackAddress().getHostAddress()+":"+mappedDiscoveryPort;
        IgniteClientDiscoveryConfiguration instance = new IgniteClientDiscoveryConfiguration.DiscoveryConfigurationBuilder().withClientDiscoveryType(ClientDiscoveryType.TCP)
            .withIPAddresses(List.of(discoverAddress))
            .build();
        Assertions.assertNotNull(instance.getIgniteConfiguration());
    }
    
}
