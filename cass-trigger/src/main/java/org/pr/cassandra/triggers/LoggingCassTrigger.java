/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pr.cassandra.triggers;

import java.util.Collection;
import org.apache.cassandra.db.Mutation;
import org.apache.cassandra.db.partitions.Partition;
import org.apache.cassandra.triggers.ITrigger;

/**
 *
 * @author msivasub
 */
public class LoggingCassTrigger implements ITrigger{

    @Override
    public Collection<Mutation> augment(Partition prtn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
