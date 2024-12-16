/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import java.io.PrintWriter;
import picocli.CommandLine.Command;

/**
 *
 * @author manikantans
 */
@Command(
        name = "readme",
        aliases = {"r", "rme"},
        mixinStandardHelpOptions = true,
        description = {
            "Provides a readme form of help"
        },
        footer = {
            "Hit Ctrl+d to exit"
        },
        helpCommand = false
)
class ReadmeCommand extends AbstractCommand {

    @Override
    public ExitCode call() throws Exception {
        PrintWriter out = commandContext.getWriter();
        out.println("Hellwo there - here is a readme");
        return ExitCode.SUCCESS;
    }

}
