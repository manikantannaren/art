/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.boomi.mashery.ignite.client;

import java.util.Collection;
import java.util.UUID;

/**
 *
 * @author manikantans
 */
public interface IgniteNode {

    Collection<String> hostNames();

    Boolean isClientNode();

    UUID nodeId();

    Boolean isLocalNode();
    
}
