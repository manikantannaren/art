/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import com.boomi.mashery.ignite.client.LocalCache;
import java.io.PrintWriter;
import org.apache.commons.lang3.BooleanUtils;
import picocli.CommandLine;

/**
 *
 * @author manikantans
 */
@CommandLine.Command(
        name = "delete",
        aliases = {"r", "rm","del"},
        mixinStandardHelpOptions = true,
        description = {
            "Removes an entry from the cache"
        },
        customSynopsis = {
            "Usage:",
            "remove <key>"
        },
        footer = {},
        requiredOptionMarker = '*',
        subcommands = {
            CommandLine.HelpCommand.class
        }
)

public class DeleteKeyCommand extends AbstractCommand{
    
    @CommandLine.Option(
            names={"-k","--key"},
            required = true
    )
    private String key;

    @Override
    public ExitCode call() throws Exception {
        LocalCache<String, Object> cache = commandContext.getCurrentContext();
        Boolean removed = cache.remove(key);
        PrintWriter out = commandContext.getWriter();
        
        out.println("Removed "+key+"? - "+BooleanUtils.toStringYesNo(removed));
        return ExitCode.SUCCESS;
    }
}
