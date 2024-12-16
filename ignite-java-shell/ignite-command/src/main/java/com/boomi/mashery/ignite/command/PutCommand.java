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
        name = "put",
        aliases = {"p"},
        mixinStandardHelpOptions = true,
        description = {
            "Put a value in the cache",
            "Currently only supports strings"
        },
        requiredOptionMarker = '*',
        subcommands = {CommandLine.HelpCommand.class}
)
public class PutCommand extends AbstractCommand{

    @CommandLine.Option(
            names = {"-g", "--get"},
            required = true
    )
    private String key;
    @CommandLine.Option(
            names = {"-v", "--value"},
            required = true
    )
    private String value;

    @Override
    public ExitCode call() throws Exception {
        LocalCache<String, Object> cache = commandContext.getCurrentContext();
        Boolean inserted = cache.put(key, value);
        PrintWriter out = commandContext.getWriter();
        out.println("Inserted key "+key+"? "+BooleanUtils.toStringYesNo(inserted));
        return ExitCode.SUCCESS;
    }
}
