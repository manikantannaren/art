/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import com.boomi.mashery.common.Status;
import java.io.PrintWriter;
import picocli.CommandLine;

/**
 *
 * @author manikantans
 */
@CommandLine.Command(
        name = "status",
        aliases = {"s", "st"},
        mixinStandardHelpOptions = true,
        description = {
            "Provides status information"
        },
        requiredOptionMarker = '*',
        subcommands = {CommandLine.HelpCommand.class}
)
public class StatusCommand extends AbstractCommand{

    @Override
    public ExitCode call() throws Exception {
        Status status = commandContext.status();
        PrintWriter out = commandContext.getWriter();
        CommandUtils.printStatus(status, out);
        return ExitCode.SUCCESS;
    }

}
