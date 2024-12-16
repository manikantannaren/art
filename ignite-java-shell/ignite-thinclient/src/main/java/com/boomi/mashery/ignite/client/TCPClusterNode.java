/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.client;

import java.util.Collection;
import java.util.UUID;
import org.apache.ignite.cluster.ClusterNode;

/**
 *
 * @author manikantans
 */
class TCPClusterNode implements IgniteNode{
    private final ClusterNode igniteClusterNode;
    TCPClusterNode(ClusterNode igniteClusterNode) {
        this.igniteClusterNode = igniteClusterNode;
    }
    
    @Override
    public Boolean isClientNode(){
        return igniteClusterNode.isClient();
    }
    @Override
    public Boolean isLocalNode(){
        return igniteClusterNode.isLocal();
    }
    @Override
    public UUID nodeId(){
        return igniteClusterNode.id();
    }
    
    @Override
    public Collection<String> hostNames(){
        return igniteClusterNode.hostNames();
    }
    
}
