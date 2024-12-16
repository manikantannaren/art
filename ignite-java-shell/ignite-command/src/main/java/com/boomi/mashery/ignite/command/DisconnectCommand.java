/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import java.io.PrintWriter;
import picocli.CommandLine;

/**
 *
 * @author manikantans
 */
@CommandLine.Command(
        name = "disconnect",
        aliases = {"d", "dc"},
        mixinStandardHelpOptions = true,
        description = {
            "Disconnect from an an ignite cluster",},
        footer = {
            "run connect to connect to a cluster"
        },
        requiredOptionMarker = '*',
        subcommands = {CommandLine.HelpCommand.class}
)
public class DisconnectCommand extends AbstractCommand{

    @Override
    public ExitCode call() throws Exception {
        commandContext.clear();
        PrintWriter out = commandContext.getWriter();
        out.println("Disconnected from cache");
        out.println("Use connect command to connect to a cache");
        return ExitCode.SUCCESS;
    }
    
}
