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
        name = "keyexists",
        aliases = {"ke, keye"},
        mixinStandardHelpOptions = true,
        description = {
            "Checks if a particulr key exists in current cache",
            ""
        },
        customSynopsis = {
            "Usage:",
            "keyexists <key>"
        },
        footer = {},
        requiredOptionMarker = '*',
        subcommands = {CommandLine.HelpCommand.class}
)
public class ContainsKeyCommand extends AbstractCommand {

    @CommandLine.Option(
            names = {"-k", "--key"},
            required = true
    )
    private String key;

    @Override
    public ExitCode call() throws Exception {
        LocalCache<String, Object> cache = commandContext.getCurrentContext();
        Boolean exists = cache.exists(key);
        PrintWriter out = commandContext.getWriter();
        out.println(key + " exists? " + BooleanUtils.toStringYesNo(exists));
        return ExitCode.SUCCESS;
    }

}
