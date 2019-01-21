/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pr.cassandra.triggers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import org.apache.cassandra.db.Mutation;
import org.apache.cassandra.db.partitions.Partition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author msivasub
 */
public class LoggingCassTriggerTest {
    
    public LoggingCassTriggerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() throws URISyntaxException, IOException {
        EmbeddedCassandraManager.getInstance().startCassandra();
    }
    
    @AfterEach
    public void tearDown() {
        EmbeddedCassandraManager.getInstance().stop();
    }

    /**
     * Test of augment method, of class LoggingCassTrigger.
     */
    @Test
    public void testAugment() {
        System.out.println("augment");
        Partition prtn = null;
        LoggingCassTrigger instance = new LoggingCassTrigger();
        Collection<Mutation> expResult = null;
        Collection<Mutation> result = instance.augment(prtn);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
