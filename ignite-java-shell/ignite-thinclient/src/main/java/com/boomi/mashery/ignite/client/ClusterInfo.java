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
public interface ClusterInfo {

    Collection<String> getCacheNames();

    IgniteClientDiscoveryConfiguration getConfig();

    Collection<? extends IgniteNode> getNodes();

    
}
