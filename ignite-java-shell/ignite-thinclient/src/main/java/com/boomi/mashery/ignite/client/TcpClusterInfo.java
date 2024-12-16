/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.client;

import java.util.Collection;

/**
 *
 * @author manikantans
 */
class TcpClusterInfo implements ClusterInfo{
    private Collection<? extends IgniteNode> nodes;
    private Collection<String> cacheNames;
    private IgniteClientDiscoveryConfiguration config;

    private TcpClusterInfo() {
    }
    
    public static Builder builder(){
        return new Builder();
    }

    @Override
    public Collection<? extends IgniteNode> getNodes() {
        return nodes;
    }

    @Override
    public IgniteClientDiscoveryConfiguration getConfig() {
        return config;
    }
    
    @Override
    public Collection<String> getCacheNames() {
        return cacheNames;
    }
    static class Builder {
        TcpClusterInfo instance;
        public Builder() {
            instance = new TcpClusterInfo();
        }
        public Builder withNodes(Collection<TCPClusterNode> nodes){
            instance.nodes = nodes;
            return this;
        }
        
        public Builder withCaches(Collection<String> cacheNames){
            instance.cacheNames = cacheNames;
            return this;
        }
        
        public Builder withConfiguration(IgniteClientDiscoveryConfiguration config){
            instance.config = config;
            return this;
        }
        
        public ClusterInfo build(){
            return instance;
        }
        
    }
}
