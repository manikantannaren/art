/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

/**
 *
 * @author manikantans
 */
@CommandLine.Command(
        version = {
            "0",
            "0.1"
        }
)

public abstract class AbstractCommand implements Callable<ExitCode> {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    protected @CommandLine.ParentCommand ParentCommand commandContext;
 
    
}
