/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import com.boomi.mashery.ignite.client.LocalCache;
import java.io.PrintWriter;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

/**
 *
 * @author manikantans
 */
@CommandLine.Command(
        name = "dumpkeys",
        aliases = {"dk"},
        mixinStandardHelpOptions = true,
        description = {
            "Get a value for a key. NUll if it does not exist"
        },
        requiredOptionMarker = '*',
        subcommands = {CommandLine.HelpCommand.class}
)
public class DumpKeysCommand extends AbstractCommand {

    @CommandLine.Option(
            names = {"-p", "--pattern"},
            required = false,
            description = {
                "Does a substring match of keys to filter"
            }
    )
    private String pattern = "";

    @Override
    public ExitCode call() throws Exception {
        LocalCache<String, Object> cache = commandContext.getCurrentContext();
        Set<String> keys = cache.keys();
        PrintWriter out = commandContext.getWriter();
        //Using Stringutils since it does empty string match as well
        keys.stream().filter(key -> StringUtils.contains(key, pattern))
                .forEach(pk -> {
                    out.println("Key:: " + pk);
                });
        return ExitCode.SUCCESS;
    }

}
