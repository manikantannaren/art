/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import com.boomi.mashery.ignite.client.ClientNode;
import com.boomi.mashery.ignite.client.LocalCache;
import java.io.PrintWriter;
import picocli.CommandLine;

/**
 *
 * @author manikantans
 */
@CommandLine.Command(
        name = "use",
        aliases = {"uc"},
        mixinStandardHelpOptions = true,
        description = {
            "Set current cache to query",
        },
        customSynopsis = {
            "Usage:",
            "usecache <cachename>"
        },
        footer = {
            ""
        },
        requiredOptionMarker = '*',
        subcommands = {CommandLine.HelpCommand.class}
)
public class UseCommand extends AbstractCommand{
    
    @CommandLine.Option(
            names = {"-c","--cache"},
            required = false
    )
    private String cacheName;

    @Override
    public ExitCode call() throws Exception {
        ClientNode node = commandContext.getCacheClient();
        LocalCache<String, Object> cache = node.getCache(cacheName);
        commandContext.setCurrentContext(cache);
        
        PrintWriter out = commandContext.getWriter();
        out.println("Current context : "+cacheName);
        
        return ExitCode.SUCCESS;
    }
}
