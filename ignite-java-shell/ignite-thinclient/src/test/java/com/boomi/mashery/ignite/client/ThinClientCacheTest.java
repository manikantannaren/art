/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.boomi.mashery.ignite.client;

import com.boomi.mashery.common.CacheClientException;
import com.boomi.mashery.common.IgniteContainerInitializer;
import java.util.Map;
import java.util.Set;
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
import java.util.logging.Level;

/**
 *
 * @author manikantans
 */
@Tags(
        @Tag("unittest")
)
@ExtendWith(IgniteContainerInitializer.class)
public class ThinClientCacheTest {

    private static Logger LOGGER = LoggerFactory.getLogger(ThinClientCacheTest.class);
    private static final String CACHE_NAME = "somecache";
    private static final Map<String, Object> ENTRIES = Map.of(
            "ONE", "1",
            "TWO", "22",
            "THREE", "333",
            "FOUR", "4444",
            "FIVE", "55555"
    );

    public ThinClientCacheTest() {
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
     * Test of getCacheName method, of class ThinClientCache.
     */
    @Test
    public void testGetCacheName(GenericContainer container) throws CacheClientException {
        LOGGER.info("Running test for getCacheName");
        LocalCache instance = getThinClientCache(container);
        String result = instance.getCacheName();
        assertEquals(CACHE_NAME, result);
    }

    /**
     * Test of get method, of class ThinClientCache.
     */
    @Test
    public void testGet(GenericContainer container) throws Exception {
        LOGGER.info("Running test for get");
        LocalCache<String, Object> instance = getThinClientCache(container);
        instance.putAll(ENTRIES);
        ENTRIES.entrySet().forEach(entry -> {
            String expResult = (String) entry.getValue();
            String key = entry.getKey();
            try {
                String actualResult = (String) instance.get(key);
                LOGGER.debug("Found Key: {}---- Value:{}",key,actualResult);
                assertEquals(expResult, actualResult);
            } catch (CacheClientException ex) {
                LOGGER.error("Error getting value for key {}", key, ex);
                fail("Failed to get value for key " + key);
            }
        });
    }

    /**
     * Test of put method, of class ThinClientCache.
     */
    @Test
    public void testPut(GenericContainer container) throws Exception {
        LOGGER.info("Running test for put");
        LocalCache<String, Object> instance = getThinClientCache(container);

        ENTRIES.entrySet().forEach(entry -> {
            String key = entry.getKey();
            Object val = entry.getValue();
            try {
                assertTrue(instance.put(key, val));
            } catch (CacheClientException ex) {
                LOGGER.error("Error putting value for key {}", key, ex);
                fail("Failed to get value for key " + key);
            }
        });
    }

    /**
     * Test of putAll method, of class ThinClientCache.
     */
    @Test
    public void testPutAll(GenericContainer container) throws Exception {
        LOGGER.info("Running test for putAll");
        LocalCache<String, Object> instance = getThinClientCache(container);
        assertTrue(instance.putAll(ENTRIES));
    }

    /**
     * Test of keys method, of class ThinClientCache.
     */
    @Test
    public void testKeys(GenericContainer container) throws Exception {
        LOGGER.info("Running test for keys");
        LocalCache<String, Object> instance = getThinClientCache(container);
        instance.putAll(ENTRIES);
        Set<String> expResult = ENTRIES.keySet();
        Set<String> result = instance.keys();
        LOGGER.debug("Found keys in cache {}",result);
        assertEquals(expResult, result);
    }

    /**
     * Test of exists method, of class ThinClientCache.
     */
    @Test
    public void testExists(GenericContainer container) throws Exception {
        LOGGER.info("Running test for exists");
        LocalCache<String, Object> instance = getThinClientCache(container);
        instance.putAll(ENTRIES);
        ENTRIES.keySet().forEach(key -> {
            try {
                assertTrue(instance.exists(key));
            } catch (CacheClientException ex) {
                LOGGER.error("Error checking if key {} exists", key, ex);
                fail("Failed to check if key " + key + " exists");
            }
        });
        assertFalse(instance.exists("nonexistentkey"));

    }

    /**
     * Test of emptyCache method, of class ThinClientCache.
     */
    @Test
    public void testEmptyCache(GenericContainer container) throws Exception {
        LOGGER.info("Running test for emptyCache");
        LocalCache<String, Object> instance = getThinClientCache(container);
        instance.putAll(ENTRIES);
        assertTrue(instance.emptyCache());
    }

    /**
     * Test of remove method, of class ThinClientCache.
     */
    @Test
    public void testRemove(GenericContainer container) throws Exception {
        LOGGER.info("Running test for remove");
        LocalCache<String, Object> instance = getThinClientCache(container);
        instance.putAll(ENTRIES);
        ENTRIES.keySet().forEach(key->{
            try {
                assertTrue(instance.remove(key));
            } catch (CacheClientException ex) {
                java.util.logging.Logger.getLogger(ThinClientCacheTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        assertFalse(instance.remove("nonexistentkey"));
    }

    /**
     * Test of removeAll method, of class ThinClientCache.
     */
    @Test
    public void testRemoveAll(GenericContainer container) throws Exception {
        LOGGER.info("Running test for removeAll");
        LocalCache<String, Object> instance = getThinClientCache(container);
        instance.putAll(ENTRIES);
        assertTrue(instance.removeAll(ENTRIES.keySet()));
    }

}
