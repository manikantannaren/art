/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.common;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.utility.DockerImageName;

/**
 *
 * @author manikantans
 */
public class IgniteContainer extends GenericContainer {

    public IgniteContainer() {
        super(dockerImageName());
    }

    @Override
    protected void waitUntilContainerStarted() {
        super.waitUntilContainerStarted();
        WaitStrategy strategy = getWaitStrategy();
        if (strategy != null) {
            strategy.waitUntilReady(this);
        }
    }

    private static DockerImageName dockerImageName(){
        return DockerImageName.parse("apacheignite/ignite")
                .withTag("latest");
    }
}
